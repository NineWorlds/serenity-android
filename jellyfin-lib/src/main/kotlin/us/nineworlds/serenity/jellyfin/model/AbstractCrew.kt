package us.nineworlds.serenity.jellyfin.model

import us.nineworlds.serenity.common.media.model.ICrew

abstract class AbstractCrew : ICrew {
    private var _tag: String? = null

    override fun getTag(): String? {
        return _tag
    }

    override fun setTag(tag: String?) {
        _tag = tag
    }

}