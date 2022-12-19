package us.nineworlds.serenity.ui.leanback.presenters

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.leanback.widget.RowPresenter
import us.nineworlds.serenity.GlideApp
import us.nineworlds.serenity.R
import us.nineworlds.serenity.core.model.VideoContentInfo
import us.nineworlds.serenity.databinding.LeanbackDetailsSummaryBinding
import us.nineworlds.serenity.ui.util.ImageInfographicUtils
import android.widget.LinearLayout

import android.widget.RatingBar
import android.view.ViewGroup


class MoviePresenterViewHolder(private val binding: LeanbackDetailsSummaryBinding) : RowPresenter.ViewHolder(binding.root) {

    private val context = binding.root.context

    fun bind(videoInfo: VideoContentInfo) {
        binding.movieBrowserPosterTitle.text = videoInfo.title
        binding.movieSummary.text = videoInfo.summary

        GlideApp.with(context).load(videoInfo.imageURL).fitCenter().into(binding.videoPoster)

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

        createInfoGraphic(videoInfo)
    }

    fun createInfoGraphic(videoInfo: VideoContentInfo) {
        val infographicsView = binding.movieInfoGraphicLayout
        infographicsView.removeAllViews()

        val normalHeight = context.resources.getDimensionPixelSize(us.nineworlds.serenity.R.dimen.video_infogrpahic_height)
        val normalWidth = context.resources.getDimensionPixelSize(R.dimen.video_infographic_width)
        val audioWidth = context.resources.getDimensionPixelSize(R.dimen.audio_infographic_width)
        val audioHeight = context.resources.getDimensionPixelSize(R.dimen.audio_inographic_height)

        val imageUtilsNormal = ImageInfographicUtils(normalWidth, normalHeight)
        val imageUtilsAudioChannel = ImageInfographicUtils(audioWidth, audioHeight)

        val durationView = imageUtilsNormal.createDurationView(videoInfo.duration.toLong(), context)
        if (durationView != null) {
            infographicsView.addView(durationView)
        }

        val resv = imageUtilsNormal.createVideoCodec(videoInfo.videoCodec, context)
        if (resv != null) {
            infographicsView.addView(resv)
        }

        val resolution = imageUtilsNormal.createVideoResolutionImage(videoInfo.videoResolution, context)
        if (resolution != null) {
            infographicsView.addView(resolution)
        }

        val aspectv = imageUtilsNormal.createAspectRatioImage(videoInfo.aspectRatio, context)
        if (aspectv != null) {
            infographicsView.addView(aspectv)
        }

        val acv = imageUtilsNormal.createAudioCodecImage(videoInfo.audioCodec, context)
        if (acv != null) {
            infographicsView.addView(acv)
        }

        val achannelsv = imageUtilsAudioChannel.createAudioChannlesImage(videoInfo.audioChannels, context)
        if (achannelsv != null) {
            infographicsView.addView(achannelsv)
        }

        if (videoInfo.rating > 0) {
            val ratingBar = RatingBar(context, null, android.R.attr.ratingBarStyleIndicator)
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
            infographicsView.addView(ratingBar)
        }

        val studiov = imageUtilsNormal.createStudioImage(videoInfo.studio, context, videoInfo.mediaTagIdentifier)
        if (studiov != null) {
            infographicsView.addView(studiov)
        }
    }

}