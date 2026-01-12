package us.nineworlds.serenity.ui.video.player

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.res.Resources
import android.media.AudioManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.KeyEvent
import android.view.View
import android.widget.FrameLayout
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.Format
import androidx.media3.common.MediaItem
import androidx.media3.common.MimeTypes
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.TrackSelectionParameters
import androidx.media3.common.Tracks
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DataSource
import androidx.media3.datasource.DataSourceBitmapLoader
import androidx.media3.exoplayer.DefaultLoadControl
import androidx.media3.exoplayer.DefaultRenderersFactory
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.Renderer
import androidx.media3.exoplayer.audio.DefaultAudioSink
import androidx.media3.exoplayer.audio.DefaultAudioTrackBufferSizeProvider
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import androidx.media3.exoplayer.source.MediaSource
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import androidx.media3.exoplayer.text.TextOutput
import androidx.media3.exoplayer.text.TextRenderer
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector
import androidx.media3.exoplayer.trackselection.TrackSelector
import androidx.media3.extractor.DefaultExtractorsFactory
import androidx.media3.extractor.text.DefaultSubtitleParserFactory
import androidx.media3.extractor.text.SubtitleParser
import androidx.media3.extractor.ts.DefaultTsPayloadReaderFactory
import androidx.media3.ui.PlayerControlView
import androidx.media3.ui.PlayerView
import androidx.media3.ui.TrackNameProvider
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import timber.log.Timber
import toothpick.Toothpick
import us.nineworlds.serenity.R
import us.nineworlds.serenity.common.annotations.InjectionConstants
import us.nineworlds.serenity.common.annotations.OpenForTesting
import us.nineworlds.serenity.core.logger.Logger
import us.nineworlds.serenity.core.model.VideoContentInfo
import us.nineworlds.serenity.core.util.AndroidHelper
import us.nineworlds.serenity.core.util.TimeUtil
import us.nineworlds.serenity.databinding.ActivityExoplayerVideoBinding
import us.nineworlds.serenity.injection.AppInjectionConstants
import us.nineworlds.serenity.injection.modules.ExoplayerVideoModule
import us.nineworlds.serenity.ui.activity.SerenityActivity
import us.nineworlds.serenity.ui.util.DisplayUtils.overscanCompensation
import java.util.Locale
import javax.inject.Inject
import javax.inject.Provider

