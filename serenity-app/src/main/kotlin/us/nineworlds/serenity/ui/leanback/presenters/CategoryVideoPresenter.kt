package us.nineworlds.serenity.ui.leanback.presenters

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.leanback.widget.ImageCardView
import androidx.leanback.widget.Presenter
import us.nineworlds.serenity.GlideApp
import us.nineworlds.serenity.R
import us.nineworlds.serenity.core.model.VideoCategory
import us.nineworlds.serenity.core.model.VideoContentInfo
import us.nineworlds.serenity.ui.views.statusoverlayview.StatusOverlayFrameLayout

class CategoryVideoPresenter : Presenter() {

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        val statusOverlayView = StatusOverlayFrameLayout(parent.context)
        statusOverlayView.isFocusable = true
        statusOverlayView.isFocusableInTouchMode = true
        statusOverlayView.setBackgroundColor(ContextCompat.getColor(parent.context, android.R.color.transparent))
        return CardPresenterViewHolder(statusOverlayView)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, item: Any) {
        val video = item as VideoCategory

        val cardHolder = viewHolder as CardPresenterViewHolder

        cardHolder.bind(video.item)
    }

    override fun onUnbindViewHolder(viewHolder: ViewHolder) {
        val vh = viewHolder as CardPresenterViewHolder
        vh.reset()
    }

    inner class CardPresenterViewHolder(view: View) : Presenter.ViewHolder(view) {
        val cardView: StatusOverlayFrameLayout = view as StatusOverlayFrameLayout

        fun reset() {
            cardView.reset()
        }

        fun bind(videoContentInfo: VideoContentInfo) {
            cardView.tag = videoContentInfo

            videoContentInfo.imageURL?.let {
                val imageWidth = view.context.resources.getDimensionPixelSize(R.dimen.movie_poster_image_width)
                val imageHeight = view.context.resources.getDimensionPixelSize(R.dimen.movie_poster_image_height)
                cardView.createImage(videoContentInfo, imageWidth, imageHeight)
            }
            cardView.toggleWatchedIndicator(videoContentInfo)
        }

    }
}
