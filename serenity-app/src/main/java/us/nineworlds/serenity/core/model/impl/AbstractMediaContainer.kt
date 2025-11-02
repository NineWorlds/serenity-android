package us.nineworlds.serenity.core.model.impl

import javax.inject.Inject
import us.nineworlds.serenity.common.media.model.IMediaContainer
import us.nineworlds.serenity.common.rest.SerenityClient
import us.nineworlds.serenity.core.model.VideoContentInfo
import us.nineworlds.serenity.injection.BaseInjector

abstract class AbstractMediaContainer(
  @JvmField
  protected var mc: IMediaContainer,
) : BaseInjector() {

  @JvmField
  protected var videoList: MutableList<VideoContentInfo>? = null

  @Inject
  @JvmField
  protected lateinit var factory: SerenityClient
}
