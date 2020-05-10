package us.nineworlds.serenity.ui.activity.manualentry

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doNothing
import com.nhaarman.mockitokotlin2.spy
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.LooperMode
import us.nineworlds.serenity.test.util.FragmentTestUtil

@RunWith(AndroidJUnit4::class)
@LooperMode(LooperMode.Mode.LEGACY)
class ManualServerSettingsFragmentTest {

  private lateinit var fragment: ManualServerSettingsFragment

  @Before
  fun setUp() {
    Robolectric.getForegroundThreadScheduler().pause()

    fragment = spy(ManualServerSettingsFragment())
    FragmentTestUtil.startFragment(fragment)
  }

  @Test
  fun startsInitialScreen() {
    doNothing().whenever(fragment).startPreferenceFragment(any())
    fragment.onPreferenceStartInitialScreen()

    verify(fragment).startPreferenceFragment(any<ManualServerSettingsFragment.Companion.ServerSettingsPreferenceFragment>())
  }

}