package us.nineworlds.serenity.ui.adapters

import androidx.recyclerview.widget.DiffUtil
import us.nineworlds.serenity.core.model.ContentInfo

class VideoContentInfoAdapter : AsyncListDifferDelegationAdapter<ContentInfo>(ContentInfoListDiffer()) {

  init {
    delegatesManager.addDelegate(MoviePosterAdapterDelegate())
  }

  class ContentInfoListDiffer : DiffUtil.ItemCallback<ContentInfo>() {
    override fun areItemsTheSame(oldItem: ContentInfo, newItem: ContentInfo): Boolean = oldItem.id() == newItem.id()

    override fun areContentsTheSame(oldItem: ContentInfo, newItem: ContentInfo): Boolean = oldItem.id() == newItem.id()
  }

}