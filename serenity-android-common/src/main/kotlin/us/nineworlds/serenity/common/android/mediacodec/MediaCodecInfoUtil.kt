package us.nineworlds.serenity.common.android.mediacodec

import android.media.MediaCodecList
import android.util.Log

class MediaCodecInfoUtil {

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
}