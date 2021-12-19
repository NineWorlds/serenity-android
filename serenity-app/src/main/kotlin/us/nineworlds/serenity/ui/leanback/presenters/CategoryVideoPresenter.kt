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

class CategoryVideoPresenter : Presenter() {


    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        val imageView = ImageCardView(parent.context)
        imageView.isFocusable = true
        imageView.isFocusableInTouchMode = true
        imageView.setBackgroundColor(ContextCompat.getColor(parent.context, R.color.holo_color))
        return CardPresenterViewHolder(imageView)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, item: Any) {
        val imageWidth = viewHolder.view.context.resources.getDimensionPixelSize(R.dimen.movie_poster_image_width)
        val imageHeight = viewHolder.view.context.resources.getDimensionPixelSize(R.dimen.movie_poster_image_height)

        val video = item as VideoCategory

        val cardHolder = viewHolder as CardPresenterViewHolder
        val imageCardView = cardHolder.cardView
        cardHolder.movie = video.item

        video.item.imageURL?.let {
            imageCardView.titleText = video.item.title
            imageCardView.contentText = video.item.studio
            imageCardView.setMainImageDimensions(imageWidth, imageHeight)
            imageCardView.setMainImageScaleType(ImageView.ScaleType.FIT_XY)
            cardHolder.updateCardViewImage(video.item.imageURL)
        }
    }

    override fun onUnbindViewHolder(viewHolder: ViewHolder) {
        val vh = viewHolder as CardPresenterViewHolder
        vh.reset()
    }

    inner class CardPresenterViewHolder(view: View) : Presenter.ViewHolder(view) {
        var movie: VideoContentInfo? = null
        val cardView: ImageCardView = view as ImageCardView

        fun reset() {
            cardView.badgeImage = null
            cardView.mainImage = null
        }

        fun updateCardViewImage(url: String) {
            GlideApp.with(view.context).load(url).fitCenter().into(cardView.mainImageView)
        }
    }

}
