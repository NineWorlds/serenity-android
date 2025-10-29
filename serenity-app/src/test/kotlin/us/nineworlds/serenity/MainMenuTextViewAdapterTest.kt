package us.nineworlds.serenity

import android.content.SharedPreferences
import android.preference.PreferenceManager
import androidx.test.core.app.ApplicationProvider
import assertk.assertThat
import assertk.assertions.isNotNull
import io.mockk.clearAllMocks
import io.mockk.mockk
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import toothpick.config.Module
import us.nineworlds.serenity.core.menus.MenuItem
import us.nineworlds.serenity.fragments.mainmenu.MainMenuPresenter
import us.nineworlds.serenity.test.InjectingTest

@RunWith(RobolectricTestRunner::class)
class MainMenuTextViewAdapterTest : InjectingTest() {

  private val mockPresenter: MainMenuPresenter = mockk(relaxed = true)
  private lateinit var adapter: MainMenuTextViewAdapter

  @Before
  override fun setUp() {
    super.setUp()
    adapter = MainMenuTextViewAdapter(mockPresenter)
  }

  @After
  fun tearDown() {
    clearAllMocks()
  }

  @Test
  fun `getItemAtPosition returns item at position zero when position is negative`() {
    MainMenuTextViewAdapter.menuItems = listOf(MenuItem())

    val result = adapter.getItemAtPosition(-1)

    assertThat(result).isNotNull()
  }

  override fun installTestModules() {
    scope.installTestModules(MockkTestingModule(), TestModule())
  }

  inner class TestModule : Module() {
    init {
      bind(SharedPreferences::class.java).toInstance(PreferenceManager.getDefaultSharedPreferences(ApplicationProvider.getApplicationContext()))
    }
  }
}
