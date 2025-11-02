package us.nineworlds.serenity.core.model.impl

import java.io.Serializable

data class Subtitle(
  var key: String? = null,
  var format: String? = null,
  var description: String? = null,
  var languageCode: String? = null
) : Serializable {
  override fun toString(): String {
    return description!!
  }

  companion object {
    private const val serialVersionUID = -8913477697639140949L
  }
}
