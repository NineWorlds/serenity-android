package us.nineworlds.serenity.emby.server

import app.cash.turbine.turbineScope
import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.shadows.ShadowLog
import timber.log.Timber
import us.nineworlds.serenity.common.channels.ServerChannel

@RunWith(RobolectricTestRunner::class)
class EmbyServerDiscoverTest {

    private lateinit var serverDiscovery : EmbyServerDiscover

    @Before
    fun setUp() {
        Timber.plant(Timber.DebugTree())
        ShadowLog.stream = System.out
        serverDiscovery = EmbyServerDiscover()
    }

    @Test
    fun `discover any known servers`() = runTest {
        turbineScope {
            val servers = ServerChannel.serverEvents.distinctUntilChanged().testIn(backgroundScope)
            serverDiscovery.findServers()
            val serverItem = servers.awaitItem()
            assertThat(serverItem.serverName).isEqualTo("Emby - Emby Server")
            servers.cancelAndIgnoreRemainingEvents()
        }
    }

}