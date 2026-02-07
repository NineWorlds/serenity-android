package us.nineworlds.serenity.core.repository

import android.content.res.Resources
import assertk.assertThat
import assertk.assertions.isInstanceOf
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import toothpick.config.Module
import us.nineworlds.serenity.common.media.model.IMediaContainer
import us.nineworlds.serenity.common.repository.Result
import us.nineworlds.serenity.common.rest.SerenityClient
import us.nineworlds.serenity.common.rest.Types
import us.nineworlds.serenity.test.InjectingTest

@OptIn(ExperimentalCoroutinesApi::class)
class CategoryRepositoryTest : InjectingTest() {

    private val mockClient = mockk<SerenityClient>(relaxed = true)
    private val mockMediaContainer = mockk<IMediaContainer>(relaxed = true)

    private lateinit var repository: CategoryRepository

    @Before
    override fun setUp() {
        super.setUp()
        Dispatchers.setMain(UnconfinedTestDispatcher())
        repository = CategoryRepository(mockClient)
    }

    @After
    fun tearDown() {
        clearAllMocks()
        Dispatchers.resetMain()
    }

    override fun installTestModules() {
        scope.installModules(TestModule())
    }

    inner class TestModule : Module() {
        init {
            bind(SerenityClient::class.java).toInstance(mockClient)
            bind(Resources::class.java).toInstance(mockk(relaxed = true))
        }
    }

    @Test
    fun `fetchItemsByCategory calls client with paging parameters`() = runTest {
        val categoryId = "cat1"
        val itemId = "item1"
        val type = "movies"
        val startIndex = 0
        val limit = 30

        coEvery { mockClient.retrieveItemByIdCategory(itemId, categoryId, Types.MOVIES, startIndex, limit) } returns mockMediaContainer

        val result = repository.fetchItemsByCategory(categoryId, itemId, type, startIndex, limit)

        assertThat(result).isInstanceOf(Result.Success::class)
        coVerify { mockClient.retrieveItemByIdCategory(itemId, categoryId, Types.MOVIES, startIndex, limit) }
    }

    @Test
    fun `fetchItemsByCategory maps tvshows correctly`() = runTest {
        val categoryId = "cat1"
        val itemId = "item1"
        val type = "tvshows"
        val startIndex = 30
        val limit = 15

        coEvery { mockClient.retrieveItemByIdCategory(itemId, categoryId, Types.SERIES, startIndex, limit) } returns mockMediaContainer

        val result = repository.fetchItemsByCategory(categoryId, itemId, type, startIndex, limit)

        assertThat(result).isInstanceOf(Result.Success::class)
        coVerify { mockClient.retrieveItemByIdCategory(itemId, categoryId, Types.SERIES, startIndex, limit) }
    }
}
