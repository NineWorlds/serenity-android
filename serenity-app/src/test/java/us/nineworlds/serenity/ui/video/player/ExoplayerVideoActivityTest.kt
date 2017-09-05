package us.nineworlds.serenity.ui.video.player

import android.annotation.TargetApi
import android.os.Build.VERSION_CODES
import com.google.android.exoplayer2.SimpleExoPlayer
import dagger.Module
import dagger.Provides
import org.assertj.android.api.Assertions
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations.initMocks
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import org.mockito.quality.Strictness.LENIENT
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment.application
import org.robolectric.annotation.Config
import us.nineworlds.serenity.BuildConfig
import us.nineworlds.serenity.TestingModule
import us.nineworlds.serenity.injection.modules.AndroidModule
import us.nineworlds.serenity.injection.modules.SerenityModule
import us.nineworlds.serenity.injection.modules.VideoModule
import us.nineworlds.serenity.test.InjectingTest
import us.nineworlds.serenity.test.ShadowSubtitleView

@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class, sdk = intArrayOf(17), shadows = arrayOf(ShadowSubtitleView::class))
open class ExoplayerVideoActivityTest : InjectingTest() {

  @Rule
  @JvmField
  var mockitoRule: MockitoRule = MockitoJUnit.rule().strictness(LENIENT)

  @Mock lateinit var mockExoPlayerPresenter: ExoplayerPresenter
  @Mock lateinit var mockPlayer: SimpleExoPlayer

  lateinit var activity: ExoplayerVideoActivity

  @TargetApi(VERSION_CODES.KITKAT)
  override fun setUp() {
    initMocks(this)
    super.setUp()
    activity = Robolectric.buildActivity(ExoplayerVideoActivity::class.java).create().get();
  }

  @Test
  fun bindsSimpleExoPlayerView() {
    Assertions.assertThat(activity.playerView).isNotNull
  }

  @Test
  fun onStartDoesNotCallsPresenterPlayBackFromVideoQueueWhenOnAPI19OrHigher() {
    activity.onStart()

    verify(mockExoPlayerPresenter, never()).playBackFromVideoQueue()
  }

  @Test
  @Config(sdk = intArrayOf(24))
  fun onStartCallsPresenterPlayBackFromVideoQueueWhenOnAPI19OrHigher() {
    activity.onStart()

    verify(mockExoPlayerPresenter).playBackFromVideoQueue()
  }

  @Test
  fun onResumeCallsPlayBackFromVideoQueue() {
    activity.onResume()

    verify(mockExoPlayerPresenter).playBackFromVideoQueue()
  }

  @Test
  @Config(sdk = intArrayOf(24))
  fun onResumeDoesNotCallsPlayBackFromVideoQueue() {
    activity.player = mockPlayer
    activity.onResume()

    verify(mockExoPlayerPresenter, never()).playBackFromVideoQueue()
  }

  override fun getModules(): MutableList<Any> =
      mutableListOf(AndroidModule(application),
          SerenityModule(), VideoModule(),
          TestModule(), TestingModule())

  @Module(library = true,
      includes = arrayOf(AndroidModule::class),
      overrides = true,
      injects = arrayOf(ExoplayerVideoActivityTest::class, ExoplayerVideoActivity::class))
  inner class TestModule {

    @Provides
    fun providesExoPlayerPresenter(): ExoplayerPresenter = mockExoPlayerPresenter
  }
}