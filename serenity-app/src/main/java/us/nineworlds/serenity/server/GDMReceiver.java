package us.nineworlds.serenity.server;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import java.util.Map;
import javax.inject.Inject;
import toothpick.Scope;
import toothpick.Toothpick;
import us.nineworlds.plex.server.GDMServer;
import us.nineworlds.serenity.common.Server;
import us.nineworlds.serenity.common.annotations.InjectionConstants;
import us.nineworlds.serenity.injection.ForMediaServers;
import us.nineworlds.serenity.injection.InjectingBroadcastReceiver;

public class GDMReceiver extends InjectingBroadcastReceiver {

  public static final String GDM_MSG_RECEIVED = ".GDMService.MESSAGE_RECEIVED";
  public static final String GDM_SOCKET_CLOSED = ".GDMService.SOCKET_CLOSED";

  Scope objectGraph;

  @Inject @ForMediaServers Map<String, Server> servers;

  @Override public void onReceive(Context context, Intent intent) {
    if (objectGraph == null) {
      objectGraph = Toothpick.openScope(InjectionConstants.APPLICATION_SCOPE);
      Toothpick.inject(this, objectGraph);
    }

    if (intent.getAction().equals(GDM_MSG_RECEIVED)) {
      String message = intent.getStringExtra("data").trim();
      String ipAddress = intent.getStringExtra("ipaddress").substring(1);
      Server server = new GDMServer();

      int namePos = message.indexOf("Name: ");
      namePos += 6;
      int crPos = message.indexOf("\r", namePos);
      String serverName = message.substring(namePos, crPos);

      server.setServerName(serverName);
      server.setIPAddress(ipAddress);
      if (!servers.containsKey(serverName)) {
        servers.put(serverName, server);
        Log.d(getClass().getName(), "Adding " + serverName);
      } else {
        Log.d(getClass().getName(), serverName + " already added.");
      }
    } else if (intent.getAction().equals(GDM_SOCKET_CLOSED)) {
      Log.i("GDMService", "Finished Searching");
    }
  }
}
