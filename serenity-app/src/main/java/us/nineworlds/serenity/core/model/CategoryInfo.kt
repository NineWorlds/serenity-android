package us.nineworlds.serenity.core.model

data class CategoryInfo(
  var level: Int = 0,
  var category: String? = null,
  var categoryDetail: String? = null
) {
  override fun toString(): String {
    return categoryDetail.orEmpty()
  }
}
