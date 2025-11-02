package us.nineworlds.serenity.core.model.impl

import us.nineworlds.serenity.common.media.model.IMediaContainer

class SeasonsMediaContainer(mc: IMediaContainer) : SeriesMediaContainer(mc) {

  override fun createSeriesInfo() {
    val baseUrl = factory.baseURL()
    val shows = mc.directories ?: return

    for (show in shows) {
      val mpi = TVShowSeriesInfo().apply {
        id = show.ratingKey
        parentTitle = mc.title2
        backgroundURL = mc.art?.let { "$baseUrl${it.replaceFirst("/", "")}" } ?: "$baseUrl:/resources/show-fanart.jpg"
        imageURL = show.thumb?.let { "$baseUrl${it.replaceFirst("/", "")}" } ?: ""
        key = show.key
        title = show.title
        showsWatched = show.viewedLeafCount
        val totalEpisodes = show.leafCount?.toInt() ?: 0
        val unwatched = totalEpisodes - (show.viewedLeafCount?.toInt() ?: 0)
        showsUnwatched = unwatched.toString()
      }
      videoList!!.add(mpi)
    }
  }
}
