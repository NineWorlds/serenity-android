package us.nineworlds.serenity.ui.preferences;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.v14.preference.PreferenceDialogFragment;
import android.support.v14.preference.PreferenceFragment;
import android.support.v17.preference.LeanbackPreferenceFragment;
import android.support.v17.preference.LeanbackSettingsFragment;
import android.support.v7.preference.DialogPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceScreen;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.inject.Inject;
import us.nineworlds.serenity.R;
import us.nineworlds.serenity.common.Server;
import us.nineworlds.serenity.common.injection.SerenityObjectGraph;
import us.nineworlds.serenity.injection.ForMediaServers;
import us.nineworlds.serenity.ui.activity.OverscanSetupActivity;

public class SettingsFragment extends LeanbackSettingsFragment implements DialogPreference.TargetFragment {

  PrefsFragment fragment;

  @Override public void onPreferenceStartInitialScreen() {
    fragment = new PrefsFragment();
    startPreferenceFragment(fragment);
  }

  @Override public boolean onPreferenceStartFragment(PreferenceFragment caller, Preference pref) {
    final Fragment f =
        Fragment.instantiate(getActivity(), pref.getFragment(), pref.getExtras());
    f.setTargetFragment(caller, 0);
    if (f instanceof PreferenceFragment || f instanceof PreferenceDialogFragment) {
      startPreferenceFragment(f);
    } else {
      startImmersiveFragment(f);
    }
    return true;
  }

  @Override public boolean onPreferenceStartScreen(PreferenceFragment caller, PreferenceScreen pref) {
    final Fragment f = new PrefsFragment();
    final Bundle args = new Bundle(1);
    args.putString(PreferenceFragment.ARG_PREFERENCE_ROOT, pref.getKey());
    f.setArguments(args);
    startPreferenceFragment(f);
    return true;
  }

  @Override public Preference findPreference(CharSequence key) {
    return fragment.findPreference(key);
  }

  public static class PrefsFragment extends LeanbackPreferenceFragment {
    @Inject @ForMediaServers Map<String, Server> mediaServers;

    public PrefsFragment() {
      SerenityObjectGraph.Companion.getInstance().inject(this);
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
      // Load the preferences from an XML resource
      setPreferencesFromResource(R.xml.preferences, rootKey);
    }

    @Override public void onStart() {
      super.onStart();
     // populateAvailablePlexMediaServers();
     // populateAvailableLocales();
      populateSupportedPlayers();
    }

    @Override public boolean onPreferenceTreeClick(Preference preference) {
      final String key = preference.getKey();
      if ("overscan_setup".equals(key)) {
        startActivity(new Intent(getActivity(), OverscanSetupActivity.class));
        return true;
      }
      return false;
    }

    /**
     * Populates the Discovered Devices preference list with available Plex
     * Media Servers. The name of the Device and the device's IP address are
     * used as values in the entry. The user friendly name is used from the
     * device itself.
     */
    protected void populateAvailablePlexMediaServers() {
      ListPreference discoveredServers = (ListPreference) findPreference("discoveredServer");
      if (mediaServers.isEmpty()) {
        discoveredServers.setEnabled(false);
        return;
      }

      discoveredServers.setEnabled(true);
      String entries[] = new String[mediaServers.size()];
      String values[] = new String[mediaServers.size()];

      mediaServers.keySet().toArray(entries);
      discoveredServers.setEntries(entries);

      ArrayList<String> ipAddresses = new ArrayList<String>();
      Iterator<Map.Entry<String, Server>> entIt = mediaServers.entrySet().iterator();
      while (entIt.hasNext()) {
        Map.Entry<String, Server> servers = entIt.next();
        Server device = servers.getValue();
        ipAddresses.add(device.getIPAddress());
      }
      if (!ipAddresses.isEmpty()) {
        ipAddresses.toArray(values);
        discoveredServers.setEntryValues(values);
      }
    }

    protected void populateAvailableLocales() {
      Locale[] locales = Locale.getAvailableLocales();
      ListPreference preferenceLocales = (ListPreference) findPreference("preferred_subtitle_language");
      ArrayList<String> localNames = new ArrayList<String>();
      ArrayList<String> localCodes = new ArrayList<String>();
      for (Locale local : locales) {
        if (!localNames.contains(local.getDisplayLanguage())) {
          localNames.add(local.getDisplayLanguage());
          localCodes.add(local.getISO3Language());
        }
      }
      String entries[] = new String[localNames.size()];
      String values[] = new String[localCodes.size()];
      localNames.toArray(entries);
      localCodes.toArray(values);
      preferenceLocales.setEntries(entries);
      preferenceLocales.setEntryValues(values);
    }

    protected void populateSupportedPlayers() {
      ListPreference supportedPlayers = (ListPreference) findPreference("serenity_external_player_filter");
      Map<String, String> availablePlayers = new HashMap<String, String>();
      Context context = getActivity();
      if (hasPlayerByName(context, "com.mxtech.videoplayer.ad")) {
        availablePlayers.put("mxplayer", "MX Player");
      }

      if (hasPlayerByName(context, "com.mxtech.videoplayer.pro")) {
        availablePlayers.put("mxplayerpro", "MX Player Pro");
      }

      if (hasPlayerByName(context, "net.gtvbox.videoplayer")) {
        availablePlayers.put("vimu", "ViMu Player");
      }

      availablePlayers.put("default", "System Default");

      String[] entries = new String[availablePlayers.size()];
      String[] values = new String[availablePlayers.size()];
      ArrayList playerNames = new ArrayList();
      ArrayList playerValues = new ArrayList();

      for (Map.Entry entry : availablePlayers.entrySet()) {
        playerNames.add(entry.getValue());
        playerValues.add(entry.getKey());
      }

      playerNames.toArray(entries);
      playerValues.toArray(values);

      supportedPlayers.setEntries(entries);
      supportedPlayers.setEntryValues(values);
    }

    protected boolean hasPlayerByName(Context context, String playerPackageName) {

      final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
      mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
      final List<ResolveInfo> pkgAppsList = context.getPackageManager().queryIntentActivities(mainIntent, 0);

      for (ResolveInfo resolveInfo : pkgAppsList) {
        String packageName = resolveInfo.activityInfo.packageName;
        if (packageName.contains(playerPackageName)) {
          return true;
        }
      }

      return false;
    }
  }
}
