package us.nineworlds.serenity.ui.util

import android.app.Activity
import android.content.SharedPreferences
import com.nhaarman.mockito_kotlin.*
import org.assertj.android.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.junit.MockitoJUnit
import org.mockito.quality.Strictness
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.shadows.ShadowAlertDialog
import us.nineworlds.serenity.TestingModule
import us.nineworlds.serenity.core.model.VideoContentInfo
import us.nineworlds.serenity.core.util.TimeUtil
import us.nineworlds.serenity.test.InjectingTest
import java.util.*

@RunWith(RobolectricTestRunner::class)
class VideoPlayerIntentUtilsTest : InjectingTest() {
  override fun installTestModules() {
    scope.installTestModules(TestingModule())
  }

  @Rule @JvmField public val rule = MockitoJUnit.rule().strictness(Strictness.STRICT_STUBS)

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

  @Test fun whenVideoQueueIsEmptyResumeOffsetIsNeverResetToZero() {
    doReturn("http://www.example.com/example.mkv").whenever(mockVideoContentInfo).directPlayUrl
    videoPlayerIntentUtils.launchExternalPlayer(mockVideoContentInfo, mockActivity, false );

    verify(mockVideoContentInfo, never()).resumeOffset = 0
  }

  @Test fun whenVideoQueueIsNotEmptyAndExternalPlayerIsSetToDefaultResetVideoResumeOffset() {
    videoQueue.add(mockVideoContentInfo)
    doReturn("http://www.example.com/example.mkv").whenever(mockVideoContentInfo).directPlayUrl
    doReturn("default").whenever(mockSharedPreferences).getString(anyString(), anyString())
    videoPlayerIntentUtils.launchExternalPlayer(mockVideoContentInfo, mockActivity, false)

    verify(mockVideoContentInfo).resumeOffset = 0
    verify(mockSharedPreferences, atLeastOnce()).getString("serenity_external_player_filter", "default")
  }

  @Test fun showResumeDialogWhenVideoHasBeenPartiallyWatched() {
    videoQueue.add(mockVideoContentInfo)
    doReturn("mxplayer").whenever(mockSharedPreferences).getString(anyString(), anyString())
    doReturn(true).whenever(mockVideoContentInfo).isPartiallyWatched

    activity = Robolectric.setupActivity(Activity::class.java)

    videoPlayerIntentUtils.launchExternalPlayer(mockVideoContentInfo, activity, false)

    val latestAlertDialog = ShadowAlertDialog.getLatestAlertDialog()

    assertThat(latestAlertDialog).isNotNull.isShowing
    verify(mockVideoContentInfo).isPartiallyWatched
  }

}