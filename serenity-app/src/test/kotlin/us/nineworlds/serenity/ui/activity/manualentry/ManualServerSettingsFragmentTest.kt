package us.nineworlds.serenity.ui.activity.manualentry

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.doNothing
import com.nhaarman.mockito_kotlin.spy
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import us.nineworlds.serenity.test.util.FragmentTestUtil

@RunWith(RobolectricTestRunner::class)
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