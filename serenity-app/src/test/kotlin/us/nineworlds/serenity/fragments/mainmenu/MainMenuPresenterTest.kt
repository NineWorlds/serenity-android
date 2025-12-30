package us.nineworlds.serenity.fragments.mainmenu

import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import toothpick.Toothpick
import toothpick.config.Module
import us.nineworlds.serenity.common.repository.Result
import us.nineworlds.serenity.core.model.CategoryInfo
import us.nineworlds.serenity.core.model.CategoryVideoInfo
import us.nineworlds.serenity.core.model.VideoContentInfo
import us.nineworlds.serenity.core.repository.CategoryRepository
import us.nineworlds.serenity.emby.model.Video
import us.nineworlds.serenity.events.MainMenuEvent
import us.nineworlds.serenity.test.InjectingTest

@OptIn(ExperimentalCoroutinesApi::class)
class MainMenuPresenterTest : InjectingTest() {

    private val mockRepository: CategoryRepository = mockk()
    private val mockView: MainMenuView = mockk(relaxed = true)

    private lateinit var presenter: MainMenuPresenter

    @Before
    override fun setUp() {
        super.setUp()
        Dispatchers.setMain(Dispatchers.Unconfined)

        presenter = MainMenuPresenter()
        presenter.attachView(mockView)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        clearAllMocks()
        Toothpick.reset()
        presenter.detachView(mockView)
    }

    @Test
    fun `retrieveMainMenu when successful loads menu`() = runTest {
        val mainMenuEvent = MainMenuEvent(mockk())
        coEvery { mockRepository.loadMainMenu() } returns Result.Success(mainMenuEvent)

        presenter.retrieveMainMenu()

        coVerify { mockView.loadMenu(mainMenuEvent) }
    }

    @Test
    fun `populateMovieCategories shows and hides loading`() = runTest {
        coEvery { mockRepository.retrieveCategories(any()) } returns Result.Success(emptyList())

        presenter.populateMovieCategories("1", "movies")

        verify { mockView.showLoading() }
        verify { mockView.hideLoading() }
    }

    @Test
    fun `populateMovieCategories clears categories for non-movie or tvshow types`() = runTest {
        coEvery { mockRepository.retrieveCategories(any()) } returns Result.Success(emptyList())

        presenter.populateMovieCategories("1", "other")

        verify { mockView.clearCategories() }
    }

    @Test
    fun `populateMovieCategories loads and updates categories for movies`() = runTest {
        val category = mockk<CategoryInfo>()
        every { category.category } returns "action"

        val categories = listOf(category)
        val video = mockk<VideoContentInfo>()
        coEvery { mockRepository.retrieveCategories("1") } returns Result.Success(categories)
        coEvery { mockRepository.fetchItemsByCategory("action", "1", "movies") } returns Result.Success(listOf(video))

        presenter.populateMovieCategories("1", "movies")

        val expectedCategoryVideoInfo = CategoryVideoInfo(categories = listOf(category))

        verify { mockView.loadCategories(expectedCategoryVideoInfo) }
        coVerify { mockView.updateCategories(eq(category), any()) }
    }

    override fun installTestModules() {
        scope.installTestModules(TestModule())
    }

    inner class TestModule : Module() {
        init {
            bind(CategoryRepository::class.java).toInstance(mockRepository)
        }
    }
}
