package us.nineworlds.serenity.jellyfin.model

import us.nineworlds.serenity.common.media.model.IDirectory
import us.nineworlds.serenity.common.media.model.IMediaContainer
import us.nineworlds.serenity.common.media.model.ITrack
import us.nineworlds.serenity.common.media.model.IVideo

class MediaContainer : IMediaContainer {
    private var _title1: String? = null
    private var _title2: String? = null
    private var _directories: List<IDirectory>? = null
    private var _size: Int = 0
    private var _allowSync: Int = 0
    private var _identifier: String? = null
    private var _mediaTagPrefix: String? = null
    private var _mediaTagVersion: Long = 0
    private var _videos: List<IVideo>? = null
    private var _art: String? = null
    private var _sortAsc: Int = 0
    private var _content: String? = null
    private var _viewGroup: String? = null
    private var _viewMode: Int = 0
    private var _parentPosterUrl: String? = null
    private var _tracks: List<ITrack>? = null
    private var _parentIndex: String? = null

    override fun getTitle1(): String? = _title1
    override fun setTitle1(title1: String?) {
        _title1 = title1
    }

    override fun getTitle2(): String? = _title2
    override fun setTitle2(title2: String?) {
        _title2 = title2
    }

    override fun getDirectories(): List<IDirectory>? = _directories
    override fun setDirectories(directory: List<IDirectory>?) {
        _directories = directory
    }

    override fun getSize(): Int = _size
    override fun setSize(size: Int) {
        _size = size
    }

    override fun getAllowSync(): Int = _allowSync
    override fun setAllowSync(allowSync: Int) {
        _allowSync = allowSync
    }

    override fun getIdentifier(): String? = _identifier
    override fun setIdentifier(identifier: String?) {
        _identifier = identifier
    }

    override fun getMediaTagPrefix(): String? = _mediaTagPrefix
    override fun setMediaTagPrefix(mediaTagPrefix: String?) {
        _mediaTagPrefix = mediaTagPrefix
    }

    override fun getMediaTagVersion(): Long = _mediaTagVersion
    override fun setMediaTagVersion(mediaTagVersion: Long) {
        _mediaTagVersion = mediaTagVersion
    }

    override fun setMediaTagVersion(mediaTagVersion: Int) {
        this.mediaTagVersion = mediaTagVersion.toLong()
    }

    override fun getArt(): String? = _art
    override fun setArt(art: String?) {
        _art = art
    }

    override fun getSortAsc(): Int = _sortAsc
    override fun setSortAsc(sortAsc: Int) {
        _sortAsc = sortAsc
    }

    override fun getContent(): String? = _content
    override fun setContent(content: String?) {
        _content = content
    }

    override fun getViewGroup(): String? = _viewGroup
    override fun setViewGroup(viewGroup: String?) {
        _viewGroup = viewGroup
    }

    override fun getViewMode(): Int = _viewMode
    override fun setViewMode(viewMode: Int) {
        _viewMode = viewMode
    }

    override fun getVideos(): List<IVideo>? = _videos
    override fun setVideos(videos: List<IVideo>?) {
        _videos = videos
    }

    override fun getParentPosterURL(): String? = _parentPosterUrl
    override fun setParentPosterURL(parentPosterURL: String?) {
        _parentPosterUrl = parentPosterURL
    }

    override fun getTracks(): List<ITrack>? = _tracks
    override fun setTracks(tracks: List<ITrack>?) {
        _tracks = tracks
    }

    override fun getParentIndex(): String? = _parentIndex
    override fun setParentIndex(parentIndex: String?) {
        _parentIndex = parentIndex
    }
}