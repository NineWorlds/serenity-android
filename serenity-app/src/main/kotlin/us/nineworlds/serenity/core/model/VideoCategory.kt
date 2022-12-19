package us.nineworlds.serenity.core.model

import us.nineworlds.serenity.common.rest.Types

data class CategoryVideoInfo(
    val categories: List<CategoryInfo>
)

data class VideoCategory(
        val type: Types,
        val item: VideoContentInfo
)
