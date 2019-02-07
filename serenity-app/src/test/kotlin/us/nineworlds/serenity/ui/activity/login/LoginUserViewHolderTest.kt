package us.nineworlds.serenity.ui.activity.login

import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.whenever
import org.apache.commons.lang3.RandomStringUtils
import org.assertj.android.api.Assertions.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import org.mockito.quality.Strictness
import org.robolectric.RobolectricTestRunner
import toothpick.config.Module
import us.nineworlds.serenity.R
import us.nineworlds.serenity.TestingModule
import us.nineworlds.serenity.common.rest.SerenityClient
import us.nineworlds.serenity.common.rest.SerenityUser
import us.nineworlds.serenity.test.InjectingTest

@RunWith(RobolectricTestRunner::class)
class LoginUserViewHolderTest : InjectingTest() {

  @Rule
  @JvmField
  var mockitoRule: MockitoRule = MockitoJUnit.rule().strictness(Strictness.LENIENT)

  @Mock
  internal lateinit var mockUser: SerenityUser

  @Mock
  internal lateinit var mockSerenityClient: SerenityClient

  private lateinit var linearLayout: LinearLayout
  private lateinit var view: View
  private lateinit var viewHolder: LoginUserViewHolder

  @Before
  override fun setUp() {
    super.setUp()
    linearLayout = LinearLayout(getApplicationContext())
    view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_user_profile, linearLayout, false)
    viewHolder = LoginUserViewHolder(view)
  }

  @Test
  fun loadUserSetsUserName() {
    val expectedUser = RandomStringUtils.randomAlphabetic(10)
    doReturn(expectedUser).whenever(mockUser).userName

    viewHolder.loadUser(mockUser)

    assertThat(viewHolder.profileName).hasText(expectedUser)
  }

  override fun installTestModules() {
    scope.installTestModules(TestingModule(), TestModule())
  }

  inner class TestModule : Module() {

    init {
      bind(SerenityClient::class.java).toInstance(mockSerenityClient)
    }
  }
}