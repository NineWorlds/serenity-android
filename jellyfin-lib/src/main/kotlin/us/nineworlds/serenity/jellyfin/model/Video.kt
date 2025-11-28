package us.nineworlds.serenity.jellyfin.model

import us.nineworlds.serenity.common.media.model.ICountry
import us.nineworlds.serenity.common.media.model.IDirector
import us.nineworlds.serenity.common.media.model.IGenre
import us.nineworlds.serenity.common.media.model.IMedia
import us.nineworlds.serenity.common.media.model.IRole
import us.nineworlds.serenity.common.media.model.IVideo
import us.nineworlds.serenity.common.media.model.IWriter

class Video : AbstractJellyfinClientObject(), IVideo {
    private var _type: String? = null
    private var _studio: String? = null
    private var _summary: String? = null
    private var _titleSort: String? = null
    private var _title: String? = null
    private var _viewCount: Int = 0
    private var _tagLine: String? = null
    private var _viewOffset: Long = 0
    private var _thumbNailImageKey: String? = null
    private var _backgroundImageKey: String? = null
    private var _parentThumbNailImageKey: String? = null
    private var _grandParentThumbNailImageKey: String? = null
    private var _grandParentTitle: String? = null
    private var _duration: Long = 0
    private var _timeAdded: Long = 0
    private var _timeUpdated: Long = 0
    private var _originallyAvailableDate: String? = null
    private var _contentRating: String? = null
    private var _year: String? = null
    private var _ratingKey: String? = null
    private var _parentKey: String? = null
    private var _episode: String? = null
    private var _season: String? = null
    private var _rating: Double = 0.0
    private var _countries: List<ICountry>? = null
    private var _directors: List<IDirector>? = null
    private var _actors: List<IRole>? = null
    private var _writers: List<IWriter>? = null
    private var _genres: List<IGenre>? = null
    private var _medias: List<IMedia>? = null
    private var _directPlayUrl: String? = null
    private var _seriesName: String? = null

    override fun getSeriesName(): String? = _seriesName
    override fun setSeriesName(seriesName: String?) {
        _seriesName = seriesName
    }

    override fun getType(): String? = _type
    override fun setType(type: String?) {
        _type = type
    }

    override fun getGrandParentTitle(): String? = _grandParentTitle
    override fun setGrandParentTitle(grandParentTitle: String?) {
        _grandParentTitle = grandParentTitle
    }

    override fun getGrandParentThumbNailImageKey(): String? = _grandParentThumbNailImageKey
    override fun setGrandParentThumbNailImageKey(grandParentThumbNailImageKey: String?) {
        _grandParentThumbNailImageKey = grandParentThumbNailImageKey
    }

    override fun getActors(): List<IRole>? = _actors
    override fun setActors(actors: List<IRole>?) {
        _actors = actors
    }

    override fun getBackgroundImageKey(): String? = _backgroundImageKey
    override fun setBackgroundImageKey(backgroundImageKey: String?) {
        _backgroundImageKey = backgroundImageKey
    }

    override fun getContentRating(): String? = _contentRating
    override fun setContentRating(contentRating: String?) {
        _contentRating = contentRating
    }

    override fun getCountries(): List<ICountry>? = _countries
    override fun setCountries(countries: List<ICountry>?) {
        _countries = countries
    }

    override fun getDirectors(): List<IDirector>? = _directors
    override fun setDirectors(directors: List<IDirector>?) {
        _directors = directors
    }

    override fun getDuration(): Long = _duration
    override fun setDuration(duration: Long) {
        _duration = duration
    }

    override fun getGenres(): List<IGenre>? = _genres
    override fun setGenres(genres: List<IGenre>?) {
        _genres = genres
    }

    override fun getMedias(): List<IMedia>? = _medias
    override fun setMedias(medias: List<IMedia>?) {
        _medias = medias
    }

    override fun getOriginallyAvailableDate(): String? = _originallyAvailableDate
    override fun setOriginallyAvailableDate(originallyAvailableDate: String?) {
        _originallyAvailableDate = originallyAvailableDate
    }

    override fun getSummary(): String? = _summary
    override fun setSummary(summary: String?) {
        _summary = summary
    }

    override fun getTagLine(): String? = _tagLine
    override fun setTagLine(tagLine: String?) {
        _tagLine = tagLine
    }

    override fun getThumbNailImageKey(): String? = _thumbNailImageKey
    override fun setThumbNailImageKey(thumbNailImageKey: String?) {
        _thumbNailImageKey = thumbNailImageKey
    }

    override fun getTimeAdded(): Long = _timeAdded
    override fun setTimeAdded(timeAdded: Long) {
        _timeAdded = timeAdded
    }

    override fun getTimeUpdated(): Long = _timeUpdated
    override fun setTimeUpdated(timeUpdated: Long) {
        _timeUpdated = timeUpdated
    }

    override fun getTitle(): String? = _title
    override fun setTitle(title: String?) {
        _title = title
    }

    override fun getTitleSort(): String? = _titleSort
    override fun setTitleSort(titleSort: String?) {
        _titleSort = titleSort
    }

    override fun getViewCount(): Int = _viewCount
    override fun setViewCount(viewCount: Int) {
        _viewCount = viewCount
    }

    override fun getViewOffset(): Long = _viewOffset
    override fun setViewOffset(viewOffset: Long) {
        _viewOffset = viewOffset
    }

    override fun getWriters(): List<IWriter>? = _writers
    override fun setWriters(writers: List<IWriter>?) {
        _writers = writers
    }

    override fun getYear(): String? = _year
    override fun setYear(year: String?) {
        _year = year
    }

    override fun getRatingKey(): String? = _ratingKey
    override fun setRatingKey(ratingKey: String?) {
        _ratingKey = ratingKey
    }

    override fun getParentThumbNailImageKey(): String? = _parentThumbNailImageKey
    override fun setParentThumbNailImageKey(parentThumbNailImageKey: String?) {
        _parentThumbNailImageKey = parentThumbNailImageKey
    }

    override fun getStudio(): String? = _studio
    override fun setStudio(studio: String?) {
        _studio = studio
    }

    override fun getRating(): Double = _rating
    override fun setRating(rating: Double) {
        _rating = rating
    }

    override fun getParentKey(): String? = _parentKey
    override fun setParentKey(parentKey: String?) {
        _parentKey = parentKey
    }

    override fun getEpisode(): String? = _episode
    override fun setEpisode(episode: String?) {
        _episode = episode
    }

    override fun getSeason(): String? = _season
    override fun setSeason(season: String?) {
        _season = season
    }

    override fun getDirectPlayUrl(): String? = _directPlayUrl
    override fun setDirectPlayUrl(directPlayUrl: String?) {
        _directPlayUrl = directPlayUrl
    }
}