/**
 * The MIT License (MIT)
 * Copyright (c) 2012 David Carver
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS
 * OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
 * OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF
 * OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package us.nineworlds.serenity.ui.util

import android.app.Activity
import android.content.Context
import android.graphics.Typeface
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup.LayoutParams
import android.widget.ImageView
import android.widget.ImageView.ScaleType
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import us.nineworlds.serenity.R
import us.nineworlds.serenity.common.rest.SerenityClient
import us.nineworlds.serenity.core.model.VideoContentInfo
import us.nineworlds.serenity.core.util.TimeUtil
import us.nineworlds.serenity.injection.BaseInjector
import us.nineworlds.serenity.ui.listeners.AbstractVideoOnItemSelectedListener
import javax.inject.Inject

class ImageInfographicUtils(private val width: Int, private val height: Int) : BaseInjector() {

  @Inject lateinit var factory: SerenityClient

  @Inject lateinit var timeUtil: TimeUtil

  fun createAudioCodecImage(codec: String, context: Context): ImageView? {
    val v = ImageView(context)
    v.scaleType = ScaleType.FIT_XY
    val w = ImageUtils.getDPI(width, v.context as Activity)
    val h = ImageUtils.getDPI(height, v.context as Activity)
    v.layoutParams = LayoutParams(w, h)

    when (codec) {
      "aac" -> v.setImageResource(R.drawable.aac)
      "ac3" -> v.setImageResource(R.drawable.ac3)
      "aif" -> v.setImageResource(R.drawable.aif)
      "aifc" -> v.setImageResource(R.drawable.aifc)
      "aiff" -> v.setImageResource(R.drawable.aiff)
      "ape" -> v.setImageResource(R.drawable.ape)
      "avc" -> v.setImageResource(R.drawable.avc)
      "cdda" -> v.setImageResource(R.drawable.cdda)
      "dca" -> v.setImageResource(R.drawable.dca)
      "dts" -> v.setImageResource(R.drawable.dts)
      "eac3" -> v.setImageResource(R.drawable.eac3)
      "flac" -> v.setImageResource(R.drawable.flac)
      "mp1" -> v.setImageResource(R.drawable.mp1)
      "mp2" -> v.setImageResource(R.drawable.mp2)
      "mp3" -> v.setImageResource(R.drawable.mp3)
      "ogg" -> v.setImageResource(R.drawable.ogg)
      "wma" -> v.setImageResource(R.drawable.wma)
      else -> return null
    }

    return v
  }

  fun createVideoResolutionImage(res: String, context: Context): ImageView? {
    val v = ImageView(context)
    v.scaleType = ScaleType.FIT_XY
    val w = ImageUtils.getDPI(width, v.context as Activity)
    val h = ImageUtils.getDPI(height, v.context as Activity)
    v.layoutParams = LayoutParams(w, h)

    when (res.toLowerCase()) {
      "sd", "480" -> v.setImageResource(R.drawable.res480)
      "576" -> v.setImageResource(R.drawable.res576)
      "720" -> v.setImageResource(R.drawable.res720)
      "1080" -> v.setImageResource(R.drawable.res1080)
      "hd" -> v.setImageResource(R.drawable.hd)
      else -> return null
    }

    return v
  }

  fun createAspectRatioImage(ratio: String, context: Context): ImageView? {
    val v = ImageView(context)
    v.scaleType = ScaleType.FIT_XY
    val w = ImageUtils.getDPI(width, v.context as Activity)
    val h = ImageUtils.getDPI(height, v.context as Activity)
    v.layoutParams = LayoutParams(w, h)

    when(ratio) {
      "1.33" -> v.setImageResource(R.drawable.aspect_1_33)
      "1.66" -> v.setImageResource(R.drawable.aspect_1_66)
      "1.78" -> v.setImageResource(R.drawable.aspect_1_78)
      "1.85" -> v.setImageResource(R.drawable.aspect_1_85)
      "2.20" -> v.setImageResource(R.drawable.aspect_2_20)
      "2.35" -> v.setImageResource(R.drawable.aspect_2_35)
      else -> return null
    }

    return v
  }

  fun createContentRatingImage(contentRating: String, context: Context): ImageView {
    val v = ImageView(context)
    v.scaleType = ScaleType.FIT_XY
    val w = ImageUtils.getDPI(width, v.context as Activity)
    val h = ImageUtils.getDPI(height, v.context as Activity)
    v.layoutParams = LayoutParams(w, h)

    when(contentRating) {
      "G" -> v.setImageResource(R.drawable.mpaa_g)
      "PG" -> v.setImageResource(R.drawable.mpaa_pg)
      "PG-13" -> v.setImageResource(R.drawable.mpaa_pg13)
      "R" -> v.setImageResource(R.drawable.mpaa_r)
      "NC-17" -> v.setImageResource(R.drawable.mpaa_nc17)
      else -> v.setImageResource(R.drawable.mpaa_nr)
    }

    return v
  }

  fun createVideoCodec(codec: String, context: Context): ImageView? {
    val v = ImageView(context)
    v.scaleType = ScaleType.FIT_XY
    val w = ImageUtils.getDPI(width, v.context as Activity)
    val h = ImageUtils.getDPI(height, v.context as Activity)
    v.layoutParams = LayoutParams(w, h)

    when(codec.toLowerCase()) {
      "divx" -> v.setImageResource(R.drawable.divx)
      "div3" -> v.setImageResource(R.drawable.div3)
      "vc-1" -> v.setImageResource(R.drawable.vc_1)
      "h264", "mpeg4" -> v.setImageResource(R.drawable.h264)
      "mpeg2" -> v.setImageResource(R.drawable.mpeg2video)
      "mpeg1" -> v.setImageResource(R.drawable.mpeg1video)
      "xvid" -> v.setImageResource(R.drawable.xvid)
      else -> return null
    }

    return v
  }

  fun createTVContentRating(contentRating: String, context: Context): ImageView {
    val v = ImageView(context)
    v.scaleType = ScaleType.FIT_XY
    val w = ImageUtils.getDPI(width, v.context as Activity)
    val h = ImageUtils.getDPI(height, v.context as Activity)
    v.layoutParams = LayoutParams(w, h)

    when(contentRating) {
      "TV-G" -> v.setImageResource(R.drawable.tvg)
      "TV-PG" -> v.setImageResource(R.drawable.tvpg)
      "TV-14" -> v.setImageResource(R.drawable.tv14)
      "TV-MA" -> v.setImageResource(R.drawable.tvma)
      "TV-Y" -> v.setImageResource(R.drawable.tvy)
      "TV-Y7" -> v.setImageResource(R.drawable.tvy7)
      else -> v.setImageResource(R.drawable.tvnr)
    }

    return v
  }

  fun createAudioChannlesImage(channels: String, context: Context): ImageView? {
    val v = ImageView(context)
    v.scaleType = ScaleType.FIT_XY
    val w = ImageUtils.getDPI(width, v.context as Activity)
    val h = ImageUtils.getDPI(height, v.context as Activity)
    v.layoutParams = LayoutParams(w, h)

    when(channels.toLowerCase()) {
      "0" -> v.setImageResource(R.drawable.audio_0)
      "1" -> v.setImageResource(R.drawable.audio_1)
      "2" -> v.setImageResource(R.drawable.audio_2)
      "6" -> v.setImageResource(R.drawable.audio_6)
      "8" -> v.setImageResource(R.drawable.audio_8)
      else -> return null
    }

    return v
  }

  fun createStudioImage(studio: String?, context: Context, identifier: String): ImageView? {
    if (studio == null) {
      return null
    }

    val v = ImageView(context)
    v.scaleType = ScaleType.FIT_XY
    val w = ImageUtils.getDPI(width, v.context as Activity)
    val h = ImageUtils.getDPI(height, v.context as Activity)
    v.layoutParams = LayoutParams(w, h)
    val mediaTagUrl = factory!!.createMediaTagURL("studio", studio, identifier)
    Glide.with(context).load(mediaTagUrl).into(v)
    return v
  }

  fun createDurationView(duration: Long, context: Context): TextView {
    val tv = TextView(context)
    tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 19f)
    tv.text = timeUtil!!.formatDurationHoursMinutes(duration)
    tv.typeface = Typeface.DEFAULT_BOLD
    val params = LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
    params.rightMargin = 10
    params.gravity = Gravity.CENTER_VERTICAL
    tv.layoutParams = params
    tv.gravity = Gravity.CENTER_VERTICAL or Gravity.RIGHT
    return tv
  }

  companion object {

    fun setWatchedCount(epiv: View, a: Activity, info: VideoContentInfo) {
      val watchedView = a.findViewById<ImageView>(AbstractVideoOnItemSelectedListener.WATCHED_VIEW_ID)
      watchedView?.setImageResource(R.drawable.watched_small)
    }

    fun setUnwatched(epiv: View, a: Activity, info: VideoContentInfo) {
      val watchedView = a.findViewById<ImageView>(AbstractVideoOnItemSelectedListener.WATCHED_VIEW_ID)
      watchedView?.setImageResource(R.drawable.unwatched_small)
    }
  }
}
