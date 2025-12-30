package us.nineworlds.serenity.core.model.impl

import us.nineworlds.serenity.common.media.model.IMediaContainer
import us.nineworlds.serenity.core.model.SecondaryCategoryInfo

class SecondaryCategoryMediaContainer(mc: IMediaContainer, private val parentCategoryKey: String?) : AbstractMediaContainer(mc) {

    private var categories: MutableList<SecondaryCategoryInfo> = mutableListOf()

    fun createCategories(): List<SecondaryCategoryInfo> {
        populateSecondaryCategories()
        return categories
    }

    private fun populateSecondaryCategories() {
        val dirs = mc.directories ?: return

        categories = mutableListOf()
        for (dir in dirs) {
            val category = SecondaryCategoryInfo().apply {
                this.category = dir.key
                categoryDetail = dir.title
                parentCategory = parentCategoryKey
            }
            categories.add(category)
        }
    }
}
