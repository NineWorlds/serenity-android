package us.nineworlds.serenity.ui.leanback.presenters

import androidx.leanback.widget.Presenter
import us.nineworlds.serenity.core.model.impl.TVShowSeriesInfo
import us.nineworlds.serenity.databinding.LeanbackDetailsSummaryBinding
import android.widget.LinearLayout

import android.widget.RatingBar

import android.widget.TextView

import us.nineworlds.serenity.ui.util.ImageInfographicUtils

import android.view.ViewGroup
import android.graphics.drawable.BitmapDrawable

import android.graphics.Bitmap

import android.graphics.drawable.Drawable
import us.nineworlds.serenity.GlideApp
import us.nineworlds.serenity.R
import us.nineworlds.serenity.ui.util.ImageUtils

class SeriesPresenterViewHolder(private val binding: LeanbackDetailsSummaryBinding) : Presenter.ViewHolder(binding.root) {

    private val context = binding.root.context

    fun bind(videoInfo : TVShowSeriesInfo) {
        binding.movieBrowserPosterTitle.text = videoInfo.title
        binding.movieSummary.text = videoInfo.summary

        GlideApp.with(context).load(videoInfo.thumbNailURL).fitCenter().into(binding.videoPoster)

        val width = context.resources.getDimensionPixelSize(R.dimen.info_graphic_width)
        val height = context.resources.getDimensionPixelSize(R.dimen.info_graphic_height)

        val imageUtilsNormal = ImageInfographicUtils(width, height)

        val crv = imageUtilsNormal.createContentRatingImage(videoInfo.contentRating, context)
        val drawable: Drawable = crv.drawable
        val bmd = drawable as BitmapDrawable
        val bitmap = bmd.bitmap
        val bitmapResized = Bitmap.createScaledBitmap(bitmap, width, height, false)

        binding.movieBrowserPosterTitle.setCompoundDrawablesWithIntrinsicBounds(null, null,
                BitmapDrawable(context.resources, bitmapResized), null)

        createInfoGraphics(videoInfo)
    }

    fun createInfoGraphics(videoInfo: TVShowSeriesInfo) {
        binding.movieInfoGraphicLayout.removeAllViews()

        val normalHeight: Int = context.resources.getDimensionPixelSize(R.dimen.video_infogrpahic_height)
        val normalWidth: Int = context.resources.getDimensionPixelSize(R.dimen.video_infographic_width)
        val audioWidth: Int = context.resources.getDimensionPixelSize(R.dimen.audio_infographic_width)
        val audioHeight: Int = context.resources.getDimensionPixelSize(R.dimen.audio_inographic_height)

        val imageUtilsNormal = ImageInfographicUtils(normalWidth, normalHeight)
        val imageUtilsAudioChannel = ImageInfographicUtils(audioWidth, audioHeight)

        if (videoInfo.rating > 0) {
            val ratingBar = RatingBar(context, null, R.attr.ratingBarStyleIndicator)
            ratingBar.max = 4
            ratingBar.setIsIndicator(true)
            ratingBar.stepSize = 0.1f
            ratingBar.numStars = 4
            ratingBar.setPadding(0, 0, 0, 0)
            val params = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT)
            params.rightMargin = 15
            ratingBar.layoutParams = params
            val rating = videoInfo.rating
            ratingBar.rating = (rating / 2.5).toFloat()
            binding.movieInfoGraphicLayout.addView(ratingBar)
        }

        val studiov = imageUtilsNormal.createStudioImage(videoInfo.studio, context, videoInfo.mediaTagIdentifier)
        if (studiov != null) {
            binding.movieInfoGraphicLayout.addView(studiov)
        }

    }
}