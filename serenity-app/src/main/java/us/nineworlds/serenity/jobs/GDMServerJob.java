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
import us.nineworlds.serenity.common.android.injection.InjectingJob;
import us.nineworlds.serenity.injection.ApplicationContext;
import us.nineworlds.serenity.server.GDMReceiver;

public class GDMServerJob extends InjectingJob {

  @Inject EventBus eventBus;

  @Inject LocalBroadcastManager localBroadcastManager;

  @Inject @ApplicationContext Context context;

  public static final String MSG_RECEIVED = ".GDMService.MESSAGE_RECEIVED";
  public static final String SOCKET_CLOSED = ".GDMService.SOCKET_CLOSED";
  private static final String multicast = "239.0.0.250";

  @Override public void onAdded() {

  }

  @Override public void onRun() throws Throwable {
    DatagramSocket socket = null;
    try {
      socket = new DatagramSocket(32414);
      socket.setBroadcast(true);
      String data = "M-SEARCH * HTTP/1.1\r\n\r\n";
      DatagramPacket packet = new DatagramPacket(data.getBytes(), data.length(), useMultiCastAddress(), 32414);

      socket.send(packet);
      Log.d("GDMService", "Search Packet Broadcasted");

      byte[] buf = new byte[256];
      packet = new DatagramPacket(buf, buf.length);
      socket.setSoTimeout(2000);
      boolean listening = true;
      while (listening) {
        try {
          socket.receive(packet);
          String packetData = new String(packet.getData());
          if (packetData.contains("HTTP/1.0 200 OK")) {
            Log.d("GDMService", "PMS Packet Received");
            // Broadcast Received Packet
            Intent packetBroadcast = new Intent(GDMReceiver.GDM_MSG_RECEIVED);
            packetBroadcast.putExtra("data", packetData);
            packetBroadcast.putExtra("ipaddress", packet.getAddress().toString());

            localBroadcastManager.sendBroadcast(packetBroadcast);
          }
        } catch (SocketTimeoutException e) {
          Log.d("GDMService", "Socket Timeout");
          socket.close();
          listening = false;
          Intent socketBroadcast = new Intent(GDMReceiver.GDM_SOCKET_CLOSED);
          localBroadcastManager.sendBroadcast(socketBroadcast);
        }
      }
    } catch (IOException e) {
      Log.e("GDMService", e.toString());
    } finally {
      try {
        if (socket != null) {
          socket.close();
        }
      } catch (Exception ex) {

      }
    }
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
