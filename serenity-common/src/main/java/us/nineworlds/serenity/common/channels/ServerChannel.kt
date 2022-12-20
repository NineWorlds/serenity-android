package us.nineworlds.serenity.common.channels

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.UNLIMITED
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import us.nineworlds.serenity.common.Server

object ServerChannel {

    val serverChannel = Channel<Server>(UNLIMITED)

    private val serverSharedFlow = MutableSharedFlow<Server>()

    val serverEvents = serverSharedFlow.asSharedFlow()

    suspend fun invokeServerEvent(event: Server) = serverSharedFlow.emit(event)
}