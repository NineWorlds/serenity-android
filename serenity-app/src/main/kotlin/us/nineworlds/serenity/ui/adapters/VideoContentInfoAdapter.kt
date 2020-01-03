package us.nineworlds.serenity.ui.adapters

import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import us.nineworlds.serenity.core.model.ContentInfo

class VideoContentInfoAdapter : ListDelegationAdapter<List<ContentInfo>>() {

  init {
    delegatesManager.addDelegate(MoviePosterAdapterDelegate())
  }
}