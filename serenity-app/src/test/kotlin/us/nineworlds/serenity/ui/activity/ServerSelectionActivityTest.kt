package us.nineworlds.serenity.ui.activity

import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.widget.TextView
import androidx.test.core.app.ApplicationProvider
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import org.assertj.android.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf
import toothpick.config.Module
import us.nineworlds.serenity.MockkTestingModule
import us.nineworlds.serenity.R
import us.nineworlds.serenity.common.Server
import us.nineworlds.serenity.core.util.StringPreference
import us.nineworlds.serenity.injection.ForMediaServers
import us.nineworlds.serenity.injection.ServerClientPreference
import us.nineworlds.serenity.injection.ServerIPPreference
import us.nineworlds.serenity.injection.ServerPortPreference
import us.nineworlds.serenity.injection.modules.AndroidModule
import us.nineworlds.serenity.injection.modules.providers.ServerClientPreferenceProvider
import us.nineworlds.serenity.test.InjectingTest
import us.nineworlds.serenity.ui.activity.login.LoginUserActivity
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

@RunWith(RobolectricTestRunner::class)
class ServerSelectionActivityTest : InjectingTest() {
  companion object {
    private val mockServer = mockk<Server>(relaxed = true)
  }

  private lateinit var activity: ServerSelectionActivity

  @Before
  override fun setUp() {
    clearAllMocks()
    super.setUp()
    activity = Robolectric.buildActivity(ServerSelectionActivity::class.java).create().visible().get()
  }

  @After
  fun tearDown() {
    activity.finish()
  }

  @Test
  fun serverDelayHandlerIsInitialized() {
    assertThat(activity.serverDisplayHandler).isNotNull
  }

  @Test
  fun createServerListHidesProgressBarAndDisplaysServerContainer() {
    Robolectric.flushForegroundThreadScheduler()

    Assertions.assertThat(activity.progressBinding.dataLoadingContainer).isGone
    Assertions.assertThat(activity.binding.serverContainer).isVisible
  }

  @Test
  fun displayHandlerPopulatesServerContainer() {
    val expectedId = UUID.randomUUID().toString()
    activity.servers[expectedId] = mockServer

    every { mockServer.discoveryProtocol() } returns "Emby"
    every { mockServer.serverName } returns "test"

    Robolectric.flushForegroundThreadScheduler()

    Assertions.assertThat(activity.binding.serverContainer).hasChildCount(3)
  }

  @Test
  fun refreshButtonIsOnlyItemWhenServerListIsEmpty() {
    Robolectric.flushForegroundThreadScheduler()

    assertThat(activity.servers).isEmpty()
    Assertions.assertThat(activity.binding.serverContainer).hasChildCount(2);
  }

  @Test
  fun serverInfoAddedToServerContainerSetsExpectedTextOnServerTextView() {
    val expectedId = UUID.randomUUID().toString()
    activity.servers[expectedId] = mockServer

    every { mockServer.discoveryProtocol() } returns "Emby"
    every { mockServer.serverName } returns "test"

    Robolectric.flushForegroundThreadScheduler()

    val view = activity.binding.serverContainer.getChildAt(1)
    val textView = view.findViewById<TextView>(R.id.server_name)

    Assertions.assertThat(textView).hasText("test")
  }

  @Test
  fun clickingOnServerOptionContainerStartsExpectedActivity() {
    val expectedId = UUID.randomUUID().toString()
    activity.servers[expectedId] = mockServer

    every { mockServer.discoveryProtocol() } returns "Emby"
    every { mockServer.serverName } returns "test"
    every { mockServer.ipAddress } returns "testserver"

    Robolectric.flushForegroundThreadScheduler()

    val view = activity.binding.serverContainer.getChildAt(1)
    view.performClick()

    val shadowActivity = shadowOf(activity)
    Assertions.assertThat(shadowActivity.nextStartedActivity).hasComponent(activity, LoginUserActivity::class.java)
  }

  override fun installTestModules() {
    scope.installTestModules(MockkTestingModule(), TestModule())
  }

  inner class TestModule : Module() {
    init {
      bind(MutableMap::class.java).withName(ForMediaServers::class.java).toInstance(ConcurrentHashMap<String, Server>())
      bind(StringPreference::class.java).withName(ServerIPPreference::class.java)
        .toProvider(AndroidModule.ServerIPPreferenceProvider::class.java)
      bind(StringPreference::class.java).withName(ServerPortPreference::class.java)
        .toProvider(AndroidModule.ServerPorPreferenceProvider::class.java)
      bind(StringPreference::class.java).withName(ServerClientPreference::class.java).toProvider(
        ServerClientPreferenceProvider::class.java)
      bind(SharedPreferences::class.java).toInstance(PreferenceManager.getDefaultSharedPreferences(ApplicationProvider.getApplicationContext()))
    }
  }
}