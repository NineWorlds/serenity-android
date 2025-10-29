package us.nineworlds.serenity.core.model.impl

import android.app.Application
import android.content.SharedPreferences
import android.content.res.Resources
import android.preference.PreferenceManager
import androidx.test.core.app.ApplicationProvider
import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isNotNull
import assertk.assertions.isTrue
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import toothpick.config.Module
import us.nineworlds.serenity.MockkTestingModule
import us.nineworlds.serenity.common.media.model.IDirectory
import us.nineworlds.serenity.common.media.model.IMediaContainer
import us.nineworlds.serenity.test.InjectingTest

@RunWith(RobolectricTestRunner::class)
class MenuMediaContainerTest : InjectingTest() {

  private val mockMediaContainer: IMediaContainer = mockk()
  private val mockDirectory: IDirectory = mockk()

  private lateinit var menuMediaContainer: MenuMediaContainer
  private lateinit var mockDirectories: MutableList<IDirectory>

  @Before
  override fun setUp() {
    super.setUp()
    mockDirectories = mutableListOf()

    every { mockMediaContainer.directories } returns mockDirectories

    menuMediaContainer = MenuMediaContainer(mockMediaContainer)
    scope.inject(menuMediaContainer)
  }

  @After
  fun tearDown() {
    clearAllMocks()
  }

  @Test
  fun `create menu items does not return null`() {
    assertThat(menuMediaContainer.createMenuItems()).isNotNull()
  }

  @Test
  fun `create menu items returns one movie menu item`() {
    demandMovieMenuItem()

    mockDirectories.add(mockDirectory)

    val menuItems = menuMediaContainer.createMenuItems()
    assertThat(menuItems).hasSize(2)
  }

  @Test
  fun `create menu items always returns settings menu item`() {
    val menuItems = menuMediaContainer.createMenuItems()

    assertThat(menuItems.any { it.type == "settings" }).isTrue()
  }

  private fun demandMovieMenuItem() {
    every { mockDirectory.type } returns "movie"
    every { mockDirectory.title } returns "title"
    every { mockDirectory.key } returns "1"
  }

  override fun installTestModules() {
    scope.installTestModules(MockkTestingModule(), TestModule())
  }

  inner class TestModule : Module() {
    init {
      bind(SharedPreferences::class.java).toInstance(PreferenceManager.getDefaultSharedPreferences(ApplicationProvider.getApplicationContext<Application>()))
      bind(Resources::class.java).toInstance(ApplicationProvider.getApplicationContext<Application>().resources)
    }
  }
}
