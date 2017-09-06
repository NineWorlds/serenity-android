package us.nineworlds.serenity.ui.video.player

import dagger.Module
import dagger.Provides
import org.apache.commons.lang3.RandomStringUtils
import org.assertj.core.api.Java6Assertions.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString
import org.mockito.ArgumentMatchers.eq
import org.mockito.Mock
import org.mockito.Mockito.doReturn
import org.mockito.Mockito.never
import org.mockito.Mockito.spy
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import org.mockito.quality.Strictness.LENIENT
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import us.nineworlds.plex.rest.PlexappFactory
import us.nineworlds.serenity.BuildConfig
import us.nineworlds.serenity.TestingModule
import us.nineworlds.serenity.core.model.VideoContentInfo
import us.nineworlds.serenity.injection.ForVideoQueue
import us.nineworlds.serenity.injection.modules.AndroidModule
import us.nineworlds.serenity.test.InjectingTest
import java.util.LinkedList
import javax.inject.Inject
import javax.inject.Singleton

@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class)
class ExoplayerPresenterTest : InjectingTest() {

  @Rule
  @JvmField
  var mockitoRule: MockitoRule = MockitoJUnit.rule().strictness(LENIENT)

  @Inject lateinit var mockPlexFactory: PlexappFactory
  @Mock lateinit var mockVideoContentInfo: VideoContentInfo
  @Mock lateinit var mockVideoQueue: LinkedList<VideoContentInfo>
  @Mock lateinit var mockView: ExoplayerContract.ExoplayerView

  private lateinit var presenter: ExoplayerPresenter

  @Before
  override fun setUp() {
    super.setUp()
    presenter = spy(ExoplayerPresenter())
    doReturn(mockView).`when`(presenter).viewState
  }

  @Test
  fun playBackFromVideoQueueDoesNothingWhenEmpty() {
    doReturn(true).`when`(mockVideoQueue).isEmpty()

    presenter.playBackFromVideoQueue()

    verify(mockVideoQueue, never()).poll()
    verify(mockVideoQueue).isEmpty()
    verify(mockView, never()).initializePlayer(anyString(), eq(0))
  }

  @Test
  fun playBackFromVideoQueuePopulatesVideoWhenPolled() {
    val expectedId = RandomStringUtils.randomNumeric(1)
    val expectedUrl = "http://www.example.com/start.mkv"

    doReturn(mockVideoContentInfo).`when`(mockVideoQueue).poll()
    doReturn(false).`when`(mockVideoQueue).isEmpty()
    doReturn("avi").`when`(mockVideoContentInfo).container
    doReturn(expectedId).`when`(mockVideoContentInfo).id()
    doReturn(expectedUrl).`when`(mockPlexFactory).getTranscodeUrl(anyString(), anyInt())

    presenter.playBackFromVideoQueue()

    assertThat(presenter.video).isNotNull().isEqualTo(mockVideoContentInfo)
    verify(mockVideoQueue).poll()
    verify(mockVideoContentInfo).container
    verify(mockVideoQueue).isEmpty()
    verify(mockVideoContentInfo).id()
    verify(mockPlexFactory).getTranscodeUrl(expectedId, 0)
    verify(mockView).initializePlayer(expectedUrl, 0)
  }

  override fun getModules(): MutableList<Any> =
      mutableListOf(AndroidModule(RuntimeEnvironment.application),
          TestModule(), TestingModule())

  @Module(library = true,
      includes = arrayOf(AndroidModule::class),
      overrides = true,
      injects = arrayOf(ExoplayerPresenterTest::class, ExoplayerPresenter::class))
  inner class TestModule {

    @Provides
    @Singleton
    @ForVideoQueue
    fun providesVideoQueue(): LinkedList<VideoContentInfo> {
      return mockVideoQueue
    }

  }
}