package us.nineworlds.serenity.emby.adapters

import us.nineworlds.serenity.common.media.model.IDirectory
import us.nineworlds.serenity.common.media.model.IMediaContainer
import us.nineworlds.serenity.emby.model.Directory
import us.nineworlds.serenity.emby.model.MediaContainer
import us.nineworlds.serenity.emby.server.model.Item

class MediaContainerAdaptor {

  fun createMainMenu(items: List<Item>): IMediaContainer {
    val mediaContainer = MediaContainer()
    val directories = ArrayList<Directory>()
    for(item in items) {
      val entry = Directory()
      entry.title = item.name
      entry.type = item.collectionType
      entry.key = item.id
      directories.add(entry)
    }

    mediaContainer.directories = directories as List<IDirectory>?
    return mediaContainer
  }
}