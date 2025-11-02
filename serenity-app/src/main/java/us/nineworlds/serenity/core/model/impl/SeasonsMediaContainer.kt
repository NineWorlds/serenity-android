package us.nineworlds.serenity.core.model.impl

import us.nineworlds.serenity.common.media.model.IMediaContainer
import us.nineworlds.serenity.core.model.VideoContentInfo

class SeasonsMediaContainer(mc: IMediaContainer) : SeriesMediaContainer(mc) {

  override fun createSeriesInfo() {
    val baseUrl = factory.baseURL()
    val shows = mc.directories ?: return

    for (show in shows) {
      val mpi = TVShowSeriesInfo().apply {
        setId(show.ratingKey)
        parentTitle = mc.title2
        setBackgroundURL(mc.art?.let { "$baseUrl${it.replaceFirst("/", "")}" } ?: "$baseUrl:/resources/show-fanart.jpg")
        setImageURL(show.thumb?.let { "$baseUrl${it.replaceFirst("/", "")}" } ?: "")
        key = show.key
        setTitle(show.title)
        showsWatched = show.viewedLeafCount
        val totalEpisodes = show.leafCount?.toInt() ?: 0
        val unwatched = totalEpisodes - (show.viewedLeafCount?.toInt() ?: 0)
        showsUnwatched = unwatched.toString()
      }
      videoList!!.add(mpi)
    }
  }
}
