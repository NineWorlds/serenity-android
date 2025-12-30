package us.nineworlds.serenity.ui.activity.login

import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import org.apache.commons.lang3.RandomStringUtils
import org.assertj.android.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import toothpick.config.Module
import us.nineworlds.serenity.MockkTestingModule
import us.nineworlds.serenity.R
import us.nineworlds.serenity.common.rest.SerenityClient
import us.nineworlds.serenity.common.rest.SerenityUser
import us.nineworlds.serenity.test.InjectingTest

@RunWith(RobolectricTestRunner::class)
class LoginUserViewHolderTest : InjectingTest() {

    private companion object {
        private val mockUser = mockk<SerenityUser>(relaxed = true)
        private val mockSerenityClient = mockk<SerenityClient>(relaxed = true)
    }

    private lateinit var linearLayout: LinearLayout
    private lateinit var view: View
    private lateinit var viewHolder: LoginUserViewHolder

    @Before
    override fun setUp() {
        clearAllMocks()
        super.setUp()
        val context = ContextThemeWrapper(getApplicationContext(), R.style.AppTheme)
        linearLayout = LinearLayout(context)
        view = LayoutInflater.from(context).inflate(R.layout.item_user_profile, linearLayout, false)
        viewHolder = LoginUserViewHolder(view)
    }

    @Test
    fun loadUserSetsUserName() {
        val expectedUser = RandomStringUtils.randomAlphabetic(10)
        every { mockUser.userName } returns expectedUser

        viewHolder.loadUser(mockUser)

        assertThat(viewHolder.profileName).hasText(expectedUser)
    }

    override fun installTestModules() {
        scope.installTestModules(MockkTestingModule(), TestModule())
    }

    inner class TestModule : Module() {

        init {
            bind(SerenityClient::class.java).toInstance(mockSerenityClient)
        }
    }
}
