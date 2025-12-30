package us.nineworlds.serenity.core.model.impl

import us.nineworlds.serenity.common.media.model.IDirectory
import us.nineworlds.serenity.common.media.model.IMediaContainer
import us.nineworlds.serenity.core.model.CategoryInfo

class CategoryMediaContainer(mc: IMediaContainer) : AbstractMediaContainer(mc) {

    @JvmField
    protected var categories: MutableList<CategoryInfo> = mutableListOf()

    @JvmField
    protected var filterAlbums = false

    fun createCategories(): List<CategoryInfo> {
        filterAlbums = false
        populateCategories()
        return categories
    }

    fun createCatagoriesFilteringAlbums(): List<CategoryInfo> {
        filterAlbums = true
        populateCategories()
        return categories
    }

    protected fun populateCategories() {
        val dirs = mc.directories ?: return

        categories = mutableListOf()
        for (dir in dirs) {
            if (resultsNotFiltered(dir)) {
                val category = CategoryInfo().apply {
                    this.category = dir.key
                    categoryDetail = dir.title
                    if (dir.secondary > 0) {
                        level = dir.secondary
                    }
                }
                categories.add(category)
            }
        }
    }

    protected fun resultsNotFiltered(dir: IDirectory): Boolean {
        if (filterAlbums) {
            if (dir.key == "year" || dir.key == "decade") {
                return false
            }
        }
        return dir.key != "folder" &&
            dir.title != "Search..." &&
            dir.title != "Search Artists..." &&
            dir.title != "Search Albums..." &&
            dir.title != "Search Tracks..."
    }
}
