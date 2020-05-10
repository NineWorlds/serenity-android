package us.nineworlds.serenity.ui.activity.login

import android.content.Intent
import android.view.View.VISIBLE
import com.google.android.flexbox.FlexboxLayoutManager
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.verify
import org.assertj.android.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import org.mockito.quality.Strictness
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf
import toothpick.config.Module
import us.nineworlds.serenity.AndroidTV
import us.nineworlds.serenity.TestingModule
import us.nineworlds.serenity.common.Server
import us.nineworlds.serenity.common.rest.SerenityUser
import us.nineworlds.serenity.test.InjectingTest

@RunWith(RobolectricTestRunner::class)
class LoginUserActivityTest : InjectingTest() {

  @Rule
  @JvmField
  var mockitoRule: MockitoRule = MockitoJUnit.rule().strictness(Strictness.LENIENT)

  @Mock
  lateinit var mockPresenter: LoginUserPresenter
  @Mock
  lateinit var mockServer: Server
  @Mock
  lateinit var mockSerenityUser: SerenityUser
  @Mock
  lateinit var mockAdapter: LoginUserAdapter

  lateinit var activity: LoginUserActivity

  @Before
  override fun setUp() {
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
    assertThat(activity.providePresenter()).isEqualTo(mockPresenter)
  }

  @Test
  fun profileContainerHasContainerSetup() {
    assertThat(activity.profileContainer).isNotNull
    assertThat(activity.profileContainer.layoutManager).isInstanceOf(FlexboxLayoutManager::class.java)

    verify(mockPresenter).initPresenter(mockServer)
    verify(mockPresenter).retrieveAllUsers()
  }

  @Test
  fun displayUserHidesProgressBar() {
    activity.dataLoadingContainer.visibility = VISIBLE
    activity.adapter = mockAdapter
    activity.displayUsers(mutableListOf(mockSerenityUser).toList())

    Assertions.assertThat(activity.dataLoadingContainer).isGone
    verify(mockAdapter).loadUsers(any())
  }

  @Test
  fun launchNextScreenStartsExpectedActivity() {
    activity.launchNextScreen()

    val shadowActivity = shadowOf(activity)
    Assertions.assertThat(shadowActivity.nextStartedActivity).hasComponent(activity, AndroidTV::class.java)
  }

  override fun installTestModules() {
    scope.installTestModules(TestingModule(), TestModule())
  }

  inner class TestModule : Module() {
    init {
      bind(LoginUserPresenter::class.java).toInstance(mockPresenter)
    }
  }
}