package us.nineworlds.serenity.ui.util

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf
import toothpick.config.Module
import us.nineworlds.serenity.R
import us.nineworlds.serenity.TestingModule
import us.nineworlds.serenity.core.util.TimeUtil
import us.nineworlds.serenity.test.InjectingTest

@RunWith(RobolectricTestRunner::class)
class ImageInfographicUtilsTest : InjectingTest() {

  lateinit var imageInfographicUtils: ImageInfographicUtils
  lateinit var context: Context

  @Before
  override fun setUp() {
    super.setUp()
    imageInfographicUtils = ImageInfographicUtils(100, 100)
    context = ApplicationProvider.getApplicationContext()
  }

  override fun installTestModules() {
    scope.installTestModules(TestingModule(), TestModule())
  }

  @Test
  fun createAudioCodecImageSetsAACDrawable() {
    val view = imageInfographicUtils.createAudioCodecImage("aac", context)
    val shadowDrawable = shadowOf(view!!.drawable)
    assertThat(shadowDrawable.createdFromResId).isEqualTo(R.drawable.aac)
  }

  @Test
  fun createAudioCodecImageSetsAC3Drawable() {
    val view = imageInfographicUtils.createAudioCodecImage("ac3", context)
    val shadowDrawable = shadowOf(view!!.drawable)
    assertThat(shadowDrawable.createdFromResId).isEqualTo(R.drawable.ac3)
  }

  @Test
  fun createAudioCodecImageSetsAIFDrawable() {
    val view = imageInfographicUtils.createAudioCodecImage("aif", context)
    val shadowDrawable = shadowOf(view!!.drawable)
    assertThat(shadowDrawable.createdFromResId).isEqualTo(R.drawable.aif)
  }

  @Test
  fun createAudioCodecImageSetsAIFCDrawable() {
    val view = imageInfographicUtils.createAudioCodecImage("aifc", context)
    val shadowDrawable = shadowOf(view!!.drawable)
    assertThat(shadowDrawable.createdFromResId).isEqualTo(R.drawable.aifc)
  }

  @Test
  fun createAudioCodecImageSetsAIFFDrawable() {
    val view = imageInfographicUtils.createAudioCodecImage("aiff", context)
    val shadowDrawable = shadowOf(view!!.drawable)
    assertThat(shadowDrawable.createdFromResId).isEqualTo(R.drawable.aiff)
  }

  @Test
  fun createAudioCodecImageSetsAPEDrawable() {
    val view = imageInfographicUtils.createAudioCodecImage("ape", context)
    val shadowDrawable = shadowOf(view!!.drawable)
    assertThat(shadowDrawable.createdFromResId).isEqualTo(R.drawable.ape)
  }

  @Test
  fun createAudioCodecImageSetsAVCDrawable() {
    val view = imageInfographicUtils.createAudioCodecImage("avc", context)
    val shadowDrawable = shadowOf(view!!.drawable)
    assertThat(shadowDrawable.createdFromResId).isEqualTo(R.drawable.avc)
  }

  @Test
  fun createAudioCodecImageSetsCDDADrawable() {
    val view = imageInfographicUtils.createAudioCodecImage("cdda", context)
    val shadowDrawable = shadowOf(view!!.drawable)
    assertThat(shadowDrawable.createdFromResId).isEqualTo(R.drawable.cdda)
  }

  @Test
  fun createAudioCodecImageSetsDCADrawable() {
    val view = imageInfographicUtils.createAudioCodecImage("dca", context)
    val shadowDrawable = shadowOf(view!!.drawable)
    assertThat(shadowDrawable.createdFromResId).isEqualTo(R.drawable.dca)
  }

  @Test
  fun createAudioCodecImageSetsDTSDrawable() {
    val view = imageInfographicUtils.createAudioCodecImage("dts", context)
    val shadowDrawable = shadowOf(view!!.drawable)
    assertThat(shadowDrawable.createdFromResId).isEqualTo(R.drawable.dts)
  }

  @Test
  fun createAudioCodecImageSetsEAC3Drawable() {
    val view = imageInfographicUtils.createAudioCodecImage("eac3", context)
    val shadowDrawable = shadowOf(view!!.drawable)
    assertThat(shadowDrawable.createdFromResId).isEqualTo(R.drawable.eac3)
  }

  @Test
  fun createAudioCodecImageSetsFLACDrawable() {
    val view = imageInfographicUtils.createAudioCodecImage("flac", context)
    val shadowDrawable = shadowOf(view!!.drawable)
    assertThat(shadowDrawable.createdFromResId).isEqualTo(R.drawable.flac)
  }

  @Test
  fun createAudioCodecImageSetsMP1Drawable() {
    val view = imageInfographicUtils.createAudioCodecImage("mp1", context)
    val shadowDrawable = shadowOf(view!!.drawable)
    assertThat(shadowDrawable.createdFromResId).isEqualTo(R.drawable.mp1)
  }

  @Test
  fun createAudioCodecImageSetsMP2Drawable() {
    val view = imageInfographicUtils.createAudioCodecImage("mp2", context)
    val shadowDrawable = shadowOf(view!!.drawable)
    assertThat(shadowDrawable.createdFromResId).isEqualTo(R.drawable.mp2)
  }

  @Test
  fun createAudioCodecImageSetsMP3Drawable() {
    val view = imageInfographicUtils.createAudioCodecImage("mp3", context)
    val shadowDrawable = shadowOf(view!!.drawable)
    assertThat(shadowDrawable.createdFromResId).isEqualTo(R.drawable.mp3)
  }

  @Test
  fun createAudioCodecImageSetsOGGDrawable() {
    val view = imageInfographicUtils.createAudioCodecImage("ogg", context)
    val shadowDrawable = shadowOf(view!!.drawable)
    assertThat(shadowDrawable.createdFromResId).isEqualTo(R.drawable.ogg)
  }

  @Test
  fun createAudioCodecImageSetsWMADrawable() {
    val view = imageInfographicUtils.createAudioCodecImage("wma", context)
    val shadowDrawable = shadowOf(view!!.drawable)
    assertThat(shadowDrawable.createdFromResId).isEqualTo(R.drawable.wma)
  }

  @Test
  fun createAudioCodecImageDoesNotSetsWhenUnknown() {
    val view = imageInfographicUtils.createAudioCodecImage("zzz", context)
    assertThat(view).isNull()
  }

  @Test
  fun createVideoResolutionImageReturnsSDImage() {
    val view = imageInfographicUtils.createVideoResolutionImage("sd", context)
    val shadowDrawable = shadowOf(view!!.drawable)
    assertThat(shadowDrawable.createdFromResId).isEqualTo(R.drawable.res480)
  }

  @Test
  fun createVideoResolutionImageReturns480Image() {
    val view = imageInfographicUtils.createVideoResolutionImage("480", context)
    val shadowDrawable = shadowOf(view!!.drawable)
    assertThat(shadowDrawable.createdFromResId).isEqualTo(R.drawable.res480)
  }

  @Test
  fun createVideoResolutionImageReturns576Image() {
    val view = imageInfographicUtils.createVideoResolutionImage("576", context)
    val shadowDrawable = shadowOf(view!!.drawable)
    assertThat(shadowDrawable.createdFromResId).isEqualTo(R.drawable.res576)
  }

  @Test
  fun createVideoResolutionImageReturns720Image() {
    val view = imageInfographicUtils.createVideoResolutionImage("720", context)
    val shadowDrawable = shadowOf(view!!.drawable)
    assertThat(shadowDrawable.createdFromResId).isEqualTo(R.drawable.res720)
  }

  @Test
  fun createVideoResolutionImageReturnsHDImage() {
    val view = imageInfographicUtils.createVideoResolutionImage("hd", context)
    val shadowDrawable = shadowOf(view!!.drawable)
    assertThat(shadowDrawable.createdFromResId).isEqualTo(R.drawable.hd)
  }

  @Test
  fun createVideoResolutionImageReturns1080Image() {
    val view = imageInfographicUtils.createVideoResolutionImage("1080", context)
    val shadowDrawable = shadowOf(view!!.drawable)
    assertThat(shadowDrawable.createdFromResId).isEqualTo(R.drawable.res1080)
  }

  @Test
  fun createVideoResolutionImageForUnknownImageReturnsNull() {
    val view = imageInfographicUtils.createVideoResolutionImage("unknown", context)
    assertThat(view).isNull()
  }

  @Test
  fun createVideoResolutionImageForNullImageReturnsNull() {
    val view = imageInfographicUtils.createVideoResolutionImage(null, context)
    assertThat(view).isNull()
  }

  @Test
  fun createAspectRationImageReturns133RatioImage() {
    val view = imageInfographicUtils.createAspectRatioImage("1.33", context)
    val shadowDrawable = shadowOf(view!!.drawable)
    assertThat(shadowDrawable.createdFromResId).isEqualTo(R.drawable.aspect_1_33)
  }

  @Test
  fun createAspectRationImageReturns166RatioImage() {
    val view = imageInfographicUtils.createAspectRatioImage("1.66", context)
    val shadowDrawable = shadowOf(view!!.drawable)
    assertThat(shadowDrawable.createdFromResId).isEqualTo(R.drawable.aspect_1_66)
  }

  @Test
  fun createAspectRationImageReturns178RatioImage() {
    val view = imageInfographicUtils.createAspectRatioImage("1.78", context)
    val shadowDrawable = shadowOf(view!!.drawable)
    assertThat(shadowDrawable.createdFromResId).isEqualTo(R.drawable.aspect_1_78)
  }

  @Test
  fun createAspectRationImageReturns185RatioImage() {
    val view = imageInfographicUtils.createAspectRatioImage("1.85", context)
    val shadowDrawable = shadowOf(view!!.drawable)
    assertThat(shadowDrawable.createdFromResId).isEqualTo(R.drawable.aspect_1_85)
  }

  @Test
  fun createAspectRationImageReturns220RatioImage() {
    val view = imageInfographicUtils.createAspectRatioImage("2.20", context)
    val shadowDrawable = shadowOf(view!!.drawable)
    assertThat(shadowDrawable.createdFromResId).isEqualTo(R.drawable.aspect_2_20)
  }

  @Test
  fun createAspectRationImageReturns235RatioImage() {
    val view = imageInfographicUtils.createAspectRatioImage("2.35", context)
    val shadowDrawable = shadowOf(view!!.drawable)
    assertThat(shadowDrawable.createdFromResId).isEqualTo(R.drawable.aspect_2_35)
  }

  @Test
  fun createAspectRationImageForNullReturnsNullImage() {
    val view = imageInfographicUtils.createAspectRatioImage(null, context)
    assertThat(view).isNull()
  }

  @Test
  fun createAspectRationImageForUnknownAspectReturnsNullImage() {
    val view = imageInfographicUtils.createAspectRatioImage("unknown", context)
    assertThat(view).isNull()
  }

  @Test
  fun createContentRatingImageReturnsGImage() {
    val view = imageInfographicUtils.createContentRatingImage("G", context)
    val shadowDrawable = shadowOf(view.drawable)
    assertThat(shadowDrawable.createdFromResId).isEqualTo(R.drawable.mpaa_g)
  }

  @Test
  fun createContentRatingImageReturnsPGImage() {
    val view = imageInfographicUtils.createContentRatingImage("PG", context)
    val shadowDrawable = shadowOf(view.drawable)
    assertThat(shadowDrawable.createdFromResId).isEqualTo(R.drawable.mpaa_pg)
  }

  @Test
  fun createContentRatingImageReturnsPG13Image() {
    val view = imageInfographicUtils.createContentRatingImage("PG-13", context)
    val shadowDrawable = shadowOf(view.drawable)
    assertThat(shadowDrawable.createdFromResId).isEqualTo(R.drawable.mpaa_pg13)
  }

  @Test
  fun createContentRatingImageReturnsRImage() {
    val view = imageInfographicUtils.createContentRatingImage("R", context)
    val shadowDrawable = shadowOf(view.drawable)
    assertThat(shadowDrawable.createdFromResId).isEqualTo(R.drawable.mpaa_r)
  }

  @Test
  fun createContentRatingImageReturnsNC17Image() {
    val view = imageInfographicUtils.createContentRatingImage("NC-17", context)
    val shadowDrawable = shadowOf(view.drawable)
    assertThat(shadowDrawable.createdFromResId).isEqualTo(R.drawable.mpaa_nc17)
  }

  @Test
  fun createContentRatingImageReturnsNullImage() {
    val view = imageInfographicUtils.createContentRatingImage(null, context)
    val shadowDrawable = shadowOf(view.drawable)
    assertThat(shadowDrawable.createdFromResId).isEqualTo(R.drawable.mpaa_nr)
  }

  @Test
  fun createVideoCodecReturnsDivxImage() {
    val view = imageInfographicUtils.createVideoCodec("divx", context)
    val shadowDrawable = shadowOf(view!!.drawable)
    assertThat(shadowDrawable.createdFromResId).isEqualTo(R.drawable.divx)
  }

  @Test
  fun createVideoCodecReturnsDiv3Image() {
    val view = imageInfographicUtils.createVideoCodec("div3", context)
    val shadowDrawable = shadowOf(view!!.drawable)
    assertThat(shadowDrawable.createdFromResId).isEqualTo(R.drawable.div3)
  }

  @Test
  fun createVideoCodecReturnsVC1Image() {
    val view = imageInfographicUtils.createVideoCodec("vc-1", context)
    val shadowDrawable = shadowOf(view!!.drawable)
    assertThat(shadowDrawable.createdFromResId).isEqualTo(R.drawable.vc_1)
  }

  @Test
  fun createVideoCodecReturnsh264Image() {
    val view = imageInfographicUtils.createVideoCodec("h264", context)
    val shadowDrawable = shadowOf(view!!.drawable)
    assertThat(shadowDrawable.createdFromResId).isEqualTo(R.drawable.h264)
  }

  @Test
  fun createVideoCodecReturnsMPEG4Image() {
    val view = imageInfographicUtils.createVideoCodec("mpeg4", context)
    val shadowDrawable = shadowOf(view!!.drawable)
    assertThat(shadowDrawable.createdFromResId).isEqualTo(R.drawable.h264)
  }

  @Test
  fun createVideoCodecReturnsMPEG2Image() {
    val view = imageInfographicUtils.createVideoCodec("mpeg2", context)
    val shadowDrawable = shadowOf(view!!.drawable)
    assertThat(shadowDrawable.createdFromResId).isEqualTo(R.drawable.mpeg2video)
  }

  @Test
  fun createVideoCodecReturnsMPEG1Image() {
    val view = imageInfographicUtils.createVideoCodec("mpeg1", context)
    val shadowDrawable = shadowOf(view!!.drawable)
    assertThat(shadowDrawable.createdFromResId).isEqualTo(R.drawable.mpeg1video)
  }

  @Test
  fun createVideoCodecReturnsXvidImage() {
    val view = imageInfographicUtils.createVideoCodec("xvid", context)
    val shadowDrawable = shadowOf(view!!.drawable)
    assertThat(shadowDrawable.createdFromResId).isEqualTo(R.drawable.xvid)
  }

  @Test
  fun createVideoCodecReturnsNullImage() {
    val view = imageInfographicUtils.createVideoCodec(null, context)
    assertThat(view).isNull()
  }

  @Test
  fun createVideoCodecReturnsNullImageForUnknown() {
    val view = imageInfographicUtils.createVideoCodec("unknown", context)
    assertThat(view).isNull()
  }

  @Test
  fun createTVContentRatingReturnsTVGImage() {
    val view = imageInfographicUtils.createTVContentRating("TV-G", context)
    val shadowDrawable = shadowOf(view.drawable)
    assertThat(shadowDrawable.createdFromResId).isEqualTo(R.drawable.tvg)
  }

  @Test
  fun createTVContentRatingReturnsTVPGImage() {
    val view = imageInfographicUtils.createTVContentRating("TV-PG", context)
    val shadowDrawable = shadowOf(view.drawable)
    assertThat(shadowDrawable.createdFromResId).isEqualTo(R.drawable.tvpg)
  }

  @Test
  fun createTVContentRatingReturnsTV14Image() {
    val view = imageInfographicUtils.createTVContentRating("TV-14", context)
    val shadowDrawable = shadowOf(view.drawable)
    assertThat(shadowDrawable.createdFromResId).isEqualTo(R.drawable.tv14)
  }

  @Test
  fun createTVContentRatingReturnsTVMAImage() {
    val view = imageInfographicUtils.createTVContentRating("TV-MA", context)
    val shadowDrawable = shadowOf(view.drawable)
    assertThat(shadowDrawable.createdFromResId).isEqualTo(R.drawable.tvma)
  }

  @Test
  fun createTVContentRatingReturnsTVYImage() {
    val view = imageInfographicUtils.createTVContentRating("TV-Y", context)
    val shadowDrawable = shadowOf(view.drawable)
    assertThat(shadowDrawable.createdFromResId).isEqualTo(R.drawable.tvy)
  }

  @Test
  fun createTVContentRatingReturnsTVY7Image() {
    val view = imageInfographicUtils.createTVContentRating("TV-Y7", context)
    val shadowDrawable = shadowOf(view.drawable)
    assertThat(shadowDrawable.createdFromResId).isEqualTo(R.drawable.tvy7)
  }

  @Test
  fun createTVContentRatingReturnsTVNRImage() {
    val view = imageInfographicUtils.createTVContentRating("unknown", context)
    val shadowDrawable = shadowOf(view.drawable)
    assertThat(shadowDrawable.createdFromResId).isEqualTo(R.drawable.tvnr)
  }

  @Test
  fun createAudioChannelsImageReturns0Image() {
    val view = imageInfographicUtils.createAudioChannlesImage("0", context)
    val shadowDrawable = shadowOf(view!!.drawable)
    assertThat(shadowDrawable.createdFromResId).isEqualTo(R.drawable.audio_0)
  }

  @Test
  fun createAudioChannelsImageReturns1Image() {
    val view = imageInfographicUtils.createAudioChannlesImage("1", context)
    val shadowDrawable = shadowOf(view!!.drawable)
    assertThat(shadowDrawable.createdFromResId).isEqualTo(R.drawable.audio_1)
  }

  @Test
  fun createAudioChannelsImageReturns2Image() {
    val view = imageInfographicUtils.createAudioChannlesImage("2", context)
    val shadowDrawable = shadowOf(view!!.drawable)
    assertThat(shadowDrawable.createdFromResId).isEqualTo(R.drawable.audio_2)
  }

  @Test
  fun createAudioChannelsImageReturns6Image() {
    val view = imageInfographicUtils.createAudioChannlesImage("6", context)
    val shadowDrawable = shadowOf(view!!.drawable)
    assertThat(shadowDrawable.createdFromResId).isEqualTo(R.drawable.audio_6)
  }

  @Test
  fun createAudioChannelsImageReturns8Image() {
    val view = imageInfographicUtils.createAudioChannlesImage("8", context)
    val shadowDrawable = shadowOf(view!!.drawable)
    assertThat(shadowDrawable.createdFromResId).isEqualTo(R.drawable.audio_8)
  }

  @Test
  fun createAudioChannelsImageForUnknownReturnsNullImage() {
    val view = imageInfographicUtils.createAudioChannlesImage("unknown", context)
    assertThat(view).isNull()
  }

  @Test
  fun createAudioChannelsImageForNullReturnsNullImage() {
    val view = imageInfographicUtils.createAudioChannlesImage(null, context)
    assertThat(view).isNull()
  }

  inner class TestModule : Module() {
    init {
      bind(TimeUtil::class.java).toInstance(TimeUtil())
    }
  }
}