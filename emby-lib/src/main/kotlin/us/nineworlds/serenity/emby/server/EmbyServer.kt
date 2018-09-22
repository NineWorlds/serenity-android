package us.nineworlds.serenity.emby.server

import us.nineworlds.serenity.common.Server
import us.nineworlds.serenity.common.ServerTypes
import us.nineworlds.serenity.common.ServerTypes.EMBY

class EmbyServer : Server {

  private var serverName: String? = null
  private var ipAddress: String? = ""
  private var hostName: String? = null
  private var discoveryProtocol: String? = "Emby"

  override fun setServerName(serverName: String?) {
    this.serverName = serverName
  }

  override fun getIPAddress(): String? {
    return ipAddress
  }

  override fun setIPAddress(ipaddress: String?) {
    this.ipAddress = ipaddress
  }

  override fun getHostName(): String? {
    return hostName
  }

  override fun setHostName(hostName: String?) {
    this.hostName = hostName
  }

  override fun discoveryProtocol(): String? {
    return discoveryProtocol
  }

  override fun setDiscoveryProtocol(protocol: String?) {
    this.discoveryProtocol = protocol
  }

  override fun getServerName(): String? {
    return serverName
  }

  override fun hasMultipleAccounts(): Boolean = true
}