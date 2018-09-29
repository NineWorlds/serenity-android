package us.nineworlds.serenity.emby.adapters

import us.nineworlds.serenity.common.media.model.IMediaContainer
import us.nineworlds.serenity.emby.model.Directory
import us.nineworlds.serenity.emby.model.Media
import us.nineworlds.serenity.emby.model.MediaContainer
import us.nineworlds.serenity.emby.model.Video
import us.nineworlds.serenity.emby.server.model.Item
import us.nineworlds.serenity.emby.server.model.NameGuidPair

class MediaContainerAdaptor {

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

  fun createCategory(genres: List<NameGuidPair>): IMediaContainer {
    val mediaContainer = MediaContainer()
    val directories = ArrayList<Directory>()

    directories.add(createAllCatagory())
    directories.add(createUnwatched())
    directories.add(createRecentlyAddedCategory())
    directories.add(createOnDeck())

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

  fun createVideoList(videos: List<Item>): IMediaContainer {
    val mediaContainer = MediaContainer()
    val serenityVideos = ArrayList<Video>()

    for (item in videos) {
      val video = Video()

      video.key = item.id
      video.parentKey = item.parentId
      video.contentRating = item.officialRating
      video.summary = item.oveview
      video.rating = item.communityRating ?: 0.00
      video.backgroundImageKey = "/emby/Items/${item.id}/Images/Backdrop"
      video.thumbNailImageKey = "/emby/Items/${item.id}/Images/Primary"
      video.viewCount = item.userData?.playCount?.toInt() ?: 0
      video.viewOffset = convertTicksToMilliseconds(item.userData?.playbackPositionTicks ?: 0)

      video.directPlayUrl = "emby/Videos/${item.mediaSources?.get(0)?.id ?: ""}/stream.${item.container}?static=true"

      if (item.runTimeTicks != null) {
        val milliseconds = convertTicksToMilliseconds(item.runTimeTicks)
        video.duration = milliseconds
      }

      if (item.mediaStreams != null) {
        val medias = ArrayList<Media>()
        val media = Media()
        media.container = item.container
        for (mediaStream in item.mediaStreams) {
          if (mediaStream.type == "Video") {
            media.aspectRatio = mediaStream.aspectRatio
            media.videoCodec = mediaStream.codec
          } else {
            media.audioCodec = mediaStream.codec
            media.audioChannels = mediaStream.channels
          }
          medias.add(media)
        }
        video.medias = medias.toList()
      }

      serenityVideos.add(video)
    }
    mediaContainer.videos = serenityVideos.toList()
    return mediaContainer
  }

  fun convertTicksToMilliseconds(ticks: Long): Long = ticks.div(TICKS_PER_MILLISECOND)

}