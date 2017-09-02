package us.nineworlds.serenity.emby.server

import android.content.Context
import mediabrowser.apiinteraction.ApiEventListener
import mediabrowser.apiinteraction.ICredentialProvider
import mediabrowser.apiinteraction.Response
import mediabrowser.apiinteraction.android.AndroidConnectionManager
import mediabrowser.apiinteraction.android.AndroidCredentialProvider
import mediabrowser.apiinteraction.connect.ConnectService
import mediabrowser.apiinteraction.connectionmanager.FindServersResponse
import mediabrowser.apiinteraction.connectionmanager.GetConnectServersResponse
import mediabrowser.apiinteraction.device.IDevice
import mediabrowser.apiinteraction.http.IAsyncHttpClient
import mediabrowser.model.apiclient.ServerCredentials
import mediabrowser.model.apiclient.ServerInfo
import mediabrowser.model.logging.ILogger
import mediabrowser.model.serialization.IJsonSerializer
import mediabrowser.model.session.ClientCapabilities
import java.util.ArrayList

/**
 * Created by dcarver on 8/20/17.
 */
class SerenityAndroidConnectionManager(context: Context?, jsonSerializer: IJsonSerializer?, logger: ILogger?,
    httpClient: IAsyncHttpClient?, applicationName: String?, applicationVersion: String?, device: IDevice?,
    clientCapabilities: ClientCapabilities?, apiEventListener: ApiEventListener?) : AndroidConnectionManager(context,
    jsonSerializer, logger, httpClient, applicationName, applicationVersion, device, clientCapabilities,
    apiEventListener) {


  fun findAvailableServers(context: Context, response: Response<ArrayList<ServerInfo>>): List<ServerInfo> {
    this.logger.Debug("Getting saved servers via credential provider", *arrayOfNulls<Any>(0))

    val credentialProvider: ICredentialProvider = AndroidCredentialProvider(jsonSerializer, context, logger);
    val tempCredentials: ServerCredentials
    try {
      tempCredentials = credentialProvider.GetCredentials()
    } catch (var10: Exception) {
      this.logger.ErrorException("Error getting available servers", var10, *arrayOfNulls<Any>(0))
      return emptyList()
    }

    val numTasksCompleted = intArrayOf(0)
    var foundServers: ArrayList<ServerInfo> = ArrayList()
    val connectServers: ArrayList<ServerInfo> = ArrayList()
    val connectionService: ConnectService = ConnectService(jsonSerializer, logger, httpClient, applicationName,
        applicationVersion)
    val findServersResponse = FindServersResponse(this, tempCredentials, foundServers, connectServers,
        numTasksCompleted, 2, response)
    this.logger.Debug("Scanning network for local servers", *arrayOfNulls<Any>(0))
    this.FindServers(findServersResponse)
    val connectServersResponse = GetConnectServersResponse(this.logger, connectionService, tempCredentials,
        foundServers, connectServers, 2, numTasksCompleted, response, this)

    return foundServers

  }
}