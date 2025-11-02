package us.nineworlds.serenity.core.model.impl

import android.content.res.Resources
import java.io.Serializable

class EpisodePosterInfo(resources: Resources?) : AbstractVideoContentInfo(resources), Serializable {
  companion object {
    private const val serialVersionUID = 5331132648154894964L
  }
}
