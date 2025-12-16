package us.nineworlds.serenity.jellyfin.adapters

import us.nineworlds.serenity.common.media.model.IDirectory
import us.nineworlds.serenity.common.media.model.IMediaContainer
import us.nineworlds.serenity.jellyfin.model.Directory
import us.nineworlds.serenity.jellyfin.model.Media
import us.nineworlds.serenity.jellyfin.model.MediaContainer
import us.nineworlds.serenity.jellyfin.model.Video
import us.nineworlds.serenity.jellyfin.server.model.Item
import us.nineworlds.serenity.jellyfin.server.model.NameGuidPair

class JellyfinMediaContainerAdaptor {

  companion object {
    const val TICKS_PER_MILLISECOND: Long = 10000
  }

  fun createMainMenu(items: List<Item>): IMediaContainer {
    val mediaContainer = MediaContainer()
    val directories = ArrayList<Directory>()
    for (item in items) {
      val entry = Directory()
      entry.title = item.name
      entry.type = item.collectionType
      entry.key = item.id
      directories.add(entry)
    }

    mediaContainer.directories = directories.toList()
    return mediaContainer
  }

  fun createCategory(genres: List<NameGuidPair>, series: Boolean? = false): IMediaContainer {
    val mediaContainer = MediaContainer()
    val directories = ArrayList<Directory>()

    directories.add(createAllCatagory())
    if (series == false) {
      directories.add(createUnwatched())
      directories.add(createRecentlyAddedCategory())
      directories.add(createOnDeck())
    }

    for (genre in genres) {
      val entry = Directory()
      entry.title = genre.name
      entry.key = genre.name
      entry.secondary = 0
      directories.add(entry)
    }

    mediaContainer.directories = directories.toList()

    return mediaContainer
  }

  fun createAllCatagory(): Directory {
    val allCategory = Directory()
    allCategory.title = "All"
    allCategory.key = "all"
    return allCategory
  }

  fun createUnwatched(): Directory {
    val allCategory = Directory()
    allCategory.title = "Unwatched"
    allCategory.key = "unwatched"
    return allCategory
  }

  fun createRecentlyAddedCategory(): Directory {
    val allCategory = Directory()
    allCategory.title = "Recently Added"
    allCategory.key = "recentlyAdded"
    return allCategory
  }

  fun createOnDeck(): Directory {
    val allCategory = Directory()
    allCategory.title = "OnDeck"
    allCategory.key = "ondeck"
    return allCategory
  }

  fun createSeriesList(series: List<Item>): IMediaContainer {
    val mediaContainer = MediaContainer()
    val seriesVideos = ArrayList<IDirectory>()

    for (item in series) {
      val seriesEntry = Directory()

      seriesEntry.title = item.name
      seriesEntry.summary = item.oveview
      seriesEntry.key = item.id
      seriesEntry.contentRating = item.officialRating
      seriesEntry.rating = (item.communityRating ?: 0.00).toString()

      var totalItemCount = 0L
      var viewdItemsCount = 0L
      if (item.userData != null) {
        if (item.userData.unplayedItemCount != null) {
          totalItemCount += item.userData.unplayedItemCount
        }

        if (item.userData.playCount != null) {
          viewdItemsCount = item.userData.playCount
          totalItemCount += viewdItemsCount
        }
      }

      seriesEntry.leafCount = totalItemCount.toString()
      seriesEntry.viewedLeafCount = viewdItemsCount.toString()

      seriesEntry.art = "/Items/${item.id}/Images/Thumb"
      seriesEntry.thumb = "/Items/${item.id}/Images/Primary"
      seriesEntry.banner = "/Items/${item.id}/Images/Banner"

      seriesVideos.add(seriesEntry)
    }
    mediaContainer.directories = seriesVideos
    mediaContainer.size = series.size

    return mediaContainer
  }

  fun createSeaonsList(seaons: List<Item>): IMediaContainer {
    return MediaContainer()
  }

  fun createVideoList(videos: List<Item>, token: String?): IMediaContainer {
    val mediaContainer = MediaContainer()
    val serenityVideos = ArrayList<Video>()
    mediaContainer.size = videos.size

    val items = videos.filter { item -> item.type != "Folder"  }
                      .filterNot { item -> item.name == "TBA" }

    for (item in items) {
      val video = Video()

      val sortEpisode = item.episodeNumber?.toInt() ?: 0

      video.type = item.type
      video.titleSort = sortEpisode.toString().padStart(3, '0')
      video.title = item.name
      video.key = item.id
      video.parentKey = item.parentId
      video.contentRating = item.officialRating
      video.summary = item.oveview
      video.rating = item.communityRating ?: 0.00
      video.season = item.parentIndexNumber
      video.seriesName = item.seriesName

      if (item.type != null && item.type == "Episode") {
        video.backgroundImageKey = "/Items/${item.parentId}/Images/Backdrop"
        video.parentThumbNailImageKey = "/Items/${item.parentId}/Images/Primary"
      } else {
        video.backgroundImageKey = "/Items/${item.id}/Images/Backdrop"
      }
      video.thumbNailImageKey = "/Items/${item.id}/Images/Primary"
      video.viewCount = item.userData?.playCount?.toInt() ?: 0
      val offset = convertTicksToMilliseconds(item.userData?.playbackPositionTicks ?: 0)
      video.viewOffset = offset
      video.episode = item.episodeNumber

      val container = if (item.container != null && item.container.contains(",")) {
        item.container.substringBefore(",")
      } else {
        item.container
      }
      video.directPlayUrl = "Videos/${item.mediaSources?.get(0)?.id ?: item.id}/stream.$container?static=true&api_key=$token"

      if (item.runTimeTicks != null) {
        val milliseconds = convertTicksToMilliseconds(item.runTimeTicks)
        video.duration = milliseconds
      }

      video.medias = createPlayableLists(item)

      serenityVideos.add(video)
    }
    mediaContainer.videos = serenityVideos.sortedBy { item -> item.titleSort } .toList()
    return mediaContainer
  }

  fun createPlayableLists(item: Item): List<Media> {
    val medias = mutableListOf<Media>()
    if (item.mediaStreams.isNullOrEmpty()) {
      return medias
    }

    val videoStreams = item.mediaStreams.filter { it.type == "Video" }
    val audioStreams = item.mediaStreams.filter { it.type == "Audio" }

    // If there's no video or no audio, no valid combination can be made.
    if (videoStreams.isEmpty() || audioStreams.isEmpty()) {
      return medias
    }

    videoStreams.forEach { videoStream ->
      audioStreams.forEach { audioStream ->
        val media = Media() // Create a new object for each combination
        media.container = item.container

        // From video stream
        media.aspectRatio = videoStream.aspectRatio
        media.videoCodec = videoStream.codec

        // From audio stream
        media.audioCodec = audioStream.codec
        media.audioChannels = audioStream.channels

        medias.add(media)
      }
    }

    return medias
  }


  fun convertTicksToMilliseconds(ticks: Long): Long = ticks.div(TICKS_PER_MILLISECOND)
}
