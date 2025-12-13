package us.nineworlds.serenity.ui.activity.login

import android.content.Intent
import android.view.View.VISIBLE
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import assertk.assertions.isInstanceOf
import com.google.android.flexbox.FlexboxLayoutManager
import io.mockk.clearAllMocks
import io.mockk.mockk
import io.mockk.verify
import org.assertj.android.api.Assertions
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf
import toothpick.config.Module
import us.nineworlds.serenity.AndroidTV
import us.nineworlds.serenity.MockkTestingModule
import us.nineworlds.serenity.common.Server
import us.nineworlds.serenity.common.rest.SerenityUser
import us.nineworlds.serenity.test.InjectingTest

@RunWith(RobolectricTestRunner::class)
class LoginUserActivityTest : InjectingTest() {

  private companion object {
    private val mockPresenter = mockk<LoginUserPresenter>(relaxed = true)
    private val mockServer = mockk<Server>(relaxed = true)
    private val mockSerenityUser = mockk<SerenityUser>(relaxed = true)
    private val mockAdapter = mockk<LoginUserAdapter>(relaxed = true)
  }

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
  fun expectedLoginPresenterIsCreated() {
    assertThat(activity.presenter).isEqualTo(mockPresenter)
  }

  @Test
  fun profileContainerHasContainerSetup() {
    assertThat(activity.binding.loginUserContainer).isNotNull()
    assertThat(activity.binding.loginUserContainer.layoutManager).isNotNull().isInstanceOf(FlexboxLayoutManager::class)

    verify {
      mockPresenter.initPresenter(mockServer)
      mockPresenter.retrieveAllUsers()
    }

  }

  @Test
  fun displayUserHidesProgressBar() {
    activity.progressBinding.dataLoadingContainer.visibility = VISIBLE
    activity.adapter = mockAdapter
    activity.displayUsers(mutableListOf(mockSerenityUser).toList())

    Assertions.assertThat(activity.progressBinding.dataLoadingContainer).isGone

    verify {
      mockAdapter.loadUsers(any())
    }
  }

  @Test
  fun launchNextScreenStartsExpectedActivity() {
    activity.launchNextScreen()

    val shadowActivity = shadowOf(activity)
    Assertions.assertThat(shadowActivity.nextStartedActivity).hasComponent(activity, AndroidTV::class.java)
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