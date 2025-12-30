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
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf
import us.nineworlds.serenity.MockkTestingModule
import us.nineworlds.serenity.R
import us.nineworlds.serenity.common.rest.SerenityUser
import us.nineworlds.serenity.test.InjectingTest

@RunWith(RobolectricTestRunner::class)
class LoginUserAdapterTest : InjectingTest() {

    private val mockUserSelectedListener = mockk<OnUserSelectedListener>(relaxed = true)
    private val mockUser = mockk<SerenityUser>(relaxed = true)
    private val mockViewHolder = mockk<LoginUserViewHolder>(relaxed = true)

    private lateinit var adapter: LoginUserAdapter

    @Before
    override fun setUp() {
        clearAllMocks()
        super.setUp()
        adapter = LoginUserAdapter(mockUserSelectedListener)
    }

    @Test
    fun `on create view holder creates expected view holder`() {
        val context = ContextThemeWrapper(
            ApplicationProvider.getApplicationContext<Application>(),
            R.style.AppTheme
        )
        val result = adapter.onCreateViewHolder(FrameLayout(context), 0)
        assertThat(result).isNotNull().isInstanceOf(LoginUserViewHolder::class)
    }

    @Test
    fun `load users initializes user list`() {
        adapter.loadUsers(mutableListOf(mockUser))
        assertThat(adapter.itemCount).isEqualTo(1)
    }

    @Test
    fun `on bind view holder loads user into view`() {
        val view = FrameLayout(ApplicationProvider.getApplicationContext<Application>())

        every { mockViewHolder.getItemView() } returns view

        adapter.loadUsers(mutableListOf(mockUser))

        adapter.onBindViewHolder(mockViewHolder, 0)

        verify { mockViewHolder.loadUser(mockUser) }

        val shadowView = shadowOf(view)
        assertThat(shadowView.onClickListener).isNotNull()
        assertThat(view.onFocusChangeListener).isNotNull()
    }

    @Test
    fun `on item click notifies listener`() {
        adapter.loadUsers(mutableListOf(mockUser))
        adapter.onClicked(0)

        verify { mockUserSelectedListener.onUserSelected(mockUser) }
    }

    @Test
    fun `on focus change without focus clears animation and background`() {
        val mockView = mockk<View>(relaxed = true)
        adapter.loadUsers(mutableListOf(mockUser))

        adapter.onFocusChanged(mockView, false)

        verify {
            mockView.clearAnimation()
            mockView.background = null
        }
    }

    @Test
    fun `on focus change with focus updates view background`() {
        val mockView = mockk<View>(relaxed = true)
        val context = ContextThemeWrapper(
            ApplicationProvider.getApplicationContext<Application>(),
            R.style.AppTheme
        )

        every { mockView.context } returns context

        adapter.loadUsers(mutableListOf(mockUser))
        adapter.onFocusChanged(mockView, true)

        verify(exactly = 2) {
            mockView.clearAnimation()
        }

        verify {
            mockView.background = any<Drawable>()
        }
    }

    override fun installTestModules() {
        scope.installTestModules(MockkTestingModule())
    }
}
