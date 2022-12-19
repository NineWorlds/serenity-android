package us.nineworlds.serenity.ui.leanback.presenters

import android.view.LayoutInflater
import android.view.ViewGroup
import us.nineworlds.serenity.core.model.VideoContentInfo
import us.nineworlds.serenity.databinding.LeanbackDetailsSummaryBinding

class MoviePresenter : SeriesPresenter() {

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = LeanbackDetailsSummaryBinding.inflate(layoutInflater, parent, false)
        return MoviePresenterViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, item: Any) {
        val videoInfo = item as VideoContentInfo
        val vh = viewHolder as MoviePresenterViewHolder

        vh.bind(videoInfo)
    }

}