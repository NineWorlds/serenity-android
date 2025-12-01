package us.nineworlds.serenity.core.model.impl

import android.content.res.Resources
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.After
import org.junit.AfterClass
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import toothpick.config.Module
import us.nineworlds.serenity.R
import us.nineworlds.serenity.common.rest.SerenityClient
import us.nineworlds.serenity.core.services.UnWatchVideoAsyncTask
import us.nineworlds.serenity.core.services.WatchedVideoAsyncTask
import us.nineworlds.serenity.core.util.AndroidHelper
import us.nineworlds.serenity.test.InjectingTest

class AbstractVideoContentInfoTest : InjectingTest() {

    private lateinit var video: TestVideoContentInfo
    private val mockResources: Resources = mockk(relaxed = true)
    private val mockSerenityClient: SerenityClient = mockk(relaxed = true)
    private val mockAndroidHelper: AndroidHelper = mockk(relaxed = true)

    companion object {
        @JvmStatic
        @BeforeClass
        fun setUpBeforeClass() {
            mockkConstructor(WatchedVideoAsyncTask::class)
            mockkConstructor(UnWatchVideoAsyncTask::class)
        }

        @JvmStatic
        @AfterClass
        fun tearDownAfterClass() {
            unmockkAll()
        }
    }

    override fun installTestModules() {
        scope.installTestModules(TestModule())
    }

    @Before
    override fun setUp() {
        super.setUp()
        video = TestVideoContentInfo(mockResources)
        every { anyConstructed<WatchedVideoAsyncTask>().execute(any<String>()) } returns mockk()
        every { anyConstructed<UnWatchVideoAsyncTask>().execute(any<String>()) } returns mockk()
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `longTitle returns title when seriesTitle is null`() {
        video.setTitle("Test Title")
        assertThat(video.longTitle).isEqualTo("Test Title")
    }

    @Test
    fun `longTitle returns formatted title when seriesTitle is not null`() {
        every {
            mockResources.getString(
                R.string.long_title,
                any(),
                any(),
                any(),
                any()
            )
        } returns "Formatted Title"
        video.setTitle("Episode Title")
        video.seriesTitle = "Series Title"
        video.seasonNumber = 1
        video.episodeNumber = 2

        assertThat(video.longTitle).isEqualTo("Formatted Title")
    }

    @Test
    fun `isPartiallyWatched is true when resumeOffset is greater than 0 and percent is not fully watched`() {
        video.resumeOffset = 100
        video.duration = 1000
        assertThat(video.isPartiallyWatched).isTrue()
    }

    @Test
    fun `isWatched is true when percentage is over threshold`() {
        video.resumeOffset = 951
        video.duration = 1000
        assertThat(video.isWatched).isTrue()
    }

    @Test
    fun `isWatched is true when resumeOffset is 0 and viewCount is greater than 0`() {
        video.resumeOffset = 0
        video.viewCount = 1
        assertThat(video.isWatched).isTrue()
    }

    @Test
    fun `isUnwatched is true when viewCount is 0`() {
        video.viewCount = 0
        assertThat(video.isUnwatched).isTrue()
    }

    @Test
    fun `viewedPercentage calculates correctly`() {
        video.resumeOffset = 500
        video.duration = 1000
        assertThat(video.viewedPercentage()).isEqualTo(0.5f)
    }

    @Test
    fun `viewedPercentage returns 0 when duration is 0`() {
        video.resumeOffset = 100
        video.duration = 0
        assertThat(video.viewedPercentage()).isEqualTo(0f)
    }

    @Test
    fun `toggleWatchStatus calls WatchedVideoAsyncTask when partially watched`() {
        video.setId("id-123")
        video.resumeOffset = 100
        video.duration = 1000

        video.toggleWatchStatus()

        verify { anyConstructed<WatchedVideoAsyncTask>().execute("id-123") }
        assertThat(video.viewCount).isEqualTo(1)
    }

    @Test
    fun `toggleWatchStatus calls WatchedVideoAsyncTask when unwatched`() {
        video.setId("id-123")
        video.viewCount = 0

        video.toggleWatchStatus()

        verify { anyConstructed<WatchedVideoAsyncTask>().execute("id-123") }
        assertThat(video.viewCount).isEqualTo(1)
    }

    @Test
    fun `toggleWatchStatus calls UnWatchVideoAsyncTask when watched`() {
        video.setId("id-123")
        video.viewCount = 1
        video.resumeOffset = 0

        video.toggleWatchStatus()

        verify { anyConstructed<UnWatchVideoAsyncTask>().execute("id-123") }
        assertThat(video.viewCount).isEqualTo(0)
    }

    private class TestVideoContentInfo(resources: Resources?) : AbstractVideoContentInfo(resources)

    inner class TestModule : Module() {
        init {
            bind(SerenityClient::class.java).toInstance(mockSerenityClient)
            bind(Resources::class.java).toInstance(mockResources)
            bind(AndroidHelper::class.java).toInstance(mockAndroidHelper)
        }
    }
}
