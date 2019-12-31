package us.nineworlds.serenity.ui.home

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import us.nineworlds.serenity.R

class MainMenuViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {

  @BindView(R.id.main_menu_item)
  lateinit var mainMenuTextView: TextView

  init {
    ButterKnife.bind(this, itemView!!)
  }
}