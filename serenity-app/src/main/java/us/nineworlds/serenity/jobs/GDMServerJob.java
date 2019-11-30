package us.nineworlds.serenity.jobs;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.birbit.android.jobqueue.RetryConstraint;
import us.nineworlds.serenity.common.android.injection.InjectingJob;
import us.nineworlds.serenity.core.logger.Logger;
import us.nineworlds.serenity.server.GDMReceiver;

import javax.inject.Inject;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;

public class GDMServerJob extends InjectingJob {

    private static final int PLEX_BROADCAST_PORT = 32414;
    private static final String PLEX_SEARCH_SERVER_MESSAGE = "M-SEARCH * HTTP/1.1\r\n\r\n";
    private static final String PLEX_SERVER_ACKNOWLEDGEMENT_MESSAGE = "HTTP/1.0 200 OK";
    private static final String PLEX_DATA_EXTRA = "data";
    private static final String PLEX_IPADDRESS_EXTRA = "ipaddress";
    private static final String multicast = "239.0.0.250";

    @Inject LocalBroadcastManager localBroadcastManager;
    @Inject Logger logger;

    protected boolean listening;

    @Override
    public void onAdded() {

    }

    @Override
    public void onRun() throws Throwable {
        try (DatagramSocket socket = new DatagramSocket(32414)) {
            socket.setBroadcast(true);
            String data = PLEX_SEARCH_SERVER_MESSAGE;
            DatagramPacket packet = new DatagramPacket(data.getBytes(), data.length(), useMultiCastAddress(),
                    PLEX_BROADCAST_PORT);

            socket.send(packet);
            logger.debug("Search Packet Broadcasted");

            byte[] buf = new byte[256];
            packet = new DatagramPacket(buf, buf.length);
            socket.setSoTimeout(2000);
            listening = true;
            while (listening) {
                listening = isReceivingPackets(socket, packet);
            }
        } catch (IOException e) {
            logger.error(e.toString(), e);
        }
    }

    protected boolean isReceivingPackets(DatagramSocket socket, DatagramPacket packet)
            throws IOException {
        try {
            socket.receive(packet);
            String packetData = new String(packet.getData());
            if (packetData.contains(PLEX_SERVER_ACKNOWLEDGEMENT_MESSAGE)) {
                logger.debug("PMS Packet Received");
                // Broadcast Received Packet
                Intent packetBroadcast = new Intent(GDMReceiver.GDM_MSG_RECEIVED);
                packetBroadcast.putExtra(PLEX_DATA_EXTRA, packetData);
                packetBroadcast.putExtra(PLEX_IPADDRESS_EXTRA, packet.getAddress().toString());

                localBroadcastManager.sendBroadcast(packetBroadcast);
            }
        } catch (SocketTimeoutException e) {
            logger.error("Socket Timeout", e);
            socket.close();
            listening = false;
            Intent socketBroadcast = new Intent(GDMReceiver.GDM_SOCKET_CLOSED);
            localBroadcastManager.sendBroadcast(socketBroadcast);
        }
        return listening;
    }

    protected InetAddress useMultiCastAddress() throws IOException {
        return InetAddress.getByName(multicast);
    }

    @Override
    protected void onCancel(int cancelReason, @Nullable Throwable throwable) {
    }

    @Override
    protected RetryConstraint shouldReRunOnThrowable(@NonNull Throwable throwable, int runCount, int maxRunCount) {
        return null;
    }
}
