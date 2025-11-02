package us.nineworlds.serenity.core.model.impl

import us.nineworlds.serenity.common.media.model.IDirectory
import us.nineworlds.serenity.common.media.model.IMediaContainer
import us.nineworlds.serenity.core.model.SeriesContentInfo

class SeriesMediaContainer(mc: IMediaContainer) : AbstractMediaContainer(mc) {

  fun createSeries(): List<SeriesContentInfo> {
    videoList = mutableListOf()
    createSeriesInfo()
    return videoList as List<SeriesContentInfo>
  }

  private fun createSeriesInfo() {
    val baseUrl = factory.baseURL()
    if (mc.size > 0) {
      val mediaTagId = mc.mediaTagVersion.toString()
      val shows = mc.directories ?: return

      for (show in shows) {
        val mpi = TVShowSeriesInfo().apply {
          id = show.key
          key = show.key
          mediaTagIdentifier = mediaTagId
          summary = show.summary
          studio = show.studio
          rating = show.rating?.toDouble() ?: 0.0
          backgroundURL = show.art?.let { "$baseUrl${it.replaceFirst("/", "")}" } ?: "$baseUrl:/resources/show-fanart.jpg"
          imageURL = show.banner?.let { "$baseUrl${it.replaceFirst("/", "")}" } ?: ""
          thumbNailURL = show.thumb?.let { "$baseUrl${it.replaceFirst("/", "")}" } ?: ""
          title = show.title
          contentRating = show.contentRating
          generes = processGeneres(show)

          val totalEpisodes = show.leafCount?.toInt() ?: 0
          val viewedEpisodes = show.viewedLeafCount?.toInt() ?: 0
          val unwatched = totalEpisodes - viewedEpisodes
          showsUnwatched = unwatched.toString()
          showsWatched = viewedEpisodes.toString()
        }
        videoList?.add(mpi)
      }
    }
  }

  private fun processGeneres(show: IDirectory): List<String> {
    return show.genres?.map { it.tag } ?: emptyList()
  }
}