@UnstableApi
@OpenForTesting
class ExoplayerVideoActivity :
    SerenityActivity(),
    ExoplayerContract.ExoplayerView {

    @Inject
    lateinit var mediaDataSourceFactory: DataSource.Factory

    @Inject
    lateinit var trackSelector: TrackSelector

    @InjectPresenter
    lateinit var presenter: ExoplayerPresenter

    @Inject
    lateinit var presenterProvider: Provider<ExoplayerPresenter>

    @Inject
    lateinit var log: Logger

    @Inject
    lateinit var androidHelper: AndroidHelper

    @Inject
    lateinit var timeUtil: TimeUtil

    internal lateinit var playerView: PlayerView

    internal lateinit var dataLoadingContainer: FrameLayout

    lateinit var player: ExoPlayer

    private val progressReportinghandler = Handler(Looper.getMainLooper())
    private val progressRunnable = ProgressRunnable()
    private var videoKeyHandler: VideoKeyCodeHandlerDelegate? = null
    private var autoResume: Boolean = false

    override fun screenName(): String = "Exoplayer Video Player"

    @ProvidePresenter
    fun providePresenter(): ExoplayerPresenter = presenterProvider.get()

    override fun inject() {
        scope = Toothpick.openScopes(InjectionConstants.APPLICATION_SCOPE, AppInjectionConstants.EXOPLAYER_SCOPE)
        scope.installModules(ExoplayerVideoModule())
        Toothpick.inject(this, scope)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityExoplayerVideoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        playerView = binding.playerView

        dataLoadingContainer = findViewById(R.id.data_loading_container)

        overscanCompensation(this, window.decorView)
        val intent = this.intent
        intent?.let { intent ->
            autoResume = intent.getBooleanExtra("autoResume", false)
        }
    }

    public override fun onStart() {
        super.onStart()
        if (androidHelper.buildNumber() > Build.VERSION_CODES.M) {
            presenter.playBackFromVideoQueue(autoResume)
        }
    }

    public override fun onResume() {
        super.onResume()
        if (androidHelper.buildNumber() <= Build.VERSION_CODES.M) {
            presenter.playBackFromVideoQueue(autoResume)
        }
    }

    public override fun onPause() {
        super.onPause()
        if (androidHelper.buildNumber() <= Build.VERSION_CODES.M) {
            pause()
            releasePlayer()
            progressReportinghandler.removeCallbacks(progressRunnable)
        }
    }

    public override fun onStop() {
        super.onStop()
        if (androidHelper.buildNumber() > Build.VERSION_CODES.M) {
            pause()
            releasePlayer()
            progressReportinghandler.removeCallbacks(progressRunnable)
        }
    }

    override fun finish() {
        setExitResultCodeFinished()
        progressReportinghandler.removeCallbacks(progressRunnable)
        Toothpick.closeScope(scope)
        super.finish()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_HOME) {
            pauseAndReleaseVideo()
            finish()
            return true
        }

        if (videoKeyHandler!!.onKeyDown(keyCode, event)) {
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun play() {
        player.playWhenReady = true
        presenter.startPlaying()
        progressReportinghandler.postDelayed(progressRunnable, Companion.PROGRESS_UPDATE_DELAY.toLong())
    }

    override fun pause() {
        player.playWhenReady = false
        presenter.stopPlaying(player.currentPosition)
        progressReportinghandler.removeCallbacks(progressRunnable)
    }

    override fun initializePlayer(videoUrl: String, offset: Int) {
        log.debug("Direct Play URL: " + videoUrl)
        player = createSimpleExoplayer()
        player.addListener(PlayerListener())

        playerView.player = player
        playerView.setControllerVisibilityListener(presenter)

        (playerView.player as ExoPlayer).playWhenReady = true

        val mediaSource: MediaSource = buildMediaSource(Uri.parse(videoUrl))

        videoKeyHandler = VideoKeyCodeHandlerDelegate(player, this, presenter)

        player.prepare(mediaSource, offset <= 0, false)
        log.debug("Player offset: $offset")
        if (offset > 0) {
            player.seekTo(offset.toLong())
        }
        progressReportinghandler.postDelayed(progressRunnable, Companion.PROGRESS_UPDATE_DELAY.toLong())
    }

    internal fun createSimpleExoplayer(): ExoPlayer {
        val tunnelingEnabled = androidHelper.enableTunneling()
        val audioManager = this.getSystemService(Context.AUDIO_SERVICE) as AudioManager

        // 1. Generate Session ID for tunneling
        val audioSessionId = if (tunnelingEnabled) audioManager.generateAudioSessionId() else C.AUDIO_SESSION_ID_UNSET

        // 2. Simplified RenderersFactory (no custom sink provider needed)
        val renderersFactory = object : DefaultRenderersFactory(this) {
            override fun buildTextRenderers(
                context: Context,
                output: TextOutput,
                outputLooper: Looper,
                extensionRendererMode: Int,
                out: ArrayList<Renderer>
            ) {
                // By using the standard constructor, the TextRenderer
                // defaults to legacy mode which handles raw samples.

                val renderer = TextRenderer(output, outputLooper)
                // In 1.9.0, if the setter is missing, use the experimental method:
                renderer.experimentalSetLegacyDecodingEnabled(true)
                out.add(renderer)

            }
        }.setEnableDecoderFallback(true)
         .setEnableAudioOutputPlaybackParameters(true)

        if (trackSelector is DefaultTrackSelector) {
            // 3. Configure Offload Mode based on Tunneling
            val offloadMode = if (tunnelingEnabled) {
                // Force OFF for TCL tunneling stability
                TrackSelectionParameters.AudioOffloadPreferences.AUDIO_OFFLOAD_MODE_DISABLED
            } else {
                // Allow for standard playback
                TrackSelectionParameters.AudioOffloadPreferences.AUDIO_OFFLOAD_MODE_ENABLED
            }

            val audioOffloadPreferences = TrackSelectionParameters.AudioOffloadPreferences.Builder()
                .setAudioOffloadMode(offloadMode)
                .setIsSpeedChangeSupportRequired(false)
                .setIsGaplessSupportRequired(false)
                .build()

            val parameters = DefaultTrackSelector.Parameters.Builder()
                .setTunnelingEnabled(tunnelingEnabled)
                .setAudioOffloadPreferences(audioOffloadPreferences)
                .setAllowAudioMixedDecoderSupportAdaptiveness(true)
                .setAllowAudioMixedSampleRateAdaptiveness(true)
                .setConstrainAudioChannelCountToDeviceCapabilities(true)
                .setPreferredTextLanguage(null)
                .setIgnoredTextSelectionFlags(C.SELECTION_FLAG_DEFAULT)
//                .setTrackTypeDisabled(C.TRACK_TYPE_TEXT, false)
                .setSelectUndeterminedTextLanguage(true)
                //.setTrackTypeDisabled(C.TRACK_TYPE_TEXT, true)
                .setPreferredAudioMimeTypes(
                    MimeTypes.AUDIO_AC3,
                    MimeTypes.AUDIO_E_AC3,
                    MimeTypes.AUDIO_TRUEHD,
                    MimeTypes.AUDIO_DTS,
                    MimeTypes.AUDIO_AAC
                )
                .build()

            trackSelector.parameters = parameters
        }

        val defaultLoadControl = DefaultLoadControl.Builder()
            .setBufferDurationsMs(
                15000, // minBufferMs (15s) - Must be >= bufferForPlaybackAfterRebufferMs
                50000, // maxBufferMs (50s)
                2500,  // bufferForPlaybackMs (2.5s)
                5000   // bufferForPlaybackAfterRebufferMs (5s)
            )
            .setTargetBufferBytes(40 * 1024 * 1024)
            .build()

        val audioAttributes = AudioAttributes.Builder()
            .setUsage(C.USAGE_MEDIA)
            .setContentType(C.AUDIO_CONTENT_TYPE_MOVIE)
            .build()



        val player = ExoPlayer.Builder(this)
            .setRenderersFactory(renderersFactory)
            .setTrackSelector(trackSelector)
            .setLoadControl(defaultLoadControl)
            .setAudioAttributes(audioAttributes, true)
            .build()

        // 5. Apply Session ID immediately
        if (tunnelingEnabled && audioSessionId != C.AUDIO_SESSION_ID_UNSET) {
            player.setAudioSessionId(audioSessionId)
        }

        return player
    }


    internal fun buildMediaSource(uri: Uri): MediaSource {
        val mediaItem = MediaItem.fromUri(uri)
        val extractorsFactory = DefaultExtractorsFactory()
            .setTsExtractorFlags(DefaultTsPayloadReaderFactory.FLAG_DETECT_ACCESS_UNITS)
            .setSubtitleParserFactory(SubtitleParser.Factory.UNSUPPORTED)


        val mediaSourceFactory = ProgressiveMediaSource.Factory(mediaDataSourceFactory, extractorsFactory)
            .experimentalParseSubtitlesDuringExtraction(false)


        return mediaSourceFactory.createMediaSource(mediaItem)
    }

    internal fun releasePlayer() {
        player.stop()
        player.clearVideoSurface()
        player.release()
    }

    override fun hideController() {
        playerView.hideController()
    }

    override fun showController() {
        playerView.showController()
    }

    protected fun setExitResultCodeFinished() {
        val returnIntent = Intent()
        returnIntent.putExtra("position", player.currentPosition.toInt())
        if (parent == null) {
            setResult(Activity.RESULT_OK, returnIntent)
        } else {
            parent.setResult(Activity.RESULT_OK, returnIntent)
        }
    }

    override fun hideLoadingProgress() {
        dataLoadingContainer.visibility = View.GONE
    }

    override fun showLoadingProgress() {
        dataLoadingContainer.visibility = View.VISIBLE
    }

    override fun playbackEnded() {
        releasePlayer()
        finish()
    }

    override fun onBackPressed() {
        pauseAndReleaseVideo()
        super.onBackPressed()
    }

    private fun pauseAndReleaseVideo() {
        if (player.playbackState == Player.STATE_READY ||
            player.playbackState == Player.STATE_BUFFERING
        ) {
            pause()
            releasePlayer()
        }
    }

    class SubtitleTrackNameProvider(val resources: Resources) : TrackNameProvider {
        override fun getTrackName(format: Format): String {
            val lang = format.language
            return if (!lang.isNullOrEmpty()) {
                // Converts "en" to "English", "es" to "Spanish", etc.
                Locale(lang).displayLanguage.replaceFirstChar { it.uppercase() }
            } else {
                "Unknown Subtitle"
            }
        }
    }

    protected inner class ProgressRunnable : Runnable {

        override fun run() {
            try {
                if (!player.playWhenReady) {
                    return
                }

                val percentage = player.currentPosition.toFloat() / player.getDuration().toFloat()
                if (percentage <= 90f) {
                    presenter.updateServerPlaybackPosition(player.currentPosition)
                    progressReportinghandler.postDelayed(this, Companion.PROGRESS_UPDATE_DELAY)
                    return
                }
                presenter.updateWatchedStatus()
            } catch (ex: IllegalStateException) {
                log.error("Illegalstate exception occurred durring progress update. No further updates will occur.", ex)
            }
        }
    }

    override fun showResumeDialog(video: VideoContentInfo) {
        val alertDialogBuilder =
            AlertDialog.Builder(this, android.R.style.Theme_Holo_Dialog)

        alertDialogBuilder.setTitle(R.string.resume_video)
        alertDialogBuilder.setMessage(
            resources.getText(R.string.resume_the_video_from_).toString() + timeUtil.formatDuration(
                video.resumeOffset.toLong()
            ) + resources.getText(R.string._or_restart_)
        )
            .setCancelable(false)
            .setPositiveButton(R.string.resume) { _, _ -> presenter.playVideo() }
            .setNegativeButton(R.string.restart) { _, _ ->
                video.resumeOffset = 0
                presenter.playVideo()
            }

        alertDialogBuilder.create()
        val dialog = alertDialogBuilder.show()
        dialog.getButton(DialogInterface.BUTTON_POSITIVE).requestFocusFromTouch()
    }

    companion object {
        internal const val PROGRESS_UPDATE_DELAY = 10000L
    }

    inner class PlayerListener : Player.Listener {
        override fun onPlaybackStateChanged(playbackState: Int) {
            if (Player.STATE_ENDED == playbackState) {
                playbackEnded()
            }
        }

        override fun onPlayerError(error: PlaybackException) {
            // We look for Decoder Init or Audio Track Init failures,
            // which are the classic symptoms of tunneling hardware issues.
            val isDecoderFailure = error.errorCode == PlaybackException.ERROR_CODE_DECODER_INIT_FAILED
            val isAudioTrackFailure = error.errorCode == PlaybackException.ERROR_CODE_AUDIO_TRACK_INIT_FAILED

            if (isDecoderFailure || isAudioTrackFailure) {
                val currentParameters = trackSelector.parameters as DefaultTrackSelector.Parameters


                // Only attempt fallback if tunneling is actually currently enabled
                if (currentParameters.tunnelingEnabled) {
                    Timber.w( "Tunneling failed for this stream. Falling back to standard playback.")

                    // 1. Save current state
                    val currentMediaItem = player.currentMediaItem
                    val currentPosition = player.currentPosition
                    val playWhenReady = player.playWhenReady

                    // 2. Update parameters to disable tunneling
                    trackSelector.parameters = currentParameters.buildUpon()
                        .setTunnelingEnabled(false)
                        .build()

                    // 3. Re-prepare the player
                    currentMediaItem?.let {
                        player.setMediaItem(it, currentPosition)
                        player.prepare()
                        player.playWhenReady = playWhenReady
                    }
                }
            }
        }

        override fun onTracksChanged(tracks: Tracks) {
            Timber.d( "--- Track Discovery Start ---")

            for (trackGroup in tracks.groups) {
                // We only care about Text (Subtitles) for this debug
                if (trackGroup.type == C.TRACK_TYPE_TEXT) {
                    val groupInfo = trackGroup.mediaTrackGroup
                    Timber.d( "Found Subtitle Group: ${groupInfo.id} (Length: ${trackGroup.length})")

                    for (i in 0 until trackGroup.length) {
                        val format = trackGroup.getTrackFormat(i)
                        val isSupported = trackGroup.isTrackSupported(i)
                        val isSelected = trackGroup.isTrackSelected(i)

                        // Metadata flags
                        val isForced = (format.selectionFlags and C.SELECTION_FLAG_FORCED) != 0
                        val isDefault = (format.selectionFlags and C.SELECTION_FLAG_DEFAULT) != 0

                        Timber.d( """
                        |   [Track $i] 
                        |   - Label: ${format.label ?: "No Label"}
                        |   - Language: ${format.language ?: "und"}
                        |   - MimeType: ${format.sampleMimeType}
                        |   - Supported by Device: $isSupported
                        |   - Currently Selected: $isSelected
                        |   - Forced Flag: $isForced
                        |   - Default Flag: $isDefault
                    """.trimMargin())
                    }
                }
            }
            Timber.d("--- Track Discovery End ---")        }

    }
}
