package us.nineworlds.serenity.ui.leanback.presenters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.leanback.widget.Presenter
import us.nineworlds.serenity.core.model.impl.TVShowSeriesInfo
import us.nineworlds.serenity.databinding.LeanbackDetailsSummaryBinding

open class SeriesPresenter : Presenter() {

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        val layOutParams = parent.layoutParams
        layOutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
        parent.layoutParams = layOutParams

        val layoutInflater = LayoutInflater.from(parent.context)
        return SeriesPresenterViewHolder(LeanbackDetailsSummaryBinding.inflate(layoutInflater, parent, false))
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, item: Any) {
        val vh = viewHolder as SeriesPresenterViewHolder
        vh.bind(item as TVShowSeriesInfo)
    }

    override fun onUnbindViewHolder(viewHolder: ViewHolder?) = Unit
}