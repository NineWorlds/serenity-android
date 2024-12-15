package us.nineworlds.serenity.ui.util

import android.app.Activity
import android.content.SharedPreferences
import org.assertj.android.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnit
import org.mockito.quality.Strictness
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf
import us.nineworlds.serenity.TestingModule
import us.nineworlds.serenity.core.model.VideoContentInfo
import us.nineworlds.serenity.core.util.TimeUtil
import us.nineworlds.serenity.test.InjectingTest
import us.nineworlds.serenity.ui.video.player.ExoplayerVideoActivity
import java.util.LinkedList

@RunWith(RobolectricTestRunner::class)
class VideoPlayerIntentUtilsTest : InjectingTest() {
  override fun installTestModules() {
    scope.installTestModules(TestingModule())
  }

  @Rule @JvmField val rule = MockitoJUnit.rule().strictness(Strictness.STRICT_STUBS)

  lateinit var videoPlayerIntentUtils: VideoPlayerIntentUtils

  @Mock lateinit var mockSharedPreferences: SharedPreferences
  @Mock lateinit var mockTimeUtil: TimeUtil
  @Mock lateinit var mockVideoContentInfo: VideoContentInfo
  @Mock lateinit var mockActivity : Activity

  val videoQueue = LinkedList<VideoContentInfo>()

  var activity : Activity? = null

  @Before fun setup() {
    videoQueue.clear()
    videoPlayerIntentUtils = VideoPlayerIntentUtils(videoQueue, mockSharedPreferences, mockTimeUtil)
  }

  @After fun tearDown() {
    if (activity != null) {
      activity!!.finish()
    }

    activity = null
  }

  @Test fun playVideoUsingInternalPlayerWhenExternalPlayerIsFalse() {
    videoQueue.add(mockVideoContentInfo)

    activity = Robolectric.setupActivity(Activity::class.java)

    videoPlayerIntentUtils.playVideo(activity, mockVideoContentInfo, false)

    val shadowActivity = shadowOf(activity)

    assertThat(shadowActivity.nextStartedActivity).isNotNull.hasComponent(activity, ExoplayerVideoActivity::class.java)
  }

}