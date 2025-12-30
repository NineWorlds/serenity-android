package us.nineworlds.serenity.core.model

open class CategoryInfo {
    var level: Int = 0
    var category: String? = null
    var categoryDetail: String? = null

    override fun toString(): String = categoryDetail.orEmpty()
}
