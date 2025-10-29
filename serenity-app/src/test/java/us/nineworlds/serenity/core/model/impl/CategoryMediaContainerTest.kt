package us.nineworlds.serenity.core.model.impl

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotEmpty
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import us.nineworlds.serenity.TestingModule
import us.nineworlds.serenity.core.model.CategoryInfo
import us.nineworlds.serenity.emby.model.Directory
import us.nineworlds.serenity.emby.model.MediaContainer
import us.nineworlds.serenity.test.InjectingTest
import java.util.UUID

@RunWith(RobolectricTestRunner::class)
class CategoryMediaContainerTest : InjectingTest() {

  private val mockMediaContainer: MediaContainer = mockk(relaxed = true)

  private lateinit var directories: List<Directory>

  private lateinit var categoryMediaContainer: CategoryMediaContainer

  @Before
  override fun setUp() {
    super.setUp()

    categoryMediaContainer = CategoryMediaContainer(mockMediaContainer)

    directories = (0..2).map { i ->
      Directory().apply {
        key = UUID.randomUUID().toString()
        title = "Title No: $i"
        secondary = if (i % 2 == 0) 1 else 0
      }
    }
  }

  @After
  fun tearDown() {
    clearAllMocks()
  }

  override fun installTestModules() {
    scope.installTestModules(TestingModule())
  }

  @Test
  fun `create categories does not return an empty categories list`() {
    every { mockMediaContainer.directories } returns directories

    val result = categoryMediaContainer.createCategories()

    assertThat(result).isNotEmpty()
  }

  @Test
  fun `create categories creates a valid category`() {
    every { mockMediaContainer.directories } returns directories

    val result = categoryMediaContainer.createCategories()
    val category = result[0]

    assertThat(category.category).isEqualTo(directories[0].key)
  }

  @Test
  fun `create categories creates expected detail`() {
    every { mockMediaContainer.directories } returns directories

    val result = categoryMediaContainer.createCategories()
    val category = result[0]

    assertThat(category.categoryDetail).isEqualTo(directories[0].title)
  }

  @Test
  fun `create categories creates expected level for a single level entry`() {
    every { mockMediaContainer.directories } returns directories

    val result = categoryMediaContainer.createCategories()
    val category = result[0]

    assertThat(category.level).isEqualTo(directories[0].secondary)
  }
}
