package us.nineworlds.serenity.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import timber.log.Timber
import us.nineworlds.serenity.R
import us.nineworlds.serenity.core.model.ContentInfo
import us.nineworlds.serenity.core.model.SeriesContentInfo
import us.nineworlds.serenity.ui.adapters.viewholders.AbstractPosterAdapterDelegate
import us.nineworlds.serenity.ui.browser.tv.TVShowViewHolder
import us.nineworlds.serenity.ui.browser.tv.seasons.SeasonViewHolder
import us.nineworlds.serenity.ui.browser.tv.seasons.TVShowSeasonOnItemClickListener
import us.nineworlds.serenity.ui.browser.tv.seasons.TVShowSeasonOnItemSelectedListener

class TVSeasonsAdapterDelegate : AbstractPosterAdapterDelegate<SeriesContentInfo, ContentInfo, TVShowViewHolder>() {

  init {
    onItemSelectedListener = TVShowSeasonOnItemSelectedListener()
  }

  override fun onCreateViewHolder(parent: ViewGroup): TVShowViewHolder {
    val view =
      LayoutInflater.from(parent.context).inflate(R.layout.poster_tvshow_indicator_view, null)
    return SeasonViewHolder(view)
  }

  override fun isForViewType(item: ContentInfo, items: MutableList<ContentInfo>, position: Int): Boolean =
    item is SeriesContentInfo

  override fun onBindViewHolder(item: SeriesContentInfo, holder: TVShowViewHolder, payloads: MutableList<Any>) {
    val viewHolder = holder as SeasonViewHolder

    val context = viewHolder.context
    val width = context.resources.getDimensionPixelSize(R.dimen.seasons_image_width)
    val height = context.resources.getDimensionPixelSize(R.dimen.seasons_image_height)

    viewHolder.reset()


    Timber.e("Poster Image url: ${item.thumbNailURL}")
    viewHolder.createImage(item, width, height, true)
    viewHolder.toggleWatchedIndicator(item)
    viewHolder.getItemView().setOnClickListener { view: View? ->
      onItemViewClick(
        view!!,
        item
      )
    }
    viewHolder.getItemView().onFocusChangeListener = OnFocusChangeListener { view: View?, focus: Boolean ->
      onItemViewFocusChanged(
        focus,
        view!!,
        item
      )
    }
//    viewHolder.getItemView()
//      .setOnLongClickListener { view: View -> onItemViewLongClick(view, position) }
  }

//  private fun onItemViewLongClick(view: View, position: Int): Boolean {
//    val seasonOnItemLongClickListener = SeasonOnItemLongClickListener(this)
//    seasonOnItemLongClickListener.setPosition(position)
//    return seasonOnItemLongClickListener.onLongClick(view)
//  }

  fun onItemViewClick(view: View, item: ContentInfo) {
    onItemClickListener = createOnItemClickListener()
    onItemClickListener?.onItemClick(view, item)
  }

  protected fun createOnItemClickListener(): TVShowSeasonOnItemClickListener {
    return TVShowSeasonOnItemClickListener()
  }

  fun onItemViewFocusChanged(hasFocus: Boolean, view: View, item: ContentInfo) {
    view.clearAnimation()
    view.background = null
    if (triggerFocusSelection) {
      if (hasFocus) {
        view.background = ContextCompat.getDrawable(view.context, R.drawable.rounded_transparent_border)
        zoomOut(view)
        onItemSelectedListener?.onItemSelected(view, item)
      }
    }
  }

}