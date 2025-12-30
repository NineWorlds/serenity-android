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

class MovieMediaContainerTest : InjectingTest() {

    private val mockSerenityClient: SerenityClient = mockk(relaxed = true)
    private val mockAndroidHelper: AndroidHelper = mockk(relaxed = true)

    private lateinit var movieMediaContainer: MovieMediaContainer

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
    fun `createVideos populates video list with correct movie info`() {
        val mockMediaContainer = mockk<IMediaContainer>(relaxed = true)
        val mockVideo = mockk<IVideo>(relaxed = true)
        val mockMedia = mockk<IMedia>(relaxed = true)
        val mockPart = mockk<IPart>(relaxed = true)

        every { mockMediaContainer.videos } returns listOf(mockVideo)
        every { mockVideo.medias } returns listOf(mockMedia)
        every { mockMedia.videoPart } returns listOf(mockPart)

        every { mockVideo.key } returns "/library/metadata/12345"
        every { mockVideo.title } returns "Movie Title"
        every { mockVideo.summary } returns "The summary of the movie."
        every { mockVideo.thumbNailImageKey } returns "/library/metadata/12345/thumb/123"
        every { mockVideo.backgroundImageKey } returns "/library/metadata/12345/art/456"
        every { mockVideo.viewCount } returns 1
        every { mockVideo.viewOffset } returns 1234L
        every { mockVideo.duration } returns 5678L
        every { mockVideo.contentRating } returns "PG-13"
        every { mockMedia.container } returns "mkv"
        every { mockPart.key } returns "/library/parts/54321/file.mkv"
        every { mockMedia.audioCodec } returns "aac"
        every { mockMedia.videoCodec } returns "h264"
        every { mockMedia.videoResolution } returns "1080"
        every { mockMedia.aspectRatio } returns "1.78"
        every { mockMedia.audioChannels } returns "6"

        every { MediaCodecInfoUtil.isCodecSupported(any()) } returns true
        every { mockAndroidHelper.isAudioPassthroughSupported(any()) } returns false

        movieMediaContainer = MovieMediaContainer(mockMediaContainer)
        val videos = movieMediaContainer.createVideos()

        assertThat(videos).isNotNull().hasSize(1)

        val videoContentInfo = videos[0] as MoviePosterInfo
        assertThat(videoContentInfo.id()).isEqualTo("/library/metadata/12345")
        assertThat(videoContentInfo.getTitle()).isEqualTo("Movie Title")
        assertThat(videoContentInfo.getSummary()).isEqualTo("The summary of the movie.")
        assertThat(videoContentInfo.getImageURL()).isEqualTo("http://localhost:8096/library/metadata/12345/thumb/123")
        assertThat(videoContentInfo.getBackgroundURL()).isEqualTo("http://localhost:8096/library/metadata/12345/art/456")
        assertThat(videoContentInfo.viewCount).isEqualTo(1)
        assertThat(videoContentInfo.resumeOffset).isEqualTo(1234)
        assertThat(videoContentInfo.duration).isEqualTo(5678)
        assertThat(videoContentInfo.contentRating).isEqualTo("PG-13")
        assertThat(videoContentInfo.container).isEqualTo("mkv")
        assertThat(videoContentInfo.directPlayUrl).isEqualTo("http://localhost:8096/library/parts/54321/file.mkv")
        assertThat(videoContentInfo.audioCodec).isEqualTo("aac")
        assertThat(videoContentInfo.videoCodec).isEqualTo("h264")
        assertThat(videoContentInfo.videoResolution).isEqualTo("1080")
        assertThat(videoContentInfo.aspectRatio).isEqualTo("1.78")
        assertThat(videoContentInfo.audioChannels).isEqualTo("6")
    }

    @Test
    fun `createVideos handles null videos list gracefully`() {
        val mockMediaContainer = mockk<IMediaContainer>(relaxed = true)
        every { mockMediaContainer.videos } returns null

        movieMediaContainer = MovieMediaContainer(mockMediaContainer)
        val videos = movieMediaContainer.createVideos()

        assertThat(videos).isNotNull().isEmpty()
    }

    @Test
    fun `backgroundUrl uses fallback when backgroundImageKey is null`() {
        val mockMediaContainer = mockk<IMediaContainer>(relaxed = true)
        val mockVideo = mockk<IVideo>(relaxed = true)

        every { mockMediaContainer.videos } returns listOf(mockVideo)
        every { mockVideo.backgroundImageKey } returns null

        movieMediaContainer = MovieMediaContainer(mockMediaContainer)
        val videos = movieMediaContainer.createVideos()

        val videoContentInfo = videos[0] as MoviePosterInfo
        assertThat(videoContentInfo.getBackgroundURL()).isEqualTo("http://localhost:8096/:/resources/movie-fanart.jpg")
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

        movieMediaContainer = MovieMediaContainer(mockMediaContainer)
        val videos = movieMediaContainer.createVideos()

        assertThat(videos).isNotEmpty()
        val video = videos.first()
        assertThat(video.videoCodec).isEqualTo("h264")
        assertThat(video.audioCodec).isEqualTo("ac3")
    }

    inner class TestModule : Module() {
        init {
            bind(SerenityClient::class.java).toInstance(mockSerenityClient)
            bind(Resources::class.java).toInstance(mockk())
            bind(AndroidHelper::class.java).toInstance(mockAndroidHelper)
        }
    }
}
