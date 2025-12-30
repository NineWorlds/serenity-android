package us.nineworlds.serenity.core.model.impl

import us.nineworlds.serenity.common.media.model.IDirectory
import us.nineworlds.serenity.common.media.model.IMediaContainer
import us.nineworlds.serenity.core.model.SeriesContentInfo

open class SeriesMediaContainer(mc: IMediaContainer) : AbstractMediaContainer(mc) {

    open fun createSeries(): List<SeriesContentInfo> {
        videoList = mutableListOf()
        createSeriesInfo()
        return videoList as List<SeriesContentInfo>
    }

    open fun createSeriesInfo() {
        val baseUrl = factory.baseURL()
        if (mc.size > 0) {
            val mediaTagId = mc.mediaTagVersion.toString()
            val shows = mc.directories ?: return

            for (show in shows) {
                val mpi = TVShowSeriesInfo().apply {
                    setId(show.key)
                    key = show.key
                    setMediaTagIdentifier(mediaTagId)
                    setSummary(show.summary)
                    studio = show.studio
                    rating = show.rating?.toDouble() ?: 0.0
                    setBackgroundURL(show.art?.let { "$baseUrl${it.replaceFirst("/", "")}" } ?: "$baseUrl:/resources/show-fanart.jpg")
                    setImageURL(show.banner?.let { "$baseUrl${it.replaceFirst("/", "")}" } ?: "")
                    thumbNailURL = show.thumb?.let { "$baseUrl${it.replaceFirst("/", "")}" } ?: ""
                    setTitle(show.title)
                    contentRating = show.contentRating
                    generes = processGeneres(show)

                    val totalEpisodes = show.leafCount?.toInt() ?: 0
                    val viewedEpisodes = show.viewedLeafCount?.toInt() ?: 0
                    val unwatched = totalEpisodes - viewedEpisodes
                    showsUnwatched = unwatched.toString()
                    showsWatched = viewedEpisodes.toString()
                }
                videoList?.add(mpi as SeriesContentInfo)
            }
        }
    }

    private fun processGeneres(show: IDirectory): List<String> = show.genres?.map { it.tag } ?: emptyList()
}
