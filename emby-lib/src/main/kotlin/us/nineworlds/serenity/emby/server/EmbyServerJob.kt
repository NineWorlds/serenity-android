package us.nineworlds.serenity.emby.server

import android.content.Context
import com.birbit.android.jobqueue.RetryConstraint
import mediabrowser.apiinteraction.ApiEventListener
import mediabrowser.apiinteraction.Response
import mediabrowser.apiinteraction.android.AndroidDevice
import mediabrowser.apiinteraction.android.GsonJsonSerializer
import mediabrowser.apiinteraction.android.VolleyHttpClient
import mediabrowser.apiinteraction.device.IDevice
import mediabrowser.apiinteraction.http.IAsyncHttpClient
import mediabrowser.model.apiclient.ServerInfo
import mediabrowser.model.logging.ILogger
import mediabrowser.model.logging.NullLogger
import mediabrowser.model.serialization.IJsonSerializer
import mediabrowser.model.session.ClientCapabilities
import org.greenrobot.eventbus.EventBus
import us.nineworlds.serenity.common.android.injection.ApplicationContext
import us.nineworlds.serenity.common.android.injection.InjectingJob
import javax.inject.Inject


class EmbyServerJob : InjectingJob() {

  @Inject lateinit var eventBus: EventBus

  @field:[Inject ApplicationContext]
  lateinit var context: Context


  override fun shouldReRunOnThrowable(throwable: Throwable, runCount: Int, maxRunCount: Int): RetryConstraint? {
    return null
  }

  override fun onAdded() {
  }

  override fun onCancel(cancelReason: Int, throwable: Throwable?) {
  }

  override fun onRun() {
    val logger: ILogger = NullLogger()
    val jsonSerializer: IJsonSerializer = GsonJsonSerializer()
    val device: IDevice = AndroidDevice(context)
    val capabilities: ClientCapabilities = ClientCapabilities()
    val apiListener: ApiEventListener = ApiEventListener()
    val httpClient: IAsyncHttpClient = VolleyHttpClient(logger, context)

    val connectionManager: SerenityAndroidConnectionManager = SerenityAndroidConnectionManager(context, jsonSerializer,
        logger, httpClient, "Serenity for Android", "2.x", device, capabilities, apiListener)

    val findAvailableServers = connectionManager.findAvailableServers(context,
        object : Response<ArrayList<ServerInfo>>() {

          override fun onResponse(response: ArrayList<ServerInfo>) {
            response.forEach { serverInfo: ServerInfo ->
              val embyServer: EmbyServer = EmbyServer()
              embyServer.serverName = serverInfo.name
              embyServer.ipAddress = serverInfo.remoteAddress

              eventBus.post(embyServer)
            }
          }
        })

    if (findAvailableServers.isNotEmpty()) {
      findAvailableServers.forEach { serverInfo: ServerInfo ->
        val embyServer: EmbyServer = EmbyServer()
        embyServer.serverName = serverInfo.name + " - Emby"
        embyServer.ipAddress = serverInfo.localAddress

        eventBus.post(embyServer)
      }
    }

  }


}