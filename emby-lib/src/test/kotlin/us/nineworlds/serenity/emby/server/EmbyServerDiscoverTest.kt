package us.nineworlds.serenity.emby.server

import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class EmbyServerDiscoverTest {

    private lateinit var serverDiscovery : EmbyServerDiscover

    @Before
    fun setUp() {
        serverDiscovery = EmbyServerDiscover()
    }

    @Test
    fun `discover any known servers`() = runTest {
        serverDiscovery.findServers()
    }

}