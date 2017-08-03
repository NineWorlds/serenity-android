package us.nineworlds.serenity.core.menus

import java.io.Serializable

/**
 * Typically used in the Main Menu to specify the type of menu item.
 *
 *
 * i.e. movie, show, music, etc.
 *
 *
 * It is primarily a data object.
 */
open class MenuItem : Serializable {
  var type: String? = null
  var title: String = ""
  var section: String? = null

  companion object {

    private const val serialVersionUID = -5394624840598604518L
  }

  override fun toString(): String {
    return title
  }


}
