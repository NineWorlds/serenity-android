package us.nineworlds.serenity.emby.adapters

import us.nineworlds.serenity.common.media.model.IMediaContainer
import us.nineworlds.serenity.emby.model.Directory
import us.nineworlds.serenity.emby.model.MediaContainer
import us.nineworlds.serenity.emby.model.Video
import us.nineworlds.serenity.emby.server.model.Item
import us.nineworlds.serenity.emby.server.model.NameGuidPair

class MediaContainerAdaptor {

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
    directories.add(createRecentlyReleasedCategory())
    directories.add(createRecentlyAddedCategory())
    directories.add(createRecentlyViewedCategory())
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

  fun createRecentlyReleasedCategory(): Directory {
    val allCategory = Directory()
    allCategory.title = "Recently Released"
    allCategory.key = "newest"
    return allCategory
  }

  fun createRecentlyAddedCategory(): Directory {
    val allCategory = Directory()
    allCategory.title = "Recently Added"
    allCategory.key = "recentlyAdded"
    return allCategory
  }

  fun createRecentlyViewedCategory(): Directory {
    val allCategory = Directory()
    allCategory.title = "Recently Viewed"
    allCategory.key = "recentlyViewed"
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

      serenityVideos.add(video)
    }
    mediaContainer.videos = serenityVideos.toList()
    return mediaContainer
  }

}