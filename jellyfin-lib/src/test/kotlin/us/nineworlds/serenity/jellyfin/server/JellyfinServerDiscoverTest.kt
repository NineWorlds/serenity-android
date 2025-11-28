package us.nineworlds.serenity.jellyfin.server

import app.cash.turbine.turbineScope
import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.shadows.ShadowLog
import timber.log.Timber
import us.nineworlds.serenity.common.channels.ServerChannel
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.SocketException

@RunWith(RobolectricTestRunner::class)
class JellyfinServerDiscoverTest {

    private lateinit var serverDiscovery : JellyfinServerDiscover
    private lateinit var fakeServer: FakeJellyfinServer

    @Before
    fun setUp() {
        Timber.plant(Timber.DebugTree())
        ShadowLog.stream = System.out
        serverDiscovery = JellyfinServerDiscover()
        fakeServer = FakeJellyfinServer()
    }

    @After
    fun tearDown() {
        fakeServer.stop()
    }

    @Test
    fun `discover any known servers`() = runTest {
        fakeServer.start()
        fakeServer.awaitReady()
        turbineScope {
            val servers = ServerChannel.serverEvents.distinctUntilChanged().testIn(backgroundScope)
            serverDiscovery.findServers()

            val serverItem = servers.awaitItem()

            assertThat(serverItem.serverName).isEqualTo("Jellyfin - Jellyfin Server")
            servers.cancelAndIgnoreRemainingEvents()
        }
    }

}

class FakeJellyfinServer {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private var socket: DatagramSocket? = null
    private val ready = CompletableDeferred<Unit>()

    fun start() {
        scope.launch {
            try {
                socket = DatagramSocket(7359)
                ready.complete(Unit)
                while (isActive) {
                    val buf = ByteArray(256)
                    val packet = DatagramPacket(buf, buf.size)
                    socket?.receive(packet) ?: break

                    val response = """
                        {
                           "Address": "http://127.0.0.1:8096",
                           "Id": "someid",
                           "Name": "Jellyfin Server"
                        }
                    """.trimIndent()

                    val responsePacket = DatagramPacket(
                        response.toByteArray(),
                        response.length,
                        packet.address,
                        packet.port
                    )
                    socket?.send(responsePacket)
                }
            } catch (e: SocketException) {
                Timber.d("FakeJellyfinServer socket closed, server stopping.")
            } catch (e: Exception) {
                if (isActive) {
                    Timber.e(e, "FakeJellyfinServer error")
                }
            } finally {
                socket?.close()
            }
        }
    }

    suspend fun awaitReady() {
        ready.await()
    }

    fun stop() {
        socket?.close()
        scope.cancel()
    }
}
