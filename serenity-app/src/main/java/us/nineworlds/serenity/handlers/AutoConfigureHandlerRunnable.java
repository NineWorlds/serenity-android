package us.nineworlds.serenity.handlers;

import us.nineworlds.serenity.R;
import us.nineworlds.serenity.SerenityApplication;
import us.nineworlds.serenity.core.model.Server;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.widget.Toast;

public class AutoConfigureHandlerRunnable implements Runnable {

	SharedPreferences preferences;
	Context context;

	public AutoConfigureHandlerRunnable(Context context) {
		preferences = PreferenceManager.getDefaultSharedPreferences(context);
		this.context = context;
	}

	@Override
	public void run() {
		if (SerenityApplication.getPlexMediaServers().isEmpty()) {
			return;
		}
		Server server = SerenityApplication.getPlexMediaServers().values()
				.iterator().next();
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
