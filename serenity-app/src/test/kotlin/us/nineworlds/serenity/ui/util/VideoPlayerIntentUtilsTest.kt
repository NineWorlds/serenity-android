package us.nineworlds.serenity.ui.util

import android.app.Activity
import android.content.SharedPreferences
import io.mockk.mockk
import java.util.LinkedList
import org.assertj.android.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf
import us.nineworlds.serenity.MockkTestingModule
import us.nineworlds.serenity.core.model.VideoContentInfo
import us.nineworlds.serenity.core.util.TimeUtil
import us.nineworlds.serenity.test.InjectingTest
import us.nineworlds.serenity.ui.video.player.ExoplayerVideoActivity

@RunWith(RobolectricTestRunner::class)
class VideoPlayerIntentUtilsTest : InjectingTest() {
    override fun installTestModules() {
        scope.installTestModules(MockkTestingModule())
    }

    private lateinit var videoPlayerIntentUtils: VideoPlayerIntentUtils

    private companion object {
        private val mockSharedPreferences = mockk<SharedPreferences>(relaxed = true)
        private val mockTimeUtil = mockk<TimeUtil>(relaxed = true)
        private val mockVideoContentInfo = mockk<VideoContentInfo>(relaxed = true)
    }

    private val videoQueue = LinkedList<VideoContentInfo>()

    private lateinit var activity: Activity

    @Before fun setup() {
        videoQueue.clear()
        videoPlayerIntentUtils = VideoPlayerIntentUtils(videoQueue, mockSharedPreferences, mockTimeUtil)
    }

    @After fun tearDown() {
        activity.finish()
    }

    @Test fun playVideoUsingInternalPlayerWhenExternalPlayerIsFalse() {
        videoQueue.add(mockVideoContentInfo)

        activity = Robolectric.setupActivity(Activity::class.java)

        videoPlayerIntentUtils.playVideo(activity, mockVideoContentInfo, false)

        val shadowActivity = shadowOf(activity)

        assertThat(shadowActivity.nextStartedActivity).isNotNull.hasComponent(activity, ExoplayerVideoActivity::class.java)
    }
}
