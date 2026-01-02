package us.nineworlds.serenity

import android.app.Application
import android.content.SharedPreferences
import android.content.res.Resources
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import androidx.test.core.app.ApplicationProvider
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isNotNull
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import java.util.LinkedList
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.LooperMode
import toothpick.Toothpick
import toothpick.config.Module
import us.nineworlds.serenity.core.model.VideoContentInfo
import us.nineworlds.serenity.core.util.AndroidHelper
import us.nineworlds.serenity.fragments.MainMenuFragment
import us.nineworlds.serenity.injection.ForVideoQueue
import us.nineworlds.serenity.test.InjectingTest
import us.nineworlds.serenity.ui.util.VideoPlayerIntentUtils

@RunWith(RobolectricTestRunner::class)
@Config(qualifiers = "large")
@LooperMode(LooperMode.Mode.PAUSED)
class MainActivityTest : InjectingTest() {

    private val mockSharedPreferences: SharedPreferences = mockk(relaxed = true)
    private val mockAndroidHelper: AndroidHelper = mockk(relaxed = true)
    private val mockVideoQueue: LinkedList<VideoContentInfo> = mockk()
    private val mockVPUtils: VideoPlayerIntentUtils = mockk()

    private lateinit var activity: MainActivity

    private val mockPresenter: MainPresenter = mockk(relaxed = true)

    @Before
    override fun setUp() {
        super.setUp()

        every { mockSharedPreferences.getBoolean("serenity_first_run", true) } returns true
        every { mockSharedPreferences.getBoolean("watched_status_firsttime", true) } returns true

        activity = Robolectric.buildActivity(MainActivity::class.java).create().get()

        val fragmentManager = activity.supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.add(MainMenuFragment(), null)
        fragmentTransaction.commit()
    }

    @After
    fun tearDown() {
        activity.finish()
        clearAllMocks()
        Toothpick.reset()
    }

    @Test
    fun `users button is not null`() {
        val usersButton = activity.findViewById<ImageButton>(R.id.iv_users)
        assertThat(usersButton).isNotNull()
    }

    @Test
    fun `presenter is injected`() {
        assertThat(activity.presenter).isEqualTo(mockPresenter)
    }

    @Test
    fun `assert that main activity is created`() {
        assertThat(activity).isNotNull()
        assertThat(activity.isFinishing).isFalse()
    }

    @Test
    fun `creates menu`() {
        val gallery = activity.findViewById<ImageView>(R.id.mainGalleryBackground)
        assertThat(gallery.visibility).isEqualTo(View.VISIBLE)
    }

    override fun installTestModules() {
        scope.installTestModules(MockkTestingModule(), TestModule())
    }

    inner class TestModule : Module() {
        init {
            bind(SharedPreferences::class.java).toInstance(mockSharedPreferences)
            bind(AndroidHelper::class.java).toInstance(mockAndroidHelper)
            bind(LinkedList::class.java).withName(ForVideoQueue::class.java).toInstance(mockVideoQueue)
            bind(Resources::class.java).toInstance(ApplicationProvider.getApplicationContext<Application>().resources)
            bind(VideoPlayerIntentUtils::class.java).toInstance(mockVPUtils)
            bind(MainPresenter::class.java).toInstance(mockPresenter)
        }
    }
}
