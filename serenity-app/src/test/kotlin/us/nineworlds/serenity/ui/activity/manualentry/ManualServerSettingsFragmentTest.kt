package us.nineworlds.serenity.ui.activity.manualentry

import androidx.test.ext.junit.runners.AndroidJUnit4
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.spyk
import io.mockk.verify
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.annotation.LooperMode
import us.nineworlds.serenity.test.util.FragmentTestUtil

@RunWith(AndroidJUnit4::class)
@LooperMode(LooperMode.Mode.LEGACY)
class ManualServerSettingsFragmentTest {

    private lateinit var fragment: ManualServerSettingsFragment

    @Before
    fun setUp() {
        Robolectric.getForegroundThreadScheduler().pause()

        fragment = spyk(ManualServerSettingsFragment())
        FragmentTestUtil.startFragment(fragment)
    }

    @Test
    fun startsInitialScreen() {
        every { fragment.startPreferenceFragment(any()) } just Runs
        fragment.onPreferenceStartInitialScreen()

        verify { fragment.startPreferenceFragment(any<ManualServerSettingsFragment.Companion.ServerSettingsPreferenceFragment>()) }
    }
}
