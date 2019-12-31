package us.nineworlds.serenity.ui.home

import android.graphics.Typeface
import android.text.TextUtils.TruncateAt
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import us.nineworlds.serenity.GalleryOnItemClickListener
import us.nineworlds.serenity.GalleryOnItemSelectedListener
import us.nineworlds.serenity.R
import us.nineworlds.serenity.core.menus.MenuItem

class MainMenuDelegatingAdapter(
  private val onItemClickListener: GalleryOnItemClickListener,
  private val onItemSelectedListener: GalleryOnItemSelectedListener
) : AbsListItemAdapterDelegate<MenuItem, MenuItem, MainMenuViewHolder>() {

  override fun onCreateViewHolder(parent: ViewGroup): MainMenuViewHolder {
    val mainMenuTextView = LayoutInflater.from(parent.context).inflate(
      R.layout.item_mainmenu,
      parent,
      false
    )
    return MainMenuViewHolder(mainMenuTextView)
  }

  override fun isForViewType(item: MenuItem, items: MutableList<MenuItem>, position: Int): Boolean = true

  override fun onBindViewHolder(item: MenuItem, holder: MainMenuViewHolder, payloads: MutableList<Any>) {
    setDefaults(item.title, holder.mainMenuTextView)
    holder.itemView.onFocusChangeListener =
      OnFocusChangeListener { view: View?, hasFocus: Boolean ->
        onItemSelectedListener.onItemSelected(view, hasFocus, item)
      }
    holder.itemView.setOnClickListener { view: View? ->
      onItemClickListener.onItemClick(view, item)
    }
  }

  fun setDefaults(title: String?, v: TextView) {
    v.text = title
    v.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 35f)
    v.setTypeface(Typeface.DEFAULT, Typeface.BOLD)
    v.gravity = Gravity.CENTER_VERTICAL
    v.setLines(1)
    v.setHorizontallyScrolling(true)
    v.ellipsize = TruncateAt.MARQUEE
    v.layoutParams = FrameLayout.LayoutParams(
      ViewGroup.LayoutParams.WRAP_CONTENT,
      ViewGroup.LayoutParams.MATCH_PARENT
    )
  }
}