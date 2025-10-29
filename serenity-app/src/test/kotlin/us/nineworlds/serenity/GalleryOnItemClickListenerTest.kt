package us.nineworlds.serenity

import android.content.Intent
import android.view.View
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isTrue
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows
import us.nineworlds.serenity.core.menus.MenuItem
import us.nineworlds.serenity.ui.preferences.LeanbackSettingsActivity

@RunWith(RobolectricTestRunner::class)
class GalleryOnItemClickListenerTest {

  private val mockAdapter: MainMenuTextViewAdapter = mockk(relaxed = true)

  private lateinit var context: MainMenuTestActivity
  private lateinit var onItemClickListener: GalleryOnItemClickListener

  @Before
  fun setUp() {
    context = Robolectric.setupActivity(MainMenuTestActivity::class.java)
    onItemClickListener = GalleryOnItemClickListener(mockAdapter)
  }

  @After
  fun tearDown() {
    context.finish()
    clearAllMocks()
  }

  @Test
  fun onClickMenuSearchLaunchesExpectedActivity() {
    val view = View(context)
    val searchMenuItem = MenuItem().apply {
      type = "search"
    }
    every { mockAdapter.getItemAtPosition(any()) } returns searchMenuItem

    onItemClickListener.onItemClick(view, 0)

    assertThat(context.onSearchActivtyCalled).isTrue()
  }

  @Test
  fun onClickOpensOptionsMenu() {
    val view = View(context)
    val optionsMenuItem = MenuItem().apply {
      type = "options"
    }
    every { mockAdapter.getItemAtPosition(any()) } returns optionsMenuItem

    onItemClickListener.onItemClick(view, 0)

    assertThat(context.openOptionsMenu).isTrue()
  }

  @Test
  fun onClickUnknownRequestLaunchesPreferences() {
    val view = View(context)
    val unknownMenuItem = MenuItem().apply {
      type = "unknown"
    }
    every { mockAdapter.getItemAtPosition(any()) } returns unknownMenuItem

    onItemClickListener.onItemClick(view, 0)

    val shadowActivity = Shadows.shadowOf(context)
    val nextStartedActivity = shadowActivity.nextStartedActivity

    val expectedIntent = Intent(context, LeanbackSettingsActivity::class.java)
    assertThat(nextStartedActivity.component).isEqualTo(expectedIntent.component)
  }
}