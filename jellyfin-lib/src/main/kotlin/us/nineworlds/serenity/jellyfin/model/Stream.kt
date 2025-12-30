package us.nineworlds.serenity.jellyfin.model

import us.nineworlds.serenity.common.media.model.IStream

class Stream : IStream {
    private var _id: Int = 0
    private var _streamType: Int = 0
    private var _codec: String? = null
    private var _index: String? = null
    private var _channels: String? = null
    private var _duration: String? = null
    private var _bitrate: String? = null
    private var _bitrateMode: String? = null
    private var _profile: String? = null
    private var _optimizedForStreaming: String? = null
    private var _format: String? = null
    private var _key: String? = null
    private var _language: String? = null
    private var _languageCode: String? = null

    override fun getLanguage(): String? = _language

    override fun setLanguage(language: String?) {
        _language = language
    }

    override fun getLanguageCode(): String? = _languageCode

    override fun setLanguageCode(languageCode: String?) {
        _languageCode = languageCode
    }

    override fun getId(): Int = _id

    override fun setId(id: Int) {
        _id = id
    }

    override fun getStreamType(): Int = _streamType

    override fun setStreamType(streamType: Int) {
        _streamType = streamType
    }

    override fun getCodec(): String? = _codec

    override fun setCodec(codec: String?) {
        _codec = codec
    }

    override fun getIndex(): String? = _index

    override fun setIndex(index: String?) {
        _index = index
    }

    override fun getChannels(): String? = _channels

    override fun setChannels(channels: String?) {
        _channels = channels
    }

    override fun getDuration(): String? = _duration

    override fun setDuration(duration: String?) {
        _duration = duration
    }

    override fun getBitrate(): String? = _bitrate

    override fun setBitrate(bitrate: String?) {
        _bitrate = bitrate
    }

    override fun getBitrateMode(): String? = _bitrateMode

    override fun setBitrateMode(bitrateMode: String?) {
        _bitrateMode = bitrateMode
    }

    override fun getProfile(): String? = _profile

    override fun setProfile(profile: String?) {
        _profile = profile
    }

    override fun getOptimizedForStreaming(): String? = _optimizedForStreaming

    override fun setOptimizedForStreaming(optimizedForStreaming: String?) {
        _optimizedForStreaming = optimizedForStreaming
    }

    override fun getFormat(): String? = _format

    override fun setFormat(format: String?) {
        _format = format
    }

    override fun getKey(): String? = _key

    override fun setKey(key: String?) {
        _key = key
    }
}
