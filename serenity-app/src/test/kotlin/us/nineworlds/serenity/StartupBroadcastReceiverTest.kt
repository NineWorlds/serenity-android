package us.nineworlds.serenity

import android.content.Intent
import android.content.SharedPreferences
import androidx.test.core.app.ApplicationProvider
import assertk.assertThat
import assertk.assertions.isNotNull
import assertk.assertions.isNull
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.apache.commons.lang3.RandomStringUtils
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.shadows.ShadowApplication
import toothpick.config.Module
import us.nineworlds.serenity.core.util.AndroidHelper
import us.nineworlds.serenity.test.InjectingTest

@RunWith(RobolectricTestRunner::class)
class StartupBroadcastReceiverTest : InjectingTest() {

  private val mockSharedPrefences: SharedPreferences = mockk(relaxed = true)
  private val mockAndroidHelper: AndroidHelper = mockk(relaxed = true)

  private lateinit var receiver: StartupBroadcastReceiver

  @Before
  override fun setUp() {
    super.setUp()
    receiver = StartupBroadcastReceiver()
  }

  @After
  fun tearDown() {
    clearAllMocks()
  }

  @Test
  fun `onReceive does nothing when intent action is null`() {
    val intent = Intent()

    receiver.onReceive(ApplicationProvider.getApplicationContext(), intent)

    val nextStartedActivity = ShadowApplication.getInstance().nextStartedActivity
    assertThat(nextStartedActivity).isNull()
  }

  @Test
  fun `onReceive does nothing when intent action is not boot completed`() {
    val intent = Intent()
    val action = RandomStringUtils.randomAlphanumeric(10)
    intent.action = action

    receiver.onReceive(ApplicationProvider.getApplicationContext(), intent)

    val nextStartedActivity = ShadowApplication.getInstance().nextStartedActivity
    assertThat(nextStartedActivity).isNull()
  }

  @Test
  fun `onReceive handles boot completed`() {
    val intent = Intent("android.intent.action.BOOT_COMPLETED")

    every { mockSharedPrefences.getBoolean("serenity_boot_startup", false) } returns true

    receiver.onReceive(ApplicationProvider.getApplicationContext(), intent)

    val nextStartedActivity = ShadowApplication.getInstance().peekNextStartedActivity()
    assertThat(nextStartedActivity).isNotNull()

    verify { mockSharedPrefences.getBoolean("serenity_boot_startup", false) }
  }

  @Test
  fun `onReceive handles leanback support`() {
    val intent = Intent("android.intent.action.BOOT_COMPLETED")

    every { mockSharedPrefences.getBoolean("serenity_boot_startup", false) } returns true
    every { mockSharedPrefences.getBoolean("androidtv_recommendation_ondeck", false) } returns true
    every { mockAndroidHelper.isLeanbackSupported } returns true

    receiver.onReceive(ApplicationProvider.getApplicationContext(), intent)

    val nextStartedActivity = ShadowApplication.getInstance().peekNextStartedActivity()
    assertThat(nextStartedActivity).isNotNull()

    verify { mockSharedPrefences.getBoolean("serenity_boot_startup", false) }
    verify { mockSharedPrefences.getBoolean("androidtv_recommendation_ondeck", false) }
    verify { mockAndroidHelper.isLeanbackSupported }
  }

  override fun installTestModules() {
    scope.installTestModules(MockkTestingModule(), TestModule())
  }

  inner class TestModule : Module() {

    init {
      bind(SharedPreferences::class.java).toInstance(mockSharedPrefences)
      bind(AndroidHelper::class.java).toInstance(mockAndroidHelper)
    }
  }
}
