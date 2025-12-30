package us.nineworlds.serenity.jellyfin.model

import us.nineworlds.serenity.common.media.model.IPart
import us.nineworlds.serenity.common.media.model.IStream

class Part : IPart {
    private var _key: String? = null
    private var _filename: String? = null
    private var _container: String? = null
    private var _streams: List<IStream>? = null

    override fun getStreams(): List<IStream>? = _streams

    override fun setStreams(streams: List<IStream>?) {
        _streams = streams
    }

    override fun getContainer(): String? = _container

    override fun setContainer(container: String?) {
        _container = container
    }

    override fun getKey(): String? = _key

    override fun setKey(key: String?) {
        _key = key
    }

    override fun getFilename(): String? = _filename

    override fun setFilename(filename: String?) {
        _filename = filename
    }
}
