package us.nineworlds.serenity.ui.adapters

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import us.nineworlds.serenity.core.model.ContentInfo

class RecyclerViewDiffUtil(private val oldList: List<ContentInfo>?, private val newList: List<ContentInfo>) : DiffUtil.Callback() {
  override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
      oldList!![oldItemPosition].id() == newList[newItemPosition].id()

  override fun getOldListSize(): Int = oldList?.size ?: 0

  override fun getNewListSize(): Int = newList.size

  override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
      oldList!![oldItemPosition] == newList[newItemPosition]

  fun dispatchUpdatesTo(adapter: androidx.recyclerview.widget.RecyclerView.Adapter<*>) {
    val diffResult = DiffUtil.calculateDiff(this)
    diffResult.dispatchUpdatesTo(adapter)
  }
}