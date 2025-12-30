package us.nineworlds.serenity.common.android.mediacodec

import android.media.MediaCodecList
import android.util.Log

object MediaCodecInfoUtil {

    private const val TAG = "MediaCodecInfoUtil"

    private val supportedCodecs: Set<String> by lazy {
        val codecs = mutableSetOf<String>()
        val mediaCodecList = MediaCodecList(MediaCodecList.ALL_CODECS)
        for (codecInfo in mediaCodecList.codecInfos) {
            if (!codecInfo.isEncoder) {
                codecs.addAll(codecInfo.supportedTypes)
            }
        }
        codecs
    }

    /**
     * Logs to the logcat the available audio and video codecs that the device reports it supports.
     * The log will contain the codec name, whether is supports encoding or decoding, and the mime type it matches too.
     *
     */
    fun logAvailableCodecs() {
        val mediaCodecList = MediaCodecList(MediaCodecList.ALL_CODECS)

        for (codec in mediaCodecList.codecInfos) {
            val codeInfo = "Codec Name: ${codec.name}\nIs Encoder: ${codec.isEncoder}\n"
            Log.d(TAG, codeInfo)
            for (type in codec.supportedTypes) {
                Log.d(TAG, "   Type: ${type}")
            }
        }
    }

    /**
     * Looks up a codec by mime type and returns whether the device supports
     * the codec natively or not.
     *
     * @param mimeType The video or audio mimeType string for the codec
     */
    fun isCodecSupported(mimeType: String): Boolean = supportedCodecs.contains(mimeType)

    fun getMaxSupportedChannels(mimeType: String): Int {
        val mediaCodecList = MediaCodecList(MediaCodecList.ALL_CODECS)
        var maxChannels = 2
        if (mediaCodecList.codecInfos == null) {
            return maxChannels
        }
        for (codecInfo in mediaCodecList.codecInfos) {
            if (!codecInfo.isEncoder) {
                val types = codecInfo.supportedTypes
                val matchedType = types.firstOrNull { it.equals(mimeType, ignoreCase = true)}
                if (matchedType != null) {
                    try {
                        val capabilities = codecInfo.getCapabilitiesForType(matchedType)
                        val audioCapabilities = capabilities.audioCapabilities
                        if (audioCapabilities != null) {
                            if (audioCapabilities.maxInputChannelCount > maxChannels) {
                                maxChannels = audioCapabilities.maxInputChannelCount
                            }
                        }
                    } catch (e: Exception) {
                        Log.e(TAG, "Could not get capabilities for $mimeType", e)
                    }
                }
            }
        }
        return maxChannels
    }

    /**
     * Find the correct video mimetype based off information that was returned to us by the server.
     *
     */
    fun findCorrectVideoMimeType(mimeType: String): String {
        val videoMimeType = when (mimeType.substringAfter("video/").lowercase()) {
            "mpeg-4" -> "video/mp4"
            "mpeg4" -> "video/mp4v-es"
            "h264" -> "video/avc"
            "h263" -> "video/3gpp"
            "mpeg2" -> "video/mpeg2"
            else -> mimeType
        }
        return videoMimeType
    }

    fun findCorrectAudioMimeType(mimeType: String): String {
        val audioMimType = when (mimeType.substringAfter("audio/").lowercase()) {
            "mp3" -> "audio/mpeg"
            "aac" -> "audio/mp4a-latm"
            else -> mimeType
        }
        return audioMimType
    }
}
