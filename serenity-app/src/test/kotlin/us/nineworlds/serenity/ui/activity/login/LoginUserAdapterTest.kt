package us.nineworlds.serenity.ui.activity.login

import android.app.Application
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.view.ContextThemeWrapper
import androidx.test.core.app.ApplicationProvider
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import assertk.assertions.isNotNull
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.atMost
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnit
import org.mockito.quality.Strictness
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf
import us.nineworlds.serenity.R
import us.nineworlds.serenity.TestingModule
import us.nineworlds.serenity.common.rest.SerenityUser
import us.nineworlds.serenity.test.InjectingTest

@RunWith(RobolectricTestRunner::class)
class LoginUserAdapterTest : InjectingTest() {

  @get:Rule
  val rule = MockitoJUnit.rule().strictness(Strictness.STRICT_STUBS)

  private val mockPresenter = mock<LoginUserPresenter>()
  private val mockUser = mock<SerenityUser>()
  private val mockViewHolder = mock<LoginUserViewHolder>()

  private lateinit var adapter: LoginUserAdapter

  @Before
  override fun setUp() {
    super.setUp()
    adapter = LoginUserAdapter(mockPresenter)
  }

  @Test
  fun onCreateViewHolderCreatesExpectedInstance() {
    val context = ContextThemeWrapper(
      ApplicationProvider.getApplicationContext<Application>(),
      R.style.AppTheme
    )
    val result = adapter.onCreateViewHolder(FrameLayout(context), 0)
    assertThat(result).isNotNull().isInstanceOf(LoginUserViewHolder::class)
  }

  @Test
  fun loadUsersSetsInitializesUserList() {
    adapter.loadUsers(mutableListOf(mockUser))
    assertThat(adapter.itemCount).isEqualTo(1)
  }

  @Test
  fun onBindViewHolderLoadsTheUserIntoTheView() {
    val view = FrameLayout(ApplicationProvider.getApplicationContext<Application>())

    doReturn(view).whenever(mockViewHolder).getItemView()

    adapter.loadUsers(mutableListOf(mockUser))

    adapter.onBindViewHolder(mockViewHolder, 0)

    verify(mockViewHolder).loadUser(mockUser)

    val shadowView = shadowOf(view)
    assertThat(shadowView.onClickListener).isNotNull()
    assertThat(view.onFocusChangeListener).isNotNull()
  }

  @Test
  fun onClickedLoadsSelectedUser() {
    adapter.loadUsers(mutableListOf(mockUser))
    adapter.onClicked(0)

    verify(mockPresenter).loadUser(mockUser)
  }

  @Test
  fun onFocusedChangeListenerUpdatesViewSelectionStateWithOutFocus() {
    val mockView = mock<View>()
    adapter.loadUsers(mutableListOf(mockUser))

    adapter.onFocusChanged(mockView, false)

    verify(mockView).clearAnimation()
    verify(mockView).background = null
  }

  @Test
  fun onFocusedChangeListenerUpdatesViewSelectionStateWithFocus() {
    val mockView = mock<View>()
    val context = ContextThemeWrapper(
      ApplicationProvider.getApplicationContext<Application>(),
      R.style.AppTheme
    )

    doReturn(context).whenever(mockView).context

    adapter.loadUsers(mutableListOf(mockUser))
    adapter.onFocusChanged(mockView, true)

    verify(mockView, atMost(2)).clearAnimation()
    verify(mockView).background = null
    verify(mockView).context
    verify(mockView).background = any<Drawable>()
  }

  override fun installTestModules() {
    scope.installTestModules(TestingModule())
  }
}