package us.nineworlds.serenity.core.model.impl

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isNotNull
import assertk.assertions.isTrue
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.mockkObject
import io.mockk.runs
import io.mockk.unmockkAll
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.AfterClass
import org.junit.Before
import org.junit.Test
import toothpick.config.Module
import us.nineworlds.serenity.common.rest.SerenityClient
import us.nineworlds.serenity.common.rest.Types
import us.nineworlds.serenity.core.services.UnWatchVideoJob
import us.nineworlds.serenity.core.services.WatchedVideoJob
import us.nineworlds.serenity.test.InjectingTest

class AbstractSeriesContentInfoTest : InjectingTest() {

    private lateinit var series: TestSeriesContentInfo
    private val mockSerenityClient: SerenityClient = mockk(relaxed = true)

    override fun installTestModules() {
        scope.installTestModules(TestModule())
    }

    companion object {

        @AfterClass
        @JvmStatic
        fun restMocks() = unmockkAll()
    }

    @Before
    override fun setUp() {
        super.setUp()
        series = TestSeriesContentInfo()

        mockkObject(WatchedVideoJob, UnWatchVideoJob)
        every { WatchedVideoJob.updateWatchedStatus(any<String>()) } just runs
        every { UnWatchVideoJob.markUnwatched(any<String>()) } just runs
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `sets and gets properties`() {
        series.setType(Types.SERIES)
        assertThat(series.getType()).isEqualTo(Types.SERIES)

        series.setId("id")
        assertThat(series.id()).isEqualTo("id")

        series.setSummary("summary")
        assertThat(series.getSummary()).isEqualTo("summary")

        series.setImageURL("poster")
        assertThat(series.getImageURL()).isEqualTo("poster")

        series.setBackgroundURL("background")
        assertThat(series.getBackgroundURL()).isEqualTo("background")

        series.setTitle("title")
        assertThat(series.getTitle()).isEqualTo("title")

        series.setMediaTagIdentifier("mediaTag")
        assertThat(series.getMediaTagIdentifier()).isEqualTo("mediaTag")

        series.key = "key"
        assertThat(series.key).isEqualTo("key")

        series.generes = listOf("action")
        assertThat(series.generes).isNotNull()

        series.showsWatched = "1"
        assertThat(series.showsWatched).isEqualTo("1")

        series.showsUnwatched = "2"
        assertThat(series.showsUnwatched).isEqualTo("2")

        series.showMetaDataURL = "url"
        assertThat(series.showMetaDataURL).isEqualTo("url")

        series.thumbNailURL = "thumb"
        assertThat(series.thumbNailURL).isEqualTo("thumb")
    }

    @Test
    fun `isPartiallyWatched returns true when some shows are watched`() {
        series.showsWatched = "2"
        series.showsUnwatched = "8"
        assertThat(series.isPartiallyWatched).isTrue()
    }

    @Test
    fun `isPartiallyWatched returns false when all shows are watched`() {
        series.showsWatched = "10"
        series.showsUnwatched = "0"
        assertThat(series.isPartiallyWatched).isFalse()
    }

    @Test
    fun `isPartiallyWatched returns false when no shows are watched`() {
        series.showsWatched = "0"
        series.showsUnwatched = "10"
        assertThat(series.isPartiallyWatched).isFalse()
    }

    @Test
    fun `isUnwatched returns true when unwatched is greater than 0`() {
        series.showsUnwatched = "1"
        assertThat(series.isUnwatched).isTrue()
    }

    @Test
    fun `isUnwatched returns false when unwatched is 0`() {
        series.showsUnwatched = "0"
        assertThat(series.isUnwatched).isFalse()
    }

    @Test
    fun `isWatched returns true when watched count equals total shows`() {
        series.showsWatched = "10"
        series.showsUnwatched = "0"
        assertThat(series.isWatched).isTrue()
    }

    @Test
    fun `isWatched returns false when watched count not equals total shows`() {
        series.showsWatched = "9"
        series.showsUnwatched = "1"
        assertThat(series.isWatched).isFalse()
    }

    @Test
    fun `totalShows returns sum of watched and unwatched`() {
        series.showsWatched = "3"
        series.showsUnwatched = "7"
        assertThat(series.totalShows()).isEqualTo(10)
    }

    @Test
    fun `viewedPercentage calculates correctly`() {
        series.showsWatched = "5"
        series.showsUnwatched = "5"
        assertThat(series.viewedPercentage()).isEqualTo(0.5f)
    }

    @Test
    fun `viewedPercentage is zero when total shows is zero`() {
        series.showsWatched = "0"
        series.showsUnwatched = "0"
        assertThat(series.viewedPercentage()).isEqualTo(0f)
    }

    @Test
    fun `toggleWatchedStatus calls WatchedVideoAsyncTask when partially watched`() {
        series.setId("id-123")
        series.showsWatched = "2"
        series.showsUnwatched = "8"

        series.toggleWatchedStatus()

        verify { WatchedVideoJob.updateWatchedStatus("id-123") }
        assertThat(series.showsWatched).isEqualTo("10")
        assertThat(series.showsUnwatched).isEqualTo("0")
    }

    @Test
    fun `toggleWatchedStatus calls WatchedVideoAsyncTask when unwatched`() {
        series.setId("id-123")
        series.showsWatched = "0"
        series.showsUnwatched = "10"

        series.toggleWatchedStatus()

        verify { WatchedVideoJob.updateWatchedStatus("id-123") }
        assertThat(series.showsWatched).isEqualTo("10")
        assertThat(series.showsUnwatched).isEqualTo("0")
    }

    @Test
    fun `toggleWatchedStatus calls UnWatchVideoAsyncTask when fully watched`() = runTest {
        series.setId("id-123")
        series.showsWatched = "10"
        series.showsUnwatched = "0"

        series.toggleWatchedStatus()

        verify { UnWatchVideoJob.markUnwatched("id-123") }
        assertThat(series.showsUnwatched).isEqualTo("10")
        assertThat(series.showsWatched).isEqualTo("0")
    }

    @Test
    fun `toggleWatchedStatus uses key when id is null`() {
        series.key = "key-123"
        series.setId(null)
        series.showsWatched = "2"
        series.showsUnwatched = "8"

        series.toggleWatchedStatus()

        verify { WatchedVideoJob.updateWatchedStatus("key-123") }
    }

    private class TestSeriesContentInfo : AbstractSeriesContentInfo()

    inner class TestModule : Module() {
        init {
            bind(SerenityClient::class.java).toInstance(mockSerenityClient)
        }
    }
}
