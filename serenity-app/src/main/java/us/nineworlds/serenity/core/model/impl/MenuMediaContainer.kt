package us.nineworlds.serenity.core.model.impl

import android.content.SharedPreferences
import android.content.res.Resources
import javax.inject.Inject
import us.nineworlds.serenity.R
import us.nineworlds.serenity.common.media.model.IDirectory
import us.nineworlds.serenity.common.media.model.IMediaContainer
import us.nineworlds.serenity.core.menus.MenuItem

class MenuMediaContainer(mc: IMediaContainer) : AbstractMediaContainer(mc) {

    @Inject
    lateinit var preferences: SharedPreferences

    private val menuItems = mutableListOf<MenuItem>()

    fun createMenuItems(): List<MenuItem> {
        val dirs = mc.directories ?: return emptyList()

        for (item in dirs) {
            if (isImplemented(item)) {
                val musicEnabled = preferences.getBoolean("plex_music_library", false)
                if (!musicEnabled && item.type == "artist") {
                    continue
                }
                val m = MenuItem().apply {
                    title = item.title
                    type = item.type
                    section = item.key
                }
                menuItems.add(m)
            }
        }

        menuItems.add(createSettingsMenu())
        return menuItems
    }

    protected fun isImplemented(item: IDirectory): Boolean = item.type != "playlists" &&
        item.type != "photos" &&
        item.type != "photo" &&
        item.type != "folders"

    fun createSettingsMenu(): MenuItem = MenuItem().apply {
        title = resources.getString(R.string.settings)
        type = SETTINGS_TYPE
        section = SETTINGS_SECTION_KEY
    }

    fun createSearchMenu(): MenuItem = MenuItem().apply {
        title = resources.getString(R.string.search)
        type = SEARCH_TYPE
        section = SETTINGS_SECTION_KEY
    }

    fun createOptionsMenu(): MenuItem = MenuItem().apply {
        title = resources.getString(R.string.options)
        type = OPTIONS_TYPE
        section = SETTINGS_SECTION_KEY
    }

    companion object {
        private const val SETTINGS_SECTION_KEY = "0"
        private const val SETTINGS_TYPE = "settings"
        private const val SEARCH_TYPE = "search"
        private const val OPTIONS_TYPE = "options"
    }
}
