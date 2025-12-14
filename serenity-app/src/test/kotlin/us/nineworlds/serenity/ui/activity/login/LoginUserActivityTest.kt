package us.nineworlds.serenity.ui.activity.login

import android.app.Application
import android.content.Intent
import android.os.Looper
import android.view.View.VISIBLE
import androidx.appcompat.app.AlertDialog
import androidx.test.core.app.ApplicationProvider
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import assertk.assertions.isInstanceOf
import com.google.android.flexbox.FlexboxLayoutManager
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.android.api.Assertions
import org.junit.After
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf
import org.robolectric.shadows.ShadowAlertDialog
import toothpick.config.Module
import us.nineworlds.serenity.AndroidTV
import us.nineworlds.serenity.MockkTestingModule
import us.nineworlds.serenity.common.Server
import us.nineworlds.serenity.common.rest.SerenityUser
import us.nineworlds.serenity.test.InjectingTest

@RunWith(RobolectricTestRunner::class)
class LoginUserActivityTest : InjectingTest() {

  private val mockPresenter = mockk<LoginUserPresenter>(relaxed = true)
  private val mockServer = mockk<Server>(relaxed = true)
  private val mockSerenityUser = mockk<SerenityUser>(relaxed = true)
  private val mockAdapter = mockk<LoginUserAdapter>(relaxed = true)

  private lateinit var activity: LoginUserActivity

  @Before
  override fun setUp() {
    clearAllMocks()
    super.setUp()

    val intent = Intent()
    intent.putExtra("server", mockServer)

    activity = Robolectric.buildActivity(LoginUserActivity::class.java, intent).create().get()
  }

  @After
  fun tearDown() {
    activity.finish()
  }

  @Test
  fun `presenter is injected`() {
    assertThat(activity.presenter).isEqualTo(mockPresenter)
  }

  @Test
  fun `profile container has expected layout manager`() {
    assertThat(activity.binding.loginUserContainer.layoutManager).isNotNull().isInstanceOf(FlexboxLayoutManager::class)
  }

  @Test
  fun `presenter has server and retrieves users`() {
    verify {
      mockPresenter.initPresenter(mockServer)
      mockPresenter.retrieveAllUsers()
    }
  }

  @Test
  fun `display users hides progress bar and loads users`() {
    activity.progressBinding.dataLoadingContainer.visibility = VISIBLE
    activity.adapter = mockAdapter
    activity.displayUsers(mutableListOf(mockSerenityUser).toList())

    Assertions.assertThat(activity.progressBinding.dataLoadingContainer).isGone

    verify {
      mockAdapter.loadUsers(any())
    }
  }

  @Test
  fun `launch next screen starts expected activity`() {
    activity.launchNextScreen()

    val shadowActivity = shadowOf(activity)
    Assertions.assertThat(shadowActivity.nextStartedActivity).hasComponent(activity, AndroidTV::class.java)
  }

  @Test
  fun `on user selected without password calls load user`() {
    every { mockSerenityUser.hasPassword() } returns false

    activity.onUserSelected(mockSerenityUser)

    verify { mockPresenter.loadUser(mockSerenityUser) }
  }

  @Test
  @Ignore
  fun `on user selected with stored password calls load user`() {
    val prefs = activity.getPreferences(0)
    prefs.edit().putString("emby_1_password", "password").commit()

    every { mockSerenityUser.hasPassword() } returns true
    every { mockSerenityUser.userId } returns "1"

    activity.onUserSelected(mockSerenityUser)

    verify { mockPresenter.loadUser(mockSerenityUser, "password") }
  }

  @Test
  @Ignore
  fun `on user selected with password shows dialog`() {
    every { mockSerenityUser.hasPassword() } returns true
    every { mockSerenityUser.userId } returns "1"

    activity.onUserSelected(mockSerenityUser)

    shadowOf(Looper.getMainLooper()).idle()
    val dialogId = shadowOf(activity).lastShownDialogId

    val dialog = shadowOf(activity).getDialogById(dialogId)
    assertThat(dialog).isInstanceOf(AlertDialog::class.java)
    assertThat(dialog.isShowing).isEqualTo(true)
  }


  override fun installTestModules() {
    scope.installTestModules(MockkTestingModule(), TestModule())
  }

  inner class TestModule : Module() {
    init {
      bind(LoginUserPresenter::class.java).toInstance(mockPresenter)
    }
  }
}