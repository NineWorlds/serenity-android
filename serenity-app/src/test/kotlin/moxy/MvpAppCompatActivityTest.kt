package moxy

import android.os.Bundle
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.mockk.clearAllMocks
import io.mockk.mockk
import io.mockk.verify
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.android.controller.ActivityController

@RunWith(AndroidJUnit4::class)
class MvpAppCompatActivityTest {

    private lateinit var controller: ActivityController<TestMvpAppCompatActivity>

    // A test-only subclass to allow mocking the delegate
    class TestMvpAppCompatActivity : MvpAppCompatActivity() {
        val mockDelegate: MvpDelegate<MvpAppCompatActivity> = mockk(relaxed = true)
        override fun getMvpDelegate(): MvpDelegate<*> = mockDelegate
    }

    @After
    fun tearDown() {
        if (::controller.isInitialized) {
            controller.destroy()
        }
        clearAllMocks()
    }

    @Test
    fun `onCreate calls delegate onCreate`() {
        controller = Robolectric.buildActivity(TestMvpAppCompatActivity::class.java).create()
        val activity = controller.get()

        verify { activity.mockDelegate.onCreate(any()) }
    }

    @Test
    fun `onStart calls delegate onAttach`() {
        controller = Robolectric.buildActivity(TestMvpAppCompatActivity::class.java).create().start()
        val activity = controller.get()

        verify { activity.mockDelegate.onAttach() }
    }

    @Test
    fun `onResume calls delegate onAttach`() {
        controller = Robolectric.buildActivity(TestMvpAppCompatActivity::class.java).create().start().resume()
        val activity = controller.get()

        // onAttach is called by both onStart and onResume
        verify(exactly = 2) { activity.mockDelegate.onAttach() }
    }

    @Test
    fun `onSaveInstanceState calls delegate onSaveInstanceState and onDetach`() {
        controller = Robolectric.buildActivity(TestMvpAppCompatActivity::class.java).create().start().resume()
        val activity = controller.get()
        val outState = Bundle()

        controller.saveInstanceState(outState)

        verify { activity.mockDelegate.onSaveInstanceState(outState) }
        verify { activity.mockDelegate.onDetach() }
    }

    @Test
    fun `onStop calls delegate onDetach`() {
        controller = Robolectric.buildActivity(TestMvpAppCompatActivity::class.java).create().start().resume().stop()
        val activity = controller.get()

        // onDetach is called once by onStop
        verify(exactly = 1) { activity.mockDelegate.onDetach() }
    }

    @Test
    fun `onDestroy calls delegate onDestroyView and onDestroy when finishing`() {
        val controller = Robolectric.buildActivity(TestMvpAppCompatActivity::class.java).create().start().resume()
        val activity = controller.get()

        activity.finish()
        controller.destroy()

        verify { activity.mockDelegate.onDestroyView() }
        verify { activity.mockDelegate.onDestroy() }
    }

    @Test
    fun `onDestroy calls only onDestroyView when not finishing`() {
        // 1. Create the initial activity and grab its mock delegate
        val controller = Robolectric.buildActivity(TestMvpAppCompatActivity::class.java).create().start().resume()
        val activity = controller.get()
        val mockDelegate = activity.mockDelegate

        // 2. Simulate a configuration change, which destroys the old activity instance
        controller.recreate()

        // 3. Verify on the original delegate
        verify { mockDelegate.onDestroyView() }
        verify(exactly = 0) { mockDelegate.onDestroy() }

        controller.destroy()
    }
}
