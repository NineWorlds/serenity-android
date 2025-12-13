package us.nineworlds.serenity.jellyfin.model

import us.nineworlds.serenity.common.media.model.ILocation

class Location : ILocation {
    private var _path: String? = null

    override fun getPath(): String? {
        return _path
    }

    override fun setPath(path: String?) {
        _path = path
    }
}