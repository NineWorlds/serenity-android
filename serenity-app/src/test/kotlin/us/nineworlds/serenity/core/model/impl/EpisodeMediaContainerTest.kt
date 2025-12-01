package us.nineworlds.serenity.core.model.impl

import android.content.res.Resources
import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import assertk.assertions.isNotEmpty
import assertk.assertions.isNotNull
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import org.junit.After
import org.junit.Before
import org.junit.Test
import toothpick.config.Module
import us.nineworlds.serenity.common.android.mediacodec.MediaCodecInfoUtil
import us.nineworlds.serenity.common.media.model.IMedia
import us.nineworlds.serenity.common.media.model.IMediaContainer
import us.nineworlds.serenity.common.media.model.IPart
import us.nineworlds.serenity.common.media.model.IVideo
import us.nineworlds.serenity.common.rest.SerenityClient
import us.nineworlds.serenity.core.util.AndroidHelper
import us.nineworlds.serenity.test.InjectingTest

class EpisodeMediaContainerTest : InjectingTest() {

  private val mockSerenityClient: SerenityClient = mockk(relaxed = true)
  private val mockResources: Resources = mockk(relaxed = true)
  private val mockAndroidHelper: AndroidHelper = mockk(relaxed = true)

  private lateinit var episodeMediaContainer: EpisodeMediaContainer

  override fun installTestModules() {
    scope.installTestModules(TestModule())
  }

  @Before
  override fun setUp() {
    super.setUp()
    every { mockSerenityClient.baseURL() } returns "http://localhost:8096/"
    mockkObject(MediaCodecInfoUtil)
  }

  @After
  fun tearDown() {
    clearAllMocks()
  }

  @Test
  fun `createVideoContent populates video list with correct episode info`() {
    val mockMediaContainer = mockk<IMediaContainer>(relaxed = true)
    val mockVideo = mockk<IVideo>(relaxed = true)
    val mockMedia = mockk<IMedia>(relaxed = true)
    val mockPart = mockk<IPart>(relaxed = true)

    every { mockMediaContainer.videos } returns listOf(mockVideo)
    every { mockMediaContainer.parentPosterURL } returns "/library/metadata/show/1"
    every { mockVideo.medias } returns listOf(mockMedia)
    every { mockMedia.videoPart } returns listOf(mockPart)

    every { mockVideo.key } returns "/library/metadata/12345"
    every { mockVideo.title } returns "Episode Title"
    every { mockVideo.summary } returns "The summary of the episode."
    every { mockVideo.thumbNailImageKey } returns "/library/metadata/12345/thumb/123"
    every { mockVideo.backgroundImageKey } returns "/library/metadata/12345/art/456"
    every { mockVideo.parentKey } returns "parentKey"
    every { mockVideo.viewCount } returns 1
    every { mockVideo.viewOffset } returns 1234
    every { mockVideo.duration } returns 5678
    every { mockVideo.originallyAvailableDate } returns "2023-10-27"
    every { mockVideo.season } returns "2"
    every { mockVideo.episode } returns "5"
    every { mockVideo.grandParentTitle } returns "Series Title"
    every { mockVideo.contentRating } returns "TV-PG"
    every { mockMedia.container } returns "mkv"
    every { mockPart.key } returns "/library/parts/54321/file.mkv"
    every { mockMedia.audioCodec } returns "aac"
    every { mockMedia.videoCodec } returns "h264"
    every { mockMedia.videoResolution } returns "720"
    every { mockMedia.aspectRatio } returns "1.78"
    every { mockMedia.audioChannels } returns "6"

    every { MediaCodecInfoUtil.isCodecSupported(any()) } returns true
    every { mockAndroidHelper.isAudioPassthroughSupported(any()) } returns false
    every { MediaCodecInfoUtil.findCorrectAudioMimeType(any()) } returns "audio/aac"
    every { MediaCodecInfoUtil.findCorrectVideoMimeType(any()) } returns "video/avc"

    episodeMediaContainer = EpisodeMediaContainer(mockMediaContainer)

    episodeMediaContainer.createVideos()

    assertThat(episodeMediaContainer.videoList).isNotNull().hasSize(1)

    val videoContentInfo = episodeMediaContainer.videoList!![0] as EpisodePosterInfo
    assertThat(videoContentInfo.id()).isEqualTo("/library/metadata/12345")
    assertThat(videoContentInfo.getTitle()).isEqualTo("Episode Title")
    assertThat(videoContentInfo.getSummary()).isEqualTo("The summary of the episode.")
    assertThat(videoContentInfo.getImageURL()).isEqualTo("http://localhost:8096/library/metadata/12345/thumb/123")
    assertThat(videoContentInfo.getBackgroundURL()).isEqualTo("http://localhost:8096/library/metadata/12345/art/456")
    assertThat(videoContentInfo.parentKey).isEqualTo("parentKey")
    assertThat(videoContentInfo.viewCount).isEqualTo(1)
    assertThat(videoContentInfo.resumeOffset).isEqualTo(1234)
    assertThat(videoContentInfo.duration).isEqualTo(5678)
    assertThat(videoContentInfo.originalAirDate).isEqualTo("2023-10-27")
    assertThat(videoContentInfo.seasonNumber).isEqualTo(2)
    assertThat(videoContentInfo.episodeNumber).isEqualTo(5)
    assertThat(videoContentInfo.seriesTitle).isEqualTo("Series Title")
    assertThat(videoContentInfo.contentRating).isEqualTo("TV-PG")
    assertThat(videoContentInfo.container).isEqualTo("mkv")
    assertThat(videoContentInfo.directPlayUrl).isEqualTo("http://localhost:8096/library/parts/54321/file.mkv")
    assertThat(videoContentInfo.audioCodec).isEqualTo("aac")
    assertThat(videoContentInfo.videoCodec).isEqualTo("h264")
    assertThat(videoContentInfo.videoResolution).isEqualTo("720")
    assertThat(videoContentInfo.aspectRatio).isEqualTo("1.78")
    assertThat(videoContentInfo.audioChannels).isEqualTo("6")
  }

