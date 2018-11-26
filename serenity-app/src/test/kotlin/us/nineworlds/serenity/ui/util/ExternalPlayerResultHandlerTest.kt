package us.nineworlds.serenity.ui.util

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.view.View
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
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
import toothpick.config.Module
import us.nineworlds.serenity.TestingModule
import us.nineworlds.serenity.core.model.VideoContentInfo
import us.nineworlds.serenity.injection.ForVideoQueue
import us.nineworlds.serenity.test.InjectingTest
import us.nineworlds.serenity.ui.adapters.AbstractPosterImageGalleryAdapter
import java.util.*

@RunWith(RobolectricTestRunner::class)
class ExternalPlayerResultHandlerTest : InjectingTest() {

  @Rule @JvmField public val rule = MockitoJUnit.rule().strictness(Strictness.STRICT_STUBS)

  @Mock lateinit var mockVideoPlayerIntentUtils: VideoPlayerIntentUtils
  @Mock lateinit var mockAbstractPosterImageGalleryAdapter: AbstractPosterImageGalleryAdapter
  @Mock lateinit var mockSharedPreferences: SharedPreferences
  @Mock lateinit var mockIntent: Intent
  @Mock lateinit var mockVideoContentInfo: VideoContentInfo
  @Mock lateinit var mockView: View
  @Mock lateinit var mockVideoQueue: LinkedList<VideoContentInfo>

  lateinit var resultHander: ExternalPlayerResultHandler
  lateinit var activity: Activity

  @Before override fun setUp() {
    super.setUp()
    activity = Robolectric.setupActivity(Activity::class.java)
    resultHander = ExternalPlayerResultHandler(0, mockIntent, activity, mockAbstractPosterImageGalleryAdapter)
  }

  @After fun tearDown() {
    activity.finish()
  }

  @Test fun updatePlaybackPositionUpdatesPlaybackPositionForMxPlayer() {
    resultHander.extPlayerVideoQueueEnabled = false
    resultHander.externalPlayerValue = "mxplayer"
    doReturn("playback_completion").whenever(mockIntent).getStringExtra("end_by")
    doReturn(100).whenever(mockVideoContentInfo).duration

    resultHander.updatePlaybackPosition(mockVideoContentInfo, mockView)

    verify(mockVideoContentInfo).resumeOffset = 100
    verify(mockAbstractPosterImageGalleryAdapter).notifyDataSetChanged()
    verify(mockIntent).getStringExtra("end_by")
  }

  @Test fun updatePlaysNextVideoInQueue() {
    resultHander.extPlayerVideoQueueEnabled = true
    resultHander.externalPlayerValue = "mxplayer"
    resultHander.resultCode = 100
    doReturn("playback_completion").whenever(mockIntent).getStringExtra("end_by")
    doReturn(100).whenever(mockVideoContentInfo).duration
    doReturn(false).whenever(mockVideoQueue).isEmpty()
    doReturn(mockVideoContentInfo).whenever(mockVideoQueue).poll()

    resultHander.updatePlaybackPosition(mockVideoContentInfo, mockView)

    verify(mockVideoContentInfo).resumeOffset = 100
    verify(mockAbstractPosterImageGalleryAdapter).notifyDataSetChanged()
    verify(mockIntent).getStringExtra("end_by")
    verify(mockVideoQueue).isEmpty()
    verify(mockVideoQueue).poll()
    verify(mockVideoPlayerIntentUtils).launchExternalPlayer(mockVideoContentInfo, activity, false)
  }

  override fun installTestModules() {
    scope.installTestModules(TestingModule(), TestModule())
  }

  inner class TestModule : Module() {
    init {
      bind(VideoPlayerIntentUtils::class.java).toInstance(mockVideoPlayerIntentUtils)
      bind(SharedPreferences::class.java).toInstance(mockSharedPreferences)
      bind(LinkedList::class.java).withName(ForVideoQueue::class.java).toInstance(mockVideoQueue)
    }
  }

}