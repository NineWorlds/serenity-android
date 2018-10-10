package us.nineworlds.serenity.emby.adapters

import us.nineworlds.serenity.common.media.model.IDirectory
import us.nineworlds.serenity.common.media.model.IMediaContainer
import us.nineworlds.serenity.emby.model.Directory
import us.nineworlds.serenity.emby.model.Media
import us.nineworlds.serenity.emby.model.MediaContainer
import us.nineworlds.serenity.emby.model.Video
import us.nineworlds.serenity.emby.server.model.Item
import us.nineworlds.serenity.emby.server.model.NameGuidPair
import us.nineworlds.serenity.emby.server.model.SearchHint
import java.util.Collections

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

    for(item in series) {
      val seriesEntry = Directory()

      seriesEntry.title = item.name
      seriesEntry.summary = item.oveview
      seriesEntry.key = item.id
      seriesEntry.contentRating = item.officialRating

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

      seriesEntry.art = "/emby/Items/${item.id}/Images/Thumb"
      seriesEntry.thumb = "/emby/Items/${item.id}/Images/Primary"
      seriesEntry.banner = "/emby/Items/${item.id}/Images/Banner"

      seriesVideos.add(seriesEntry)
    }
    mediaContainer.directories = seriesVideos
    mediaContainer.size = series.size

    return mediaContainer
  }

  fun createSeaonsList(seaons: List<Item>): IMediaContainer {
    return MediaContainer()
  }

  fun createVideoList(videos: List<Item>): IMediaContainer {
    val mediaContainer = MediaContainer()
    val serenityVideos = ArrayList<Video>()
    mediaContainer.size = videos.size

    for (item in videos) {
      val video = Video()

      video.title = item.name
      video.key = item.id
      video.parentKey = item.parentId
      video.contentRating = item.officialRating
      video.summary = item.oveview
      video.rating = item.communityRating ?: 0.00
      video.season = item.parentIndexNumber
      if (item.type != null && item.type == "Episode") {
        video.backgroundImageKey = "/emby/Items/${item.parentLogoItemId}/Images/Logo"
        video.parentThumbNailImageKey = "/emby/Items/${item.parentId}/Images/Primary"
      } else {
        video.backgroundImageKey = "/emby/Items/${item.id}/Images/Backdrop"
      }
      video.thumbNailImageKey = "/emby/Items/${item.id}/Images/Primary"
      video.viewCount = item.userData?.playCount?.toInt() ?: 0
      video.viewOffset = convertTicksToMilliseconds(item.userData?.playbackPositionTicks ?: 0)
      video.episode = item.episodeNumber

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