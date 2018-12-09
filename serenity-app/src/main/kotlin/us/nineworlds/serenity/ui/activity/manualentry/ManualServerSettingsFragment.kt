package us.nineworlds.serenity.ui.activity.manualentry

import android.app.Fragment
import android.os.Bundle
import android.support.v14.preference.PreferenceDialogFragment
import android.support.v14.preference.PreferenceFragment
import android.support.v17.preference.LeanbackPreferenceFragment
import android.support.v17.preference.LeanbackSettingsFragment
import android.support.v7.preference.DialogPreference
import android.support.v7.preference.Preference
import android.support.v7.preference.PreferenceScreen
import us.nineworlds.serenity.R
import us.nineworlds.serenity.ui.preferences.SettingsFragment

class ManualServerSettingsFragment : LeanbackSettingsFragment(), DialogPreference.TargetFragment {

  lateinit var fragment: ServerSettingsPreferenceFragment

  override fun onPreferenceStartInitialScreen() {
    fragment = ServerSettingsPreferenceFragment()
    startPreferenceFragment(fragment)
  }

  override fun onPreferenceStartFragment(caller: PreferenceFragment, pref: Preference): Boolean {
    val f = Fragment.instantiate(activity, pref.fragment, pref.extras)
    f.setTargetFragment(caller, 0)
    if (f is PreferenceFragment || f is PreferenceDialogFragment) {
      startPreferenceFragment(f)
    } else {
      startImmersiveFragment(f)
    }
    return true
  }

  override fun onPreferenceStartScreen(caller: PreferenceFragment, pref: PreferenceScreen): Boolean {
    val f = SettingsFragment.PrefsFragment()
    val args = Bundle(1)
    args.putString(android.support.v14.preference.PreferenceFragment.ARG_PREFERENCE_ROOT, pref.key)
    f.setArguments(args)
    startPreferenceFragment(f)
    return true
  }

  override fun findPreference(key: CharSequence): Preference {
    return fragment.findPreference(key)
  }

  companion object {
    class ServerSettingsPreferenceFragment : LeanbackPreferenceFragment() {
      override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.manual_server_preferences, rootKey)
      }
    }
  }

}