  @Test
  fun `createVideoContent handles null videos list gracefully`() {
    val mockMediaContainer = mockk<IMediaContainer>(relaxed = true)
    every { mockMediaContainer.videos } returns null
    every { mockMediaContainer.parentPosterURL } returns "/library/metadata/show/1"

    episodeMediaContainer = EpisodeMediaContainer(mockMediaContainer)
    episodeMediaContainer.createVideos()

    assertThat(episodeMediaContainer.videoList).isNotNull().isEmpty()
  }

  @Test
  fun `backgroundUrl uses mediaContainerArt when episodeBackgroundIsNull`() {
    val mockMediaContainer = mockk<IMediaContainer>(relaxed = true)
    val mockVideo = mockk<IVideo>(relaxed = true)

    every { mockMediaContainer.videos } returns listOf(mockVideo)
    every { mockMediaContainer.art } returns "/library/art/show/1"
    every { mockMediaContainer.parentPosterURL } returns "/library/metadata/show/1"
    every { mockVideo.backgroundImageKey } returns null

    episodeMediaContainer = EpisodeMediaContainer(mockMediaContainer)
    episodeMediaContainer.createVideos()

    val videoContentInfo = episodeMediaContainer.videoList!![0] as EpisodePosterInfo
    assertThat(videoContentInfo.getBackgroundURL()).isEqualTo("http://localhost:8096/library/art/show/1")
  }

  @Test
  fun `backgroundUrl uses fallback when all image options are null`() {
    val mockMediaContainer = mockk<IMediaContainer>(relaxed = true)
    val mockVideo = mockk<IVideo>(relaxed = true)

    every { mockMediaContainer.videos } returns listOf(mockVideo)
    every { mockMediaContainer.art } returns null
    every { mockMediaContainer.parentPosterURL } returns "/library/metadata/show/1"
    every { mockVideo.backgroundImageKey } returns null

    episodeMediaContainer = EpisodeMediaContainer(mockMediaContainer)
    episodeMediaContainer.createVideos()

    val videoContentInfo = episodeMediaContainer.videoList!![0] as EpisodePosterInfo
    assertThat(videoContentInfo.getBackgroundURL()).isEqualTo("http://localhost:8096/:/resources/show-fanart.jpg")
  }

  @Test
  fun `direct play sorting picks supported media`() {
    val mockMediaContainer = mockk<IMediaContainer>(relaxed = true)
    val mockVideo = mockk<IVideo>(relaxed = true)
    val supportedMedia = mockk<IMedia>(relaxed = true) {
      every { audioCodec } returns "ac3"
      every { videoCodec } returns "h264"
    }
    val unsupportedMedia = mockk<IMedia>(relaxed = true) {
      every { audioCodec } returns "flac"
      every { videoCodec } returns "hevc"
    }

    every { mockMediaContainer.videos } returns listOf(mockVideo)
    every { mockVideo.medias } returns listOf(unsupportedMedia, supportedMedia)

    every { MediaCodecInfoUtil.isCodecSupported("video/avc") } returns true
    every { MediaCodecInfoUtil.isCodecSupported("audio/ac4") } returns true
    every { MediaCodecInfoUtil.isCodecSupported("video/hevc") } returns false
    every { MediaCodecInfoUtil.isCodecSupported("audio/flac") } returns false

    every { mockAndroidHelper.isAudioPassthroughSupported("ac3") } returns true
    every { mockAndroidHelper.isAudioPassthroughSupported("flac") } returns false
    every { MediaCodecInfoUtil.findCorrectAudioMimeType("audio/ac3") } returns "audio/ac4"
    every { MediaCodecInfoUtil.findCorrectAudioMimeType("audio/flac") } returns "audio/flac"
    every { MediaCodecInfoUtil.findCorrectVideoMimeType("video/h264") } returns "video/avc"
    every { MediaCodecInfoUtil.findCorrectVideoMimeType("video/hevc") } returns "video/hevc"


    episodeMediaContainer = EpisodeMediaContainer(mockMediaContainer)
    val videos = episodeMediaContainer.createVideos()

    assertThat(videos).isNotEmpty()
    val video = videos.first()
    assertThat(video.videoCodec).isEqualTo("h264")
    assertThat(video.audioCodec).isEqualTo("ac3")
  }

  inner class TestModule : Module() {
    init {
      bind(SerenityClient::class.java).toInstance(mockSerenityClient)
      bind(Resources::class.java).toInstance(mockResources)
      bind(AndroidHelper::class.java).toInstance(mockAndroidHelper)
    }
  }
}
