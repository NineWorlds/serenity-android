package us.nineworlds.serenity.common.android.mediacodec

import android.media.MediaCodecList
import android.util.Log

class MediaCodecInfoUtil {

  val supportedContainers = hashMapOf<String, Boolean>("video/mkv" to true, "video/mp4" to true, "video/avi" to false, "video/webm" to true, "video/ogg" to true, "video/mv4" to true)

  fun logAvailableCodecs() {
    val mediaCodecList = MediaCodecList(MediaCodecList.ALL_CODECS)

    for (codec in mediaCodecList.codecInfos) {
      val codeInfo = "Codec Name: ${codec.name}\nIs Encoder: ${codec.isEncoder}\n"
      Log.d(MediaCodecInfoUtil::class.java.simpleName, codeInfo)
      for (type in codec.supportedTypes) {
        Log.d(MediaCodecInfoUtil::class.java.simpleName, "   Type: ${type}")
      }
    }
  }

  fun isCodecSupportted(mimeType : String): Boolean {
    val mediaCodecList = MediaCodecList(MediaCodecList.ALL_CODECS)
    for (codecInfo in mediaCodecList.codecInfos) {
      if (!codecInfo.isEncoder) {
        val types : Array<String> = codecInfo.supportedTypes
        for ( type in types) {
          if (type == mimeType) {
            Log.d(MediaCodecInfoUtil::class.java.simpleName, "MimeType Found for $mimeType!")
            return true
          }
        }
      }
    }

    Log.d(MediaCodecInfoUtil::class.java.simpleName, "MimeType $mimeType not supported.")

    return false
  }

  fun isExoPlayerContainerSupported(mimeType: String) = if (supportedContainers.contains(mimeType)) { supportedContainers.get(mimeType) } else { false }

  fun findCorrectVideoMimeType(mimeType: String): String {
    val videoMimeType = when(mimeType.substringAfter("video/").toLowerCase()) {
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
    val audioMimType = when(mimeType.substringAfter("audio/").toLowerCase()) {
      "mp3" -> "audio/mpeg"
      "aac" -> "audio/mp4a-latm"
      else -> mimeType
    }
    return audioMimType
  }
}