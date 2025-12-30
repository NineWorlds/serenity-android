package us.nineworlds.serenity.jellyfin.model

import us.nineworlds.serenity.common.media.model.IMedia
import us.nineworlds.serenity.common.media.model.IPart

class Media : IMedia {
    private var _aspectRatio: String? = null
    private var _audioCodec: String? = null
    private var _videoCodec: String? = null
    private var _videoResolution: String? = null
    private var _container: String? = null
    private var _audioChannels: String? = null
    private var _videoParts: List<IPart>? = null

    override fun getAudioChannels(): String? = _audioChannels

    override fun setAudioChannels(audioChannels: String?) {
        _audioChannels = audioChannels
    }

    override fun getContainer(): String? = _container

    override fun setContainer(container: String?) {
        _container = container
    }

    override fun getVideoPart(): List<IPart>? = _videoParts

    override fun setVideoPart(videoParts: List<IPart>?) {
        _videoParts = videoParts
    }

    override fun getAspectRatio(): String? = _aspectRatio

    override fun setAspectRatio(aspectRatio: String?) {
        _aspectRatio = aspectRatio
    }

    override fun getAudioCodec(): String? = _audioCodec

    override fun setAudioCodec(audioCodec: String?) {
        _audioCodec = audioCodec
    }

    override fun getVideoCodec(): String? = _videoCodec

    override fun setVideoCodec(videoCodec: String?) {
        _videoCodec = videoCodec
    }

    override fun getVideoResolution(): String? = _videoResolution

    override fun setVideoResolution(videoResolution: String?) {
        _videoResolution = videoResolution
    }
}
