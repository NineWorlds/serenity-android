package us.nineworlds.serenity.handlers;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.inject.Inject;

import us.nineworlds.serenity.R;
import us.nineworlds.serenity.core.model.Server;
import us.nineworlds.serenity.injection.BaseInjector;
import us.nineworlds.serenity.injection.ForMediaServers;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.widget.Toast;

public class AutoConfigureHandlerRunnable extends BaseInjector implements
		Runnable {

	@Inject
	SharedPreferences preferences;

	@Inject
	@ForMediaServers
	Map<String, Server> mediaServers;

	Context context;

	public AutoConfigureHandlerRunnable(Context context) {
		this.context = context;
	}

	@Override
	public void run() {
		if (mediaServers.isEmpty()) {
			return;
		}
		Server server = mediaServers.values().iterator().next();
		String ipAddress = preferences.getString("server", "");
		if ("".equals(ipAddress)) {
			Editor edit = preferences.edit();
			edit.putString("server", server.getIPAddress());
			edit.apply();
			Toast.makeText(
					context,
					context.getResources().getText(
							R.string.auto_configuring_server_using_)
							+ server.getServerName(), Toast.LENGTH_LONG).show();
			Activity activity = (Activity) context;
			activity.recreate();
		}
	}
}
