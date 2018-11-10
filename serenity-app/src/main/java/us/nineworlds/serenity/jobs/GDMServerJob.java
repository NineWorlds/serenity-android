package us.nineworlds.serenity.jobs;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import com.birbit.android.jobqueue.RetryConstraint;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import javax.inject.Inject;
import org.greenrobot.eventbus.EventBus;
import us.nineworlds.serenity.common.android.injection.ApplicationContext;
import us.nineworlds.serenity.common.android.injection.InjectingJob;
import us.nineworlds.serenity.core.logger.Logger;
import us.nineworlds.serenity.server.GDMReceiver;

public class GDMServerJob extends InjectingJob {

  public static final int PLEX_BROADCAST_PORT = 32414;
  public static final String PLEX_SEARCH_SERVER_MESSAGE = "M-SEARCH * HTTP/1.1\r\n\r\n";
  public static final String PLEX_SERVER_ACKNOWLEDGEMENT_MESSAGE = "HTTP/1.0 200 OK";
  public static final String PLEX_DATA_EXTRA = "data";
  public static final String PLEX_IPADDRESS_EXTRA = "ipaddress";
  @Inject LocalBroadcastManager localBroadcastManager;
  @Inject @ApplicationContext Context context;
  @Inject Logger logger;

  private static final String multicast = "239.0.0.250";
  protected boolean listening;

  @Override public void onAdded() {

  }

  @Override public void onRun() throws Throwable {
    DatagramSocket socket = null;
    try {
      socket = new DatagramSocket(32414);
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
    } finally {
      try {
        if (socket != null) {
          socket.close();
        }
      } catch (Exception ex) {

      }
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

  @Override protected void onCancel(int cancelReason, @Nullable Throwable throwable) {
  }

  @Override
  protected RetryConstraint shouldReRunOnThrowable(@NonNull Throwable throwable, int runCount, int maxRunCount) {
    return null;
  }
}
