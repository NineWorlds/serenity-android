package us.nineworlds.serenity.jellyfin.model

import us.nineworlds.serenity.common.media.model.ClientObject

abstract class AbstractJellyfinClientObject : ClientObject {
    private var _key: String? = null

    override fun getKey(): String? = _key

    override fun setKey(key: String?) {
        _key = key
    }
}
