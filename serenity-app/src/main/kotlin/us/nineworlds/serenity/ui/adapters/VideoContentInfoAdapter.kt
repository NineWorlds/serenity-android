package us.nineworlds.serenity.ui.adapters

import androidx.recyclerview.widget.DiffUtil
import com.hannesdorfmann.adapterdelegates4.AdapterDelegate
import us.nineworlds.serenity.core.model.ContentInfo

class VideoContentInfoAdapter : AsyncListDifferDelegationAdapter<ContentInfo>(ContentInfoListDiffer()) {

  init {
    delegatesManager.fallbackDelegate = MoviePosterAdapterDelegate()
  }

  class ContentInfoListDiffer : DiffUtil.ItemCallback<ContentInfo>() {
    override fun areItemsTheSame(oldItem: ContentInfo, newItem: ContentInfo): Boolean = oldItem.id() == newItem.id()

    override fun areContentsTheSame(oldItem: ContentInfo, newItem: ContentInfo): Boolean = oldItem.id() == newItem.id()
  }

  fun getItemAtPosition(position: Int) : ContentInfo = differ.currentList[position]

  fun addDelegates(delegates: List<AdapterDelegate<List<ContentInfo>>>) {
    for (delegate in delegates) {
      delegatesManager.addDelegate(delegate)

    }
  }

}