package us.nineworlds.serenity.core

import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.preference.PreferenceManager
import androidx.test.core.app.ApplicationProvider
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import assertk.assertThat
import assertk.assertions.isEqualTo
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.LooperMode
import toothpick.config.Module
import us.nineworlds.serenity.MockkTestingModule
import us.nineworlds.serenity.test.InjectingTest

@RunWith(RobolectricTestRunner::class)
@LooperMode(LooperMode.Mode.LEGACY)
class ServerConfigTest : InjectingTest() {

  private lateinit var serverConfigChangeListener: SharedPreferences.OnSharedPreferenceChangeListener
  private lateinit var serverConfig: ServerConfig

  private val mockPrefs: SharedPreferences = mockk(relaxed = true)

  @Before
  override fun setUp() {
    super.setUp()
    Robolectric.getForegroundThreadScheduler().pause()
    Robolectric.getBackgroundThreadScheduler().pause()
    serverConfig = ServerConfig.getInstance(getApplicationContext()) as ServerConfig
    serverConfigChangeListener = serverConfig.serverConfigChangeListener
  }

  @After
  fun tearDown() {
    clearAllMocks()
  }

  @Test
  fun serverPortNewValueIsRetrieved() {
    every { mockPrefs.getString("serverport", "32400") } returns "9999"

    serverConfigChangeListener.onSharedPreferenceChanged(mockPrefs, "serverport")

    verify { mockPrefs.getString("serverport", "32400") }
  }

  @Test
  fun serverHostNewValueIsRetrieved() {
    every { mockPrefs.getString("server", "") } returns "10.0.0.3"

    serverConfigChangeListener.onSharedPreferenceChanged(mockPrefs, "server")

    verify { mockPrefs.getString("server", "") }
  }

  @Test
  fun serverSetsNewServerAddressBasedOnDiscoveredServers() {
    val reditor: Editor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit()
    every { mockPrefs.getString("discoveredServer", "") } returns "10.0.0.3"
    every { mockPrefs.edit() } returns reditor

    serverConfigChangeListener.onSharedPreferenceChanged(mockPrefs, "discoveredServer")

    verify { mockPrefs.getString("discoveredServer", "") }
  }

  @Test
  fun setHostSetsCorrectValue() {
    serverConfig.host = "10.0.0.4"
    assertThat(serverConfig.host).isEqualTo("10.0.0.4")
  }

  @Test
  fun setPortSetsExpectedValue() {
    serverConfig.port = "6666"
    assertThat(serverConfig.port).isEqualTo("6666")
  }

  override fun installTestModules() {
    scope.installTestModules(MockkTestingModule(), TestModule())
  }

  inner class TestModule : Module() {
    init {
      bind(SharedPreferences::class.java).toInstance(
        PreferenceManager.getDefaultSharedPreferences(
          ApplicationProvider.getApplicationContext()
        )
      )
    }
  }
}
