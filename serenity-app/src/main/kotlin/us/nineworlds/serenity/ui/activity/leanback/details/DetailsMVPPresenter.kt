package us.nineworlds.serenity.ui.activity.leanback.details

import kotlinx.coroutines.launch
import moxy.MvpPresenter
import moxy.presenterScope
import toothpick.Toothpick
import us.nineworlds.serenity.common.annotations.InjectionConstants
import us.nineworlds.serenity.core.model.impl.MovieMediaContainer
import us.nineworlds.serenity.core.model.impl.SeriesMediaContainer
import us.nineworlds.serenity.core.model.impl.TVShowSeriesInfo
import us.nineworlds.serenity.core.repository.VideoRepository
import javax.inject.Inject

class DetailsMVPPresenter : MvpPresenter<DetailsView>() {

    @Inject
    lateinit var repository: VideoRepository

    init {
        Toothpick.inject(this, Toothpick.openScope(InjectionConstants.APPLICATION_SCOPE));
    }

    fun loadItem(itemId: String, type: String) {
        presenterScope.launch {
            val result = repository.fetchItemById(itemId)
            when (type) {
                "tvshows" -> viewState.updateDetails(SeriesMediaContainer(result).createSeries()[0])
                "movies" -> viewState.updateDetails(MovieMediaContainer(result).createVideos()[0])
            }
        }
    }

}