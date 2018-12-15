package us.nineworlds.serenity.injection.modules.providers;

import android.content.SharedPreferences;
import javax.inject.Inject;
import javax.inject.Provider;
import us.nineworlds.serenity.core.util.StringPreference;

public class ServerClientPreferenceProvider implements Provider<StringPreference> {

    @Inject SharedPreferences sharedPreferences;

    @Override public StringPreference get() {
      return new StringPreference(sharedPreferences, "server_client", "");
    }
  }
