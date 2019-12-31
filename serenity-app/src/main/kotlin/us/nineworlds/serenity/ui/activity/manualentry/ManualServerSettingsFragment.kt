package us.nineworlds.serenity.ui.activity.manualentry

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.leanback.preference.LeanbackPreferenceFragmentCompat
import androidx.leanback.preference.LeanbackSettingsFragmentCompat
import androidx.preference.Preference
import androidx.preference.PreferenceDialogFragmentCompat
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceScreen
import us.nineworlds.serenity.R
import us.nineworlds.serenity.ui.preferences.SettingsFragment

class ManualServerSettingsFragment : LeanbackSettingsFragmentCompat() {

  lateinit var fragment: ServerSettingsPreferenceFragment

  override fun onPreferenceStartInitialScreen() {
    fragment = ServerSettingsPreferenceFragment()
    startPreferenceFragment(fragment)
  }

  override fun onPreferenceStartFragment(caller: PreferenceFragmentCompat, pref: Preference): Boolean {
    val f = Fragment.instantiate(requireContext(), pref.fragment, pref.extras)
    f.setTargetFragment(caller, 0)
    if (f is PreferenceFragmentCompat || f is PreferenceDialogFragmentCompat) {
      startPreferenceFragment(f)
    } else {
      startImmersiveFragment(f)
    }
    return true
  }

  override fun onPreferenceStartScreen(caller: PreferenceFragmentCompat, pref: PreferenceScreen): Boolean {
    val f = SettingsFragment.PrefsFragment()
    val args = Bundle(1)
    args.putString(PreferenceFragmentCompat.ARG_PREFERENCE_ROOT, pref.key)
    f.setArguments(args)
    startPreferenceFragment(f as Fragment)
    return true
  }

//  override fun findPreference(key: CharSequence): Preference {
//    return fragment.findPreference(key)
//  }

  companion object {
    class ServerSettingsPreferenceFragment : LeanbackPreferenceFragmentCompat() {
      override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.manual_server_preferences, rootKey)
      }
    }
  }
}