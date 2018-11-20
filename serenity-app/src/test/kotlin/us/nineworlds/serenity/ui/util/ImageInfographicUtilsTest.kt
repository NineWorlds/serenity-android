package us.nineworlds.serenity.ui.util

import android.app.Activity
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf
import us.nineworlds.serenity.R
import us.nineworlds.serenity.TestingModule
import us.nineworlds.serenity.test.InjectingTest

@RunWith(RobolectricTestRunner::class)
class ImageInfographicUtilsTest : InjectingTest() {

  lateinit var imageInfographicUtils: ImageInfographicUtils
  lateinit var context : Activity

  @Before override fun setUp() {
    super.setUp()
    imageInfographicUtils = ImageInfographicUtils(100, 100)
    context = Robolectric.setupActivity(Activity::class.java)
  }

  @After fun tearDown() {
    context.finish()
  }

  override fun installTestModules() {
    scope.installTestModules(TestingModule())
  }
  
  @Test fun createAudioCodecImageSetsAACDrawable() {
    val view = imageInfographicUtils.createAudioCodecImage("aac", context)
    val shadowDrawable = shadowOf(view!!.drawable)
    assertThat(shadowDrawable.createdFromResId).isEqualTo(R.drawable.aac)
  }

  @Test fun createAudioCodecImageSetsAC3Drawable() {
    val view = imageInfographicUtils.createAudioCodecImage("ac3", context)
    val shadowDrawable = shadowOf(view!!.drawable)
    assertThat(shadowDrawable.createdFromResId).isEqualTo(R.drawable.ac3)
  }

  @Test fun createAudioCodecImageSetsAIFDrawable() {
    val view = imageInfographicUtils.createAudioCodecImage("aif", context)
    val shadowDrawable = shadowOf(view!!.drawable)
    assertThat(shadowDrawable.createdFromResId).isEqualTo(R.drawable.aif)
  }

  @Test fun createAudioCodecImageSetsAIFCDrawable() {
    val view = imageInfographicUtils.createAudioCodecImage("aifc", context)
    val shadowDrawable = shadowOf(view!!.drawable)
    assertThat(shadowDrawable.createdFromResId).isEqualTo(R.drawable.aifc)
  }

  @Test fun createAudioCodecImageSetsAIFFDrawable() {
    val view = imageInfographicUtils.createAudioCodecImage("aiff", context)
    val shadowDrawable = shadowOf(view!!.drawable)
    assertThat(shadowDrawable.createdFromResId).isEqualTo(R.drawable.aiff)
  }

  @Test fun createAudioCodecImageSetsAPEDrawable() {
    val view = imageInfographicUtils.createAudioCodecImage("ape", context)
    val shadowDrawable = shadowOf(view!!.drawable)
    assertThat(shadowDrawable.createdFromResId).isEqualTo(R.drawable.ape)
  }

  @Test fun createAudioCodecImageSetsAVCDrawable() {
    val view = imageInfographicUtils.createAudioCodecImage("avc", context)
    val shadowDrawable = shadowOf(view!!.drawable)
    assertThat(shadowDrawable.createdFromResId).isEqualTo(R.drawable.avc)
  }

  @Test fun createAudioCodecImageSetsCDDADrawable() {
    val view = imageInfographicUtils.createAudioCodecImage("cdda", context)
    val shadowDrawable = shadowOf(view!!.drawable)
    assertThat(shadowDrawable.createdFromResId).isEqualTo(R.drawable.cdda)
  }

  @Test fun createAudioCodecImageSetsDCADrawable() {
    val view = imageInfographicUtils.createAudioCodecImage("dca", context)
    val shadowDrawable = shadowOf(view!!.drawable)
    assertThat(shadowDrawable.createdFromResId).isEqualTo(R.drawable.dca)
  }

  @Test fun createAudioCodecImageSetsDTSDrawable() {
    val view = imageInfographicUtils.createAudioCodecImage("dts", context)
    val shadowDrawable = shadowOf(view!!.drawable)
    assertThat(shadowDrawable.createdFromResId).isEqualTo(R.drawable.dts)
  }

  @Test fun createAudioCodecImageSetsEAC3Drawable() {
    val view = imageInfographicUtils.createAudioCodecImage("eac3", context)
    val shadowDrawable = shadowOf(view!!.drawable)
    assertThat(shadowDrawable.createdFromResId).isEqualTo(R.drawable.eac3)
  }

  @Test fun createAudioCodecImageSetsFLACDrawable() {
    val view = imageInfographicUtils.createAudioCodecImage("flac", context)
    val shadowDrawable = shadowOf(view!!.drawable)
    assertThat(shadowDrawable.createdFromResId).isEqualTo(R.drawable.flac)
  }

  @Test fun createAudioCodecImageSetsMP1Drawable() {
    val view = imageInfographicUtils.createAudioCodecImage("mp1", context)
    val shadowDrawable = shadowOf(view!!.drawable)
    assertThat(shadowDrawable.createdFromResId).isEqualTo(R.drawable.mp1)
  }

  @Test fun createAudioCodecImageSetsMP2Drawable() {
    val view = imageInfographicUtils.createAudioCodecImage("mp2", context)
    val shadowDrawable = shadowOf(view!!.drawable)
    assertThat(shadowDrawable.createdFromResId).isEqualTo(R.drawable.mp2)
  }

  @Test fun createAudioCodecImageSetsMP3Drawable() {
    val view = imageInfographicUtils.createAudioCodecImage("mp3", context)
    val shadowDrawable = shadowOf(view!!.drawable)
    assertThat(shadowDrawable.createdFromResId).isEqualTo(R.drawable.mp3)
  }

  @Test fun createAudioCodecImageSetsOGGDrawable() {
    val view = imageInfographicUtils.createAudioCodecImage("ogg", context)
    val shadowDrawable = shadowOf(view!!.drawable)
    assertThat(shadowDrawable.createdFromResId).isEqualTo(R.drawable.ogg)
  }

  @Test fun createAudioCodecImageSetsWMADrawable() {
    val view = imageInfographicUtils.createAudioCodecImage("wma", context)
    val shadowDrawable = shadowOf(view!!.drawable)
    assertThat(shadowDrawable.createdFromResId).isEqualTo(R.drawable.wma)
  }

  @Test fun createAudioCodecImageDoesNotSetsWhenUnknown() {
    val view = imageInfographicUtils.createAudioCodecImage("zzz", context)
    assertThat(view).isNull()
  }

}