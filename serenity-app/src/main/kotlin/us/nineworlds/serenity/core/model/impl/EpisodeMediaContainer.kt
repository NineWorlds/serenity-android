package us.nineworlds.serenity.core.model.impl

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import us.nineworlds.serenity.common.media.model.IMediaContainer
import us.nineworlds.serenity.common.media.model.IVideo
import us.nineworlds.serenity.core.model.VideoContentInfo

class EpisodeMediaContainer(mc: IMediaContainer) : MovieMediaContainer(mc) {

    override fun createVideos(): List<VideoContentInfo> {
        val videos = mc.videos ?: return emptyList()
        val baseUrl = factory.baseURL().orEmpty()
        var parentPosterURL: String? = null
        if (!mc.parentPosterURL.isNullOrEmpty() && !mc.parentPosterURL.contains("show")) {
            parentPosterURL = baseUrl + mc.parentPosterURL.substring(1)
        }

        val createdVideos = runBlocking {
            videos.chunked(25).map { chunk ->
                async(Dispatchers.Default) {
                    chunk.map { episode ->
                        createEpisodeContentInfo(mc, baseUrl, parentPosterURL, episode)
                    }
                }
            }.awaitAll().flatten()
        }
        videoList = createdVideos.toMutableList()
        return createdVideos
    }

    private fun createEpisodeContentInfo(mc: IMediaContainer, baseUrl: String, parentPosterURL: String?, episode: IVideo): EpisodePosterInfo {
        val epi = EpisodePosterInfo(resources).apply {
            if (parentPosterURL != null) {
                this.parentPosterURL = parentPosterURL
            }
            setId(episode.key)
            parentKey = episode.parentKey
            setSummary(episode.summary)
            viewCount = episode.viewCount
            resumeOffset = episode.viewOffset.toInt()
            duration = episode.duration.toInt()
            originalAirDate = episode.originallyAvailableDate
            if (episode.parentThumbNailImageKey != null && !episode.parentThumbNailImageKey.isNullOrEmpty()) {
                this.parentPosterURL = baseUrl + episode.parentThumbNailImageKey.substring(1)
            }
            if (episode.grandParentThumbNailImageKey != null && !episode.grandParentThumbNailImageKey.isNullOrEmpty()) {
                grandParentPosterURL = baseUrl + episode.grandParentThumbNailImageKey.substring(1)
            }
            setBackgroundURL(
                when {
                    episode.backgroundImageKey != null -> baseUrl + episode.backgroundImageKey.replaceFirst("/", "")
                    mc.art != null -> baseUrl + mc.art.replaceFirst("/", "")
                    else -> "${factory.baseURL()}:/resources/show-fanart.jpg"
                }
            )

            setImageURL(episode.thumbNailImageKey?.let { "$baseUrl${it.replaceFirst("/", "")}" } ?: "")
            setTitle(episode.title)
            seriesTitle = episode.grandParentTitle ?: mc.title1
            contentRating = episode.contentRating
        }

        val sortedMedias = episode.medias?.sortedByDescending { epi.isDirectPlaySupported(it) }

        sortedMedias?.firstOrNull()?.let { media ->
            epi.container = media.container
            val part = media.videoPart?.firstOrNull()
            if (part != null) {
                epi.directPlayUrl = "${factory.baseURL()}${part.key.replaceFirst("/", "")}"
            } else {
                epi.directPlayUrl = "${factory.baseURL()}${episode.directPlayUrl}"
            }
            epi.seasonNumber = episode.season?.toIntOrNull() ?: mc.parentIndex?.toIntOrNull() ?: 0
            epi.episodeNumber = episode.episode?.toIntOrNull() ?: 0
            epi.audioCodec = media.audioCodec
            epi.videoCodec = media.videoCodec
            epi.videoResolution = media.videoResolution
            epi.aspectRatio = media.aspectRatio
            epi.audioChannels = media.audioChannels
        }

        createVideoDetails(episode, epi)
        epi.castInfo = ""
        return epi
    }

    override fun createVideoDetails(video: IVideo, videoContentInfo: VideoContentInfo) {
        super.createVideoDetails(video, videoContentInfo)
    }
}
