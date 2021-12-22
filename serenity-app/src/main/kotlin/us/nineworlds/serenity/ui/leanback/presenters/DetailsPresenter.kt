package us.nineworlds.serenity.ui.leanback.presenters

import androidx.leanback.widget.AbstractDetailsDescriptionPresenter
import us.nineworlds.serenity.core.model.ContentInfo

class DetailsPresenter : AbstractDetailsDescriptionPresenter() {

    override fun onBindDescription(vh: ViewHolder, item: Any) {
        val videoInfo = item as ContentInfo
        vh.title.text = videoInfo.title
        vh.body.text = videoInfo.summary
    }

}