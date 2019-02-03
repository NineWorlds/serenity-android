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

package us.nineworlds.serenity.ui.leanback.search

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.support.annotation.VisibleForTesting
import android.support.v17.leanback.widget.ImageCardView
import android.support.v17.leanback.widget.Presenter
import android.support.v4.content.ContextCompat
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import us.nineworlds.serenity.R
import us.nineworlds.serenity.core.model.VideoContentInfo
import us.nineworlds.serenity.ui.util.ImageUtils

class CardPresenter(private val context: Context) : Presenter() {

  val imageWidth = context.resources.getDimensionPixelSize(R.dimen.search_card_image_width)
  val imageHeight = context.resources.getDimensionPixelSize(R.dimen.search_card_image_height)

  override fun onCreateViewHolder(parent: ViewGroup): Presenter.ViewHolder {
    val imageView = createImageView()
    imageView.isFocusable = true
    imageView.isFocusableInTouchMode = true
    imageView.setBackgroundColor(ContextCompat.getColor(context, R.color.holo_color))
    return CardPresenterViewHolder(imageView)
  }

  internal fun createImageView(): ImageCardView = ImageCardView(context)

  override fun onBindViewHolder(viewHolder: Presenter.ViewHolder, item: Any) {
    val video = item as VideoContentInfo

    val cardHolder = viewHolder as CardPresenterViewHolder
    val imageCardView = cardHolder.cardView
    cardHolder.movie = video

    video.imageURL?.let {
      imageCardView.titleText = video.title
      imageCardView.contentText = video.studio
      val activity = getActivity(context)
      activity?.let {
        imageCardView.setMainImageDimensions(imageWidth, imageHeight)
        imageCardView.setMainImageScaleType(ImageView.ScaleType.FIT_XY)
        cardHolder.updateCardViewImage(video.imageURL)
      }
    }
  }

  internal fun getActivity(contextWrapper: Context): Activity? {
    var context = contextWrapper
    while (context is ContextWrapper) {
      if (context is Activity) {
        return context
      }
      context = context.baseContext
    }
    return null
  }

  override fun onUnbindViewHolder(viewHolder: Presenter.ViewHolder) {
    val vh = viewHolder as CardPresenterViewHolder
    vh.reset()
  }

  @VisibleForTesting
  inner class CardPresenterViewHolder(view: View) : Presenter.ViewHolder(view) {
    var movie: VideoContentInfo? = null
    val cardView: ImageCardView = view as ImageCardView

    fun reset() {
      cardView.badgeImage = null
      cardView.mainImage = null
    }

    fun updateCardViewImage(url: String) {
      Glide.with(context).load(url).fitCenter().into(cardView.mainImageView)
    }
  }
}
