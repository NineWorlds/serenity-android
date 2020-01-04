package us.nineworlds.serenity.ui.adapters

import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import toothpick.Toothpick
import us.nineworlds.serenity.R
import us.nineworlds.serenity.common.annotations.InjectionConstants
import us.nineworlds.serenity.core.model.ContentInfo
import us.nineworlds.serenity.core.model.SeriesContentInfo
import us.nineworlds.serenity.ui.adapters.viewholders.AbstractPosterAdapterDelegate
import us.nineworlds.serenity.ui.browser.tv.TVShowBrowserActivity
import us.nineworlds.serenity.ui.browser.tv.TVShowBrowserGalleryOnItemClickListener
import us.nineworlds.serenity.ui.browser.tv.TVShowGalleryOnItemSelectedListener
import us.nineworlds.serenity.ui.browser.tv.TVShowGridOnItemSelectedListener
import us.nineworlds.serenity.ui.browser.tv.TVShowViewHolder
import javax.inject.Inject

class TVSeriesAdapterDelegate : AbstractPosterAdapterDelegate<SeriesContentInfo, ContentInfo, TVShowViewHolder>() {

  @Inject
  lateinit var sharedPreferences: SharedPreferences

  private val posterLayoutActive: Boolean
  private val gridViewActive: Boolean

  init {
    val scope = Toothpick.openScope(InjectionConstants.APPLICATION_SCOPE)
    Toothpick.inject(this, scope)

    posterLayoutActive = sharedPreferences.getBoolean("series_layout_posters", false)
    gridViewActive = sharedPreferences.getBoolean(TVShowBrowserActivity.SERIES_LAYOUT_GRID, false)

    onItemClickListener = TVShowBrowserGalleryOnItemClickListener()
    onItemSelectedListener = if (gridViewActive) {
      TVShowGridOnItemSelectedListener()
    } else {
      TVShowGalleryOnItemSelectedListener()
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup): TVShowViewHolder {
    val view = LayoutInflater.from(parent.context)
      .inflate(R.layout.poster_tvshow_indicator_view, parent, false)
    return TVShowViewHolder(view)
  }

  override fun isForViewType(item: ContentInfo, items: MutableList<ContentInfo>, position: Int): Boolean = item is SeriesContentInfo

  override fun onBindViewHolder(item: SeriesContentInfo, holder: TVShowViewHolder, payloads: MutableList<Any>) {

    val context = holder.context
    val width = if (posterLayoutActive) {
      context.resources.getDimensionPixelSize(R.dimen.tvshow_poster_image_width)
    } else {
      context.resources.getDimensionPixelSize(R.dimen.banner_image_width)
    }
    val height = if (posterLayoutActive) {
      context.resources.getDimensionPixelSize(R.dimen.tvshow_poster_image_height)
    } else {
      context.resources.getDimensionPixelSize(R.dimen.banner_image_height)
    }

    holder.reset()
    holder.createImage(item, width, height, posterLayoutActive)
    holder.toggleWatchedIndicator(item)
    holder.getItemView().setOnClickListener { view: View? ->
      onItemViewClick(view, item)
    }
    holder.getItemView().onFocusChangeListener = OnFocusChangeListener { view: View, focus: Boolean ->
      onItemViewFocusChanged(focus, view, item)
    }
//    tvShowViewHolder.getItemView()
//      .setOnLongClickListener { view: View? -> onItemViewLongClick(view, position) }
    holder.getItemView().setOnKeyListener(this)
  }

//  protected fun onItemViewLongClick(view: View?, position: Int): Boolean {
//    val onItemLongClickListener = ShowOnItemLongClickListener(this)
//    onItemLongClickListener.setPosition(position)
//    return onItemLongClickListener.onLongClick(view)
//  }

  fun onItemViewClick(view: View?, item: ContentInfo) {
    onItemClickListener?.onItemClick(view, item)
  }

  fun onItemViewFocusChanged(hasFocus: Boolean, view: View, item: ContentInfo) {
    view.clearAnimation()
    view.background = null
    if (triggerFocusSelection) {
      if (hasFocus) {
        view.background = ContextCompat.getDrawable(
          view.context,
          R.drawable.rounded_transparent_border
        )
        onItemSelectedListener?.onItemSelected(view, item)
        return
      }
    }
  }
}