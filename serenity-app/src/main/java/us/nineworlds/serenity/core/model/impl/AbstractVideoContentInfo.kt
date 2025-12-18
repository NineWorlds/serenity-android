package us.nineworlds.serenity.core.model.impl

import android.content.res.Resources
import toothpick.Toothpick
import us.nineworlds.serenity.R
import us.nineworlds.serenity.common.android.mediacodec.MediaCodecInfoUtil
import us.nineworlds.serenity.common.annotations.InjectionConstants
import us.nineworlds.serenity.common.media.model.IMedia
import us.nineworlds.serenity.common.rest.Types
import us.nineworlds.serenity.core.SerenityConstants
import us.nineworlds.serenity.core.model.VideoContentInfo
import us.nineworlds.serenity.core.services.UnWatchVideoJob
import us.nineworlds.serenity.core.services.WatchedVideoJob
import us.nineworlds.serenity.core.util.AndroidHelper
import java.io.Serializable
import javax.inject.Inject

abstract class AbstractVideoContentInfo(private val resources: Resources?) : VideoContentInfo,
    Serializable {

    @Inject
    internal lateinit var androidHelper: AndroidHelper

    init {
        val scope = Toothpick.openScope(InjectionConstants.APPLICATION_SCOPE)
        Toothpick.inject(this, scope)
    }

    private var _id: String? = null
    private var _type: Types? = null

    private var _plotSummary: String? = null
    private var _posterURL: String? = null
    private var _backgroundURL: String? = null
    private var _title: String? = null
    private var _originalTitle: String? = null
    private var _mediaTagIdentifier: String? = null

    override fun id(): String? = _id

    override fun getType(): Types? = _type

    override fun setType(type: Types?) {
        this._type = type
    }

    override fun getSummary(): String? = _plotSummary

    override fun getBackgroundURL(): String? = _backgroundURL

    override fun getImageURL(): String? = _posterURL

    override fun getTitle(): String? = _title

    override fun setTitle(title: String?) {
        this._title = title
    }

    override fun setImageURL(imageURL: String?) {
        this._posterURL = imageURL
    }

    override fun setSummary(summary: String?) {
        this._plotSummary = summary
    }

    override fun setBackgroundURL(backgroundURL: String?) {
        this._backgroundURL = backgroundURL
    }

    override fun setId(id: String?) {
        this._id = id
    }

    override fun getMediaTagIdentifier(): String? = _mediaTagIdentifier

    override fun setMediaTagIdentifier(mediaTagIdentifier: String?) {
        this._mediaTagIdentifier = mediaTagIdentifier
    }

    override val longTitle: String?
        get() {
            val seriesTitle = seriesTitle
            return if (seriesTitle == null) {
                _title
            } else resources?.getString(
                R.string.long_title,
                seriesTitle,
                seasonNumber,
                episodeNumber,
                _title
            )
        }
    override var directPlayUrl: String? = null
    override var actors: List<String>? = null
    override var audioCodec: String? = null
    override var castInfo: String? = null
    override var contentRating: String? = null
    override var directors: List<String>? = null
    override var genres: List<String>? = null
    override var videoCodec: String? = null
    override var videoResolution: String? = null
    override var writers: List<String>? = null
    override var year: String? = null
    override var aspectRatio: String? = null
    override var viewCount: Int = 0
    override var container: String? = null
    override var parentPosterURL: String? = null
    override var grandParentPosterURL: String? = null
    override var audioChannels: String? = null
    override var resumeOffset: Int = 0
    override var duration: Int = 0
    override val season: String?
        get() = resources?.getString(R.string.season_) + seasonNumber
    override var seasonNumber: Int = 0
    override val episode: String?
        get() = resources?.getString(R.string.episode_) + episodeNumber
    override var episodeNumber: Int = 0
    override var originalAirDate: String? = null
    override var seriesTitle: String? = null
    override var subtitle: Subtitle? = null
    override var studio: String? = null
    override var rating: Double = 0.0
    override var parentKey: String? = null
    override val isPartiallyWatched: Boolean
        get() {
            if (resumeOffset > 0) {
                val percentWatched = viewedPercentage()
                if (percentWatched <= SerenityConstants.WATCHED_PERCENT) {
                    return true
                }
            }
            return false
        }
    override val isWatched: Boolean
        get() {
            val percentWatched = viewedPercentage()
            if (percentWatched > SerenityConstants.WATCHED_PERCENT) {
                return true
            }
            return if (resumeOffset == 0 && viewCount > 0) {
                true
            } else false
        }
    override val isUnwatched: Boolean
        get() = if (viewCount == 0) {
            true
        } else false

    override fun viewedPercentage(): Float {
        val duration = duration.toFloat()
        val offset = resumeOffset.toFloat()
        if (duration == 0f) {
            return 0f
        }
        return offset / duration
    }

    override fun toggleWatchStatus() {
        if (isPartiallyWatched || isUnwatched) {
            WatchedVideoJob.updateWatchedStatus(id().orEmpty())
            viewCount++
            return
        }
        UnWatchVideoJob.markUnwatched(id().orEmpty())
        viewCount = 0
    }

    private var _hasTrailer: Boolean = false

    override fun hasTrailer(): Boolean = _hasTrailer

    override fun setTrailer(trailer: Boolean) {
        _hasTrailer = trailer
    }

    private var _trailerId: String? = null
    override fun trailerId(): String? = _trailerId

    override fun setTrailerId(id: String?) {
        _trailerId = id
    }

    override var availableSubtitles: List<Subtitle>? = null
    override var tagLine: String? = null
    override var seriesName: String? = null

    internal open fun isDirectPlaySupported(media: IMedia): Boolean {
        val audioCodec = media.audioCodec.orEmpty()
        val hasStandardAudioSupport =
            MediaCodecInfoUtil.isCodecSupported(MediaCodecInfoUtil.findCorrectAudioMimeType("audio/$audioCodec"))
        val hasPassthroughAudioSupport = androidHelper.isAudioPassthroughSupported(audioCodec)
        val isAudioCodecSupported = hasStandardAudioSupport || hasPassthroughAudioSupport

        val videoCodec = media.videoCodec.orEmpty()
        val isVideoSupported =
            MediaCodecInfoUtil.isCodecSupported(MediaCodecInfoUtil.findCorrectVideoMimeType("video/$videoCodec"))

        return isAudioCodecSupported && isVideoSupported
    }


    companion object {
        private const val serialVersionUID = 4744447508883279194L
    }
}

