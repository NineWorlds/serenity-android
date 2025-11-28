package us.nineworlds.serenity.jellyfin.model

import us.nineworlds.serenity.common.media.model.IDirectory
import us.nineworlds.serenity.common.media.model.IGenre
import us.nineworlds.serenity.common.media.model.ILocation

class Directory : IDirectory {
    private var _thumb: String? = null
    private var _banner: String? = null
    private var _secondary: Int = 0
    private var _genres: List<IGenre>? = null
    private var _ratingKey: String? = null
    private var _studio: String? = null
    private var _rating: String? = null
    private var _year: String? = null
    private var _contentRating: String? = null
    private var _summary: String? = null
    private var _leafCount: String? = null
    private var _viewedLeafCount: String? = null
    private var _title: String? = null
    private var _art: String? = null
    private var _refreshing: Int = 0
    private var _type: String? = null
    private var _agent: String? = null
    private var _scanner: String? = null
    private var _language: String? = null
    private var _uuid: String? = null
    private var _updatedAt: Long = 0
    private var _createdAt: Long = 0
    private var _prompt: String? = null
    private var _search: String? = null
    private var _locations: List<ILocation>? = null
    private var _key: String? = null

    override fun getThumb(): String? = _thumb
    override fun setThumb(thumb: String?) {
        _thumb = thumb
    }

    override fun getBanner(): String? = _banner
    override fun setBanner(banner: String?) {
        _banner = banner
    }

    override fun getSecondary(): Int = _secondary
    override fun setSecondary(secondary: Int) {
        _secondary = secondary
    }

    override fun getGenres(): List<IGenre>? = _genres
    override fun setGenres(genres: List<IGenre>?) {
        _genres = genres
    }

    override fun getRatingKey(): String? = _ratingKey
    override fun setRatingKey(ratingKey: String?) {
        _ratingKey = ratingKey
    }

    override fun getStudio(): String? = _studio
    override fun setStudio(studio: String?) {
        _studio = studio
    }

    override fun getRating(): String? = _rating
    override fun setRating(rating: String?) {
        _rating = rating
    }

    override fun getYear(): String? = _year
    override fun setYear(year: String?) {
        _year = year
    }

    override fun getContentRating(): String? = _contentRating
    override fun setContentRating(contentRating: String?) {
        _contentRating = contentRating
    }

    override fun getSummary(): String? = _summary
    override fun setSummary(summary: String?) {
        _summary = summary
    }

    override fun getLeafCount(): String? = _leafCount
    override fun setLeafCount(leafCount: String?) {
        _leafCount = leafCount
    }

    override fun getViewedLeafCount(): String? = _viewedLeafCount
    override fun setViewedLeafCount(viewedLeafCount: String?) {
        this._viewedLeafCount = viewedLeafCount
    }

    override fun getTitle(): String? = _title
    override fun setTitle(title: String?) {
        _title = title
    }

    override fun getArt(): String? = _art
    override fun setArt(art: String?) {
        _art = art
    }

    override fun getRefreshing(): Int = _refreshing
    override fun setRefreshing(refreshing: Int) {
        _refreshing = refreshing
    }

    override fun getType(): String? = _type
    override fun setType(type: String?) {
        _type = type
    }

    override fun getAgent(): String? = _agent
    override fun setAgent(agent: String?) {
        _agent = agent
    }

    override fun getScanner(): String? = _scanner
    override fun setScanner(scanner: String?) {
        _scanner = scanner
    }

    override fun getLanguage(): String? = _language
    override fun setLanguage(language: String?) {
        _language = language
    }

    override fun getUuid(): String? = _uuid
    override fun setUuid(uuid: String?) {
        _uuid = uuid
    }

    override fun getUpdatedAt(): Long = _updatedAt
    override fun setUpdatedAt(updatedAt: Long) {
        _updatedAt = updatedAt
    }

    override fun getCreatedAt(): Long = _createdAt
    override fun setCreatedAt(createdAt: Long) {
        _createdAt = createdAt
    }

    override fun getLocations(): List<ILocation>? = _locations
    override fun setLocation(location: List<ILocation>?) {
        _locations = location
    }

    override fun getPrompt(): String? = _prompt
    override fun setPrompt(prompt: String?) {
        _prompt = prompt
    }

    override fun getSearch(): String? = _search
    override fun setSearch(search: String?) {
        this._search = search
    }

    override fun setLocations(locations: List<ILocation>?) {
        _locations = locations
    }

    override fun getKey(): String? = _key
    override fun setKey(key: String?) {
        _key = key
    }
}