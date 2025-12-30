package us.nineworlds.serenity.core.model

import us.nineworlds.serenity.common.rest.Types

interface ContentInfo {
    fun id(): String?
    fun getType(): Types?
    fun setType(type: Types?)
    fun getSummary(): String?
    fun getBackgroundURL(): String?
    fun getImageURL(): String?
    fun getTitle(): String?
    fun setTitle(title: String?)
    fun setImageURL(imageURL: String?)
    fun setSummary(summary: String?)
    fun setBackgroundURL(backgroundURL: String?)
    fun setId(id: String?)
    fun getMediaTagIdentifier(): String?
    fun setMediaTagIdentifier(mediaTagIdentifier: String?)
}
