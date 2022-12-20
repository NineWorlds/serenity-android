package us.nineworlds.serenity.emby.server

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.greenrobot.eventbus.EventBus
import timber.log.Timber
import us.nineworlds.serenity.common.Server
import us.nineworlds.serenity.common.channels.ServerChannel
import java.io.IOException
import java.net.*


class EmbyServerDiscover {

    private val eventBus = EventBus.getDefault()
    private val moshiBuilder = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    suspend fun findServers() = withContext(Dispatchers.IO) {
        try {
            //Open a random port to send the package
            val c = DatagramSocket()
            c.setBroadcast(true)

            val sendData = "who is EmbyServer?".toByteArray()

            val port = 7359

            //Try the 255.255.255.255 first

            try {
                val sendPacket = DatagramPacket(sendData, sendData.size, InetAddress.getByName("255.255.255.255"), port)
                c.send(sendPacket)
                Timber.d(">>> Request packet sent to: 255.255.255.255 (DEFAULT)")
            } catch (e: Exception) {
                Timber.e(e, "Error sending DatagramPacket")
            }

//      try {
//        val sendPacket = DatagramPacket(sendData, sendData.size, useMultiCastAddress(), port)
//        c.send(sendPacket)
//      } catch (e: Exception) {
//        Timber.e(e, "error sending to multicast address")
//      }

            // Broadcast the message over all the network interfaces
            val interfaces = NetworkInterface.getNetworkInterfaces()
            while (interfaces.hasMoreElements()) {
                val networkInterface = interfaces.nextElement() as NetworkInterface

                if (networkInterface.isLoopback() || !networkInterface.isUp()) {
                    continue // Don't want to broadcast to the loopback interface
                }

                for (interfaceAddress in networkInterface.getInterfaceAddresses()) {
                    val broadcast = interfaceAddress.getBroadcast() ?: continue

                    // Send the broadcast package!
                    try {
                        val sendPacket = DatagramPacket(sendData, sendData.size, broadcast, port)
                        c.send(sendPacket)
                    } catch (e: Exception) {
                        Timber.e(e, "Error sending DatagramPacket")
                    }

                    Timber.d(
                            ">>> Request packet sent to: " + broadcast.getHostAddress() + "; Interface: " + networkInterface.getDisplayName()
                    )
                }
            }

            Timber.d(">>> Done looping over all network interfaces. Now waiting for a reply!")

            receive(c, 8000L)

            //Close the port!
            c.close()
        } catch (ex: Exception) {
            Timber.e(ex, "Error finding servers")
        }
    }

    @Throws(IOException::class)
    private suspend fun receive(c: DatagramSocket, timeoutMs: Long) {

        var timeout = timeoutMs

        val servers = ArrayList<Server>()

        while (timeoutMs > 0) {

            val startTime = System.currentTimeMillis()

            // Wait for a response
            val recvBuf = ByteArray(15000)
            val receivePacket = DatagramPacket(recvBuf, recvBuf.size)
            c.soTimeout = timeoutMs.toInt()

            try {
                c.receive(receivePacket)
            } catch (e: SocketTimeoutException) {
                Timber.d("Server discovery timed out waiting for response.")
                break
            }

            // We have a response
            Timber.d(">>> Broadcast response from server: " + receivePacket.address.hostAddress)

            // Check if the message is correct
            val message = String(receivePacket.data).trim { it <= ' ' }

            Timber.d(javaClass.name + ">>> Broadcast response from server: " + message)

            val embyServerInfo = moshiBuilder.adapter(EmbyServerInfo::class.java).fromJson(message)

            val server = EmbyServer()
            val uri = URI.create(embyServerInfo!!.remoteAddres)
            Timber.d("Server Remote Address: ${embyServerInfo!!.remoteAddres}")
            Timber.d("Server host: ${uri.host}")

            server.ipAddress = uri.host
            server.port = uri.port.toString()
            Timber.d("Server Port: ${uri.port}")

            server.serverName = "Emby - " + embyServerInfo.name

            ServerChannel.invokeServerEvent(server)

            // TODO: Once completely migrated to the ServerChannel remove the event bus.
            eventBus.post(server)

            val endTime = System.currentTimeMillis()
            timeout -= (endTime - startTime)
        }

        Timber.d("Found %d servers", servers.size)
    }

    @Throws(IOException::class)
    suspend fun useMultiCastAddress(): InetAddress {
        return InetAddress.getByName(MULTICAST_ADDRESS)
    }

    companion object {
        const val MULTICAST_ADDRESS = "239.255.255.250"
    }

}