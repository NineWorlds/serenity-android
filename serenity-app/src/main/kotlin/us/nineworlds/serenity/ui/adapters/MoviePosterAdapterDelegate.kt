package us.nineworlds.serenity.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import us.nineworlds.serenity.R
import us.nineworlds.serenity.core.model.ContentInfo
import us.nineworlds.serenity.core.model.impl.MoviePosterInfo
import us.nineworlds.serenity.ui.adapters.viewholders.AbstractPosterAdapterDelegate
import us.nineworlds.serenity.ui.browser.movie.MoviePosterOnItemSelectedListener
import us.nineworlds.serenity.ui.browser.movie.MoviePosterViewHolder
import us.nineworlds.serenity.ui.listeners.GalleryVideoOnItemClickListener
import us.nineworlds.serenity.ui.views.statusoverlayview.StatusOverlayFrameLayout

class MoviePosterAdapterDelegate : AbstractPosterAdapterDelegate<MoviePosterInfo, ContentInfo, MoviePosterViewHolder>() {

  init {
    onItemClickListener = GalleryVideoOnItemClickListener()
    onItemSelectedListener = MoviePosterOnItemSelectedListener()
  }

  override fun onCreateViewHolder(parent: ViewGroup): MoviePosterViewHolder {
    val view = LayoutInflater.from(parent.context)
      .inflate(R.layout.movie_status_overlay, parent, false)
    return MoviePosterViewHolder(view)
  }

  override fun isForViewType(item: ContentInfo, items: MutableList<ContentInfo>, position: Int): Boolean
      = item is MoviePosterInfo

  override fun onBindViewHolder(item: MoviePosterInfo, holder: MoviePosterViewHolder, payloads: MutableList<Any>) {
    val overlayView = holder.itemView as StatusOverlayFrameLayout
    holder.isZoomedOut = false

    val width =
      overlayView.context.resources.getDimensionPixelSize(R.dimen.movie_poster_image_width)
    val height =
      overlayView.context.resources.getDimensionPixelSize(R.dimen.movie_poster_image_height)

    overlayView.tag = item
    overlayView.reset()
    overlayView.createImage(item, width, height)
    overlayView.toggleWatchedIndicator(item)
    overlayView.isClickable = true
    overlayView.setOnClickListener { view: View? -> onItemViewClick(view, item) }
//    overlayView.setOnLongClickListener { view: View? ->
//      onItemViewLongClick(
//        view,
//        item
//      )
//    }
    overlayView.onFocusChangeListener = OnFocusChangeListener { view: View, hasFocus: Boolean ->
      onItemViewFocusChanged(hasFocus, view, item)
    }
    overlayView.setOnKeyListener(this)
  }

  fun onItemViewClick(view: View?, item: MoviePosterInfo) {
   onItemClickListener?.onItemClick(view, item)
  }

  fun onItemViewFocusChanged(hasFocus: Boolean, view: View, item: MoviePosterInfo) {
    view.clearAnimation()
    view.background = null
    if (triggerFocusSelection) {
      if (hasFocus) {
        view.background = ContextCompat.getDrawable(
          view.context,
          R.drawable.rounded_transparent_border
        )
        zoomOut(view)
        onItemSelectedListener?.onItemSelected(view, item)
      }
    }
  }

//  private fun onItemViewLongClick(view: View, position: Int): Boolean {
//    val onItemLongClickListener = GalleryVideoOnItemLongClickListener(this)
//    onItemLongClickListener.setPosition(position)
//    return onItemLongClickListener.onLongClick(view)
//  }
}