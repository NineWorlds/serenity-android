package us.nineworlds.serenity.common.android.mediacodec

import android.media.MediaCodecInfo
import android.media.MediaCodecList
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.After
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class MediaCodecInfoUtilTest {

    companion object {
        
        @BeforeClass
        @JvmStatic
        fun initMocks() {
            mockkConstructor(MediaCodecList::class)
        }
        
        @AfterClass
        @JvmStatic
        fun resetMocks() = unmockkAll()
    }
    
  @After
  fun tearDown() {
    clearAllMocks()
  }

  @Test
  fun `logAvailableCodecs logs available codecs`() {
    val codecInfo = mockk<MediaCodecInfo>(relaxed = true)
    every { codecInfo.name } returns "testCodec"
    every { codecInfo.isEncoder } returns false
    every { codecInfo.supportedTypes } returns arrayOf("video/mp4")
    
    every { anyConstructed<MediaCodecList>().codecInfos } returns arrayOf(codecInfo)

    MediaCodecInfoUtil.logAvailableCodecs()
    
    verify { codecInfo.name }
    verify { codecInfo.isEncoder }
    verify { codecInfo.supportedTypes }
  }

  @Test
  fun `isCodecSupported returns true for supported codec`() {
    val codecInfo = mockk<MediaCodecInfo>(relaxed = true)
    every { codecInfo.isEncoder } returns false
    every { codecInfo.supportedTypes } returns arrayOf("video/mp4")
    
    every { anyConstructed<MediaCodecList>().codecInfos } returns arrayOf(codecInfo)

    assertThat(MediaCodecInfoUtil.isCodecSupported("video/mp4")).isTrue()
  }

  @Test
  fun `isCodecSupported returns false for unsupported codec`() {
    val codecInfo = mockk<MediaCodecInfo>(relaxed = true)
    every { codecInfo.isEncoder } returns false
    every { codecInfo.supportedTypes } returns arrayOf("video/mp4")
    
    every { anyConstructed<MediaCodecList>().codecInfos } returns arrayOf(codecInfo)

    assertThat(MediaCodecInfoUtil.isCodecSupported("video/avc")).isFalse()
  }

  @Test
  @Ignore("should look at Shadowing the MediaCodecInfo and related classes to test this")
  fun `getMaxSupportedChannels returns correct max channels`() {
    val codecInfo = mockk<MediaCodecInfo>(relaxed = true)

    every { codecInfo.isEncoder } returns false
    every { codecInfo.supportedTypes } returns arrayOf("audio/mp4a-latm")
    // Chain the mocks to avoid creating explicit mocks for final inner classes which causes issues with Robolectric
    every { codecInfo.getCapabilitiesForType("audio/mp4a-latm").audioCapabilities.maxInputChannelCount } returns 6

    every { anyConstructed<MediaCodecList>().codecInfos } returns arrayOf(codecInfo)

    val result = MediaCodecInfoUtil.getMaxSupportedChannels("audio/mp4a-latm")
    assertThat(result).isEqualTo(6)
  }

  @Test
  fun `getMaxSupportedChannels returns default when no codec found`() {
    val codecInfo = mockk<MediaCodecInfo>(relaxed = true)
    every { codecInfo.isEncoder } returns false
    every { codecInfo.supportedTypes } returns arrayOf("audio/mpeg")

    every { anyConstructed<MediaCodecList>().codecInfos } returns arrayOf(codecInfo)

    val result = MediaCodecInfoUtil.getMaxSupportedChannels("audio/mp4a-latm")
    assertThat(result).isEqualTo(2)
  }

  @Test
  @Ignore("should look at Shadowing the MediaCodecInfo and related classes to test this")
  fun `getMaxSupportedChannels handles exception gracefully`() {
    val codecInfo = mockk<MediaCodecInfo>(relaxed = true)
    every { codecInfo.isEncoder } returns false
    every { codecInfo.supportedTypes } returns arrayOf("audio/mp4a-latm")
    every { codecInfo.getCapabilitiesForType("audio/mp4a-latm") } throws RuntimeException("Error")

    every { anyConstructed<MediaCodecList>().codecInfos } returns arrayOf(codecInfo)

    val result = MediaCodecInfoUtil.getMaxSupportedChannels("audio/mp4a-latm")
    assertThat(result).isEqualTo(2)
  }

  @Test
  fun `getMaxSupportedChannels returns default when codecInfos is null`() {
    every { anyConstructed<MediaCodecList>().codecInfos } returns null

    val result = MediaCodecInfoUtil.getMaxSupportedChannels("audio/mp4a-latm")
    assertThat(result).isEqualTo(2)
  }

  @Test
  fun `findCorrectVideoMimeType returns correct mime type`() {
    assertThat(MediaCodecInfoUtil.findCorrectVideoMimeType("video/mpeg-4")).isEqualTo("video/mp4")
    assertThat(MediaCodecInfoUtil.findCorrectVideoMimeType("video/mpeg4")).isEqualTo("video/mp4v-es")
    assertThat(MediaCodecInfoUtil.findCorrectVideoMimeType("video/h264")).isEqualTo("video/avc")
    assertThat(MediaCodecInfoUtil.findCorrectVideoMimeType("video/h263")).isEqualTo("video/3gpp")
    assertThat(MediaCodecInfoUtil.findCorrectVideoMimeType("video/mpeg2")).isEqualTo("video/mpeg2")
    assertThat(MediaCodecInfoUtil.findCorrectVideoMimeType("video/unknown")).isEqualTo("video/unknown")
  }

  @Test
  fun `findCorrectAudioMimeType returns correct mime type`() {
    assertThat(MediaCodecInfoUtil.findCorrectAudioMimeType("audio/mp3")).isEqualTo("audio/mpeg")
    assertThat(MediaCodecInfoUtil.findCorrectAudioMimeType("audio/aac")).isEqualTo("audio/mp4a-latm")
    assertThat(MediaCodecInfoUtil.findCorrectAudioMimeType("audio/unknown")).isEqualTo("audio/unknown")
  }
}
