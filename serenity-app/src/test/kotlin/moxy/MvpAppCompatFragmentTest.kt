package moxy

import android.app.Application
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import io.mockk.verifyOrder
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.LooperMode

@RunWith(AndroidJUnit4::class)
class MvpAppCompatFragmentTest {

    // A test-only subclass to allow mocking the delegate
    class TestMvpAppCompatFragment : MvpAppCompatFragment() {
        val mockDelegate: MvpDelegate<TestMvpAppCompatFragment> = mockk(relaxed = true)
        override fun getMvpDelegate(): MvpDelegate<*> = mockDelegate
    }

    @Before
    fun setUp() {
        ApplicationProvider.getApplicationContext<Application>().setTheme(us.nineworlds.serenity.R.style.AppTheme)
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `onCreate calls delegate onCreate`() {
        val scenario = launchFragmentInContainer<TestMvpAppCompatFragment>()
        scenario.onFragment { fragment ->
            verify { fragment.mockDelegate.onCreate(any()) }
        }
    }

    @Test
    fun `onStartAndOnResume_callDelegateOnAttach`() {
        val scenario = launchFragmentInContainer<TestMvpAppCompatFragment>()
        scenario.onFragment { fragment ->
            // onAttach is called by both onStart and onResume
            verify(exactly = 2) { fragment.mockDelegate.onAttach() }
        }
    }

    @Test
    fun `onSaveInstanceState_callsDelegateOnSaveInstanceStateAndOnDetach`() {
        val scenario = launchFragmentInContainer<TestMvpAppCompatFragment>()
        lateinit var mockDelegate: MvpDelegate<*>
        scenario.onFragment { mockDelegate = it.mockDelegate }

        scenario.recreate()

        verifyOrder {
            mockDelegate.onSaveInstanceState(any())
            mockDelegate.onDetach()
        }
    }

    @Test
    fun `onStopAndOnDestroyView_callDelegatesInOrder`() {
        val scenario = launchFragmentInContainer<TestMvpAppCompatFragment>()
        lateinit var mockDelegate: MvpDelegate<*>
        scenario.onFragment { mockDelegate = it.mockDelegate }

        // Move from RESUMED to CREATED. This calls onPause, onStop, and onDestroyView.
        scenario.moveToState(Lifecycle.State.CREATED)

        verifyOrder {
            mockDelegate.onDetach() // From onStop
            mockDelegate.onDetach() // From onDestroyView
            mockDelegate.onDestroyView()
        }
    }

    @Test
    fun `onDestroy_doesNotCallDelegateOnDestroy_duringConfigChange`() {
        val scenario = launchFragmentInContainer<TestMvpAppCompatFragment>()
        lateinit var mockDelegate: MvpDelegate<*>
        scenario.onFragment { mockDelegate = it.mockDelegate }

        scenario.recreate()

        verify(exactly = 0) { mockDelegate.onDestroy() }
    }

    @Test
    fun `onDestroy_callsDelegateOnDestroy_whenActivityIsFinishing`() {
        val fragment = spyk(TestMvpAppCompatFragment())
        val mockDelegate = fragment.mockDelegate
        val mockActivity: FragmentActivity = mockk {
            every { isFinishing } returns true
        }
        every { fragment.requireActivity() } returns mockActivity

        fragment.onDestroy()

        verify { mockDelegate.onDestroy() }
    }

    @Test
    fun `onDestroy_callsDelegateOnDestroy_whenFragmentIsRemoving`() {
        val fragment = spyk(TestMvpAppCompatFragment())
        val mockDelegate = fragment.mockDelegate
        val mockActivity: FragmentActivity = mockk {
            every { isFinishing } returns false
        }
        every { fragment.requireActivity() } returns mockActivity
        every { fragment.isRemoving } returns true

        fragment.onDestroy()

        verify { mockDelegate.onDestroy() }
    }

    @Test
    fun `onDestroy_callsDelegateOnDestroy_whenParentFragmentIsRemoving`() {
        val fragment = spyk(TestMvpAppCompatFragment())
        val mockDelegate = fragment.mockDelegate
        val parentFragment: Fragment = mockk {
            every { isRemoving } returns true
            every { parentFragment } returns null
        }
        val mockActivity: FragmentActivity = mockk {
            every { isFinishing } returns false
        }

        every { fragment.requireActivity() } returns mockActivity
        every { fragment.isRemoving } returns false
        every { fragment.parentFragment } returns parentFragment

        fragment.onDestroy()

        verify { mockDelegate.onDestroy() }
    }
}
