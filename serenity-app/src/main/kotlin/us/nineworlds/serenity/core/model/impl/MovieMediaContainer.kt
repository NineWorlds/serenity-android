package us.nineworlds.serenity.core.model.impl

import us.nineworlds.serenity.common.media.model.IMediaContainer
import us.nineworlds.serenity.common.media.model.IVideo
import us.nineworlds.serenity.common.rest.Types
import us.nineworlds.serenity.core.model.VideoContentInfo

open class MovieMediaContainer(mc: IMediaContainer) : AbstractMediaContainer(mc) {

  fun createVideos(): List<VideoContentInfo> {
    videoList = mutableListOf()
    createVideoContent(mc)
    return videoList as List<VideoContentInfo>
  }

  protected open fun createVideoContent(mc: IMediaContainer) {
    val baseUrl = factory.baseURL()
    val videos = mc.videos ?: return

    val mediaTagId = mc.mediaTagVersion.toString()
    val baseImageResource = "$baseUrl:/resources/movie-fanart.jpg"

    for (movie in videos) {
      val mpi = MoviePosterInfo().apply {
        setType(when (movie.type?.lowercase()) {
          "episode" -> Types.EPISODE.also { seriesName = movie.seriesName }
          "series" -> Types.SERIES.also { seriesName = movie.seriesName }
          "season" -> Types.SEASON.also { seriesName = movie.seriesName }
          "movie" -> Types.MOVIES
          else -> Types.UNKNOWN
        })

        setMediaTagIdentifier(mediaTagId)
        setId(movie.key)
        studio = movie.studio
        setSummary(movie.summary)
        resumeOffset = movie.viewOffset.toInt()
        duration = movie.duration.toInt()
        viewCount = movie.viewCount
        rating = movie.rating
        tagLine = movie.tagLine
        setBackgroundURL(movie.backgroundImageKey?.let { "$baseUrl${it.replaceFirst("/", "")}" } ?: baseImageResource)
        setImageURL(movie.thumbNailImageKey?.let { "$baseUrl${it.replaceFirst("/", "")}" } ?: "")
        setTitle(movie.title)
        contentRating = movie.contentRating
        directPlayUrl = "$baseUrl${movie.directPlayUrl}"

        val sortedMedias = movie.medias?.sortedByDescending { isDirectPlaySupported(it) }
        sortedMedias?.firstOrNull()?.let { media ->
          container = media.container
          media.videoPart?.firstOrNull()?.let {
            directPlayUrl = "$baseUrl${it.key.replaceFirst("/", "")}"
          }
          audioCodec = media.audioCodec
          videoCodec = media.videoCodec
          videoResolution = media.videoResolution
          aspectRatio = media.aspectRatio
          audioChannels = media.audioChannels
        }
      }

      createVideoDetails(movie, mpi)
      videoList!!.add(mpi)
    }
  }

  protected open fun createVideoDetails(video: IVideo, videoContentInfo: VideoContentInfo) {
    videoContentInfo.year = video.year
    videoContentInfo.genres = video.genres?.map { it.tag }
    videoContentInfo.writers = video.writers?.map { it.tag }
    videoContentInfo.directors = video.directors?.map { it.tag }
  }
}
