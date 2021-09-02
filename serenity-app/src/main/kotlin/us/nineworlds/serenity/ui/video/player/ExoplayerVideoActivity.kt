package us.nineworlds.serenity.ui.video.player

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.KeyEvent
import android.view.View
import android.widget.FrameLayout
import butterknife.BindView
import butterknife.ButterKnife.bind
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.TrackSelector
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DataSource
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
import us.nineworlds.serenity.injection.AppInjectionConstants
import us.nineworlds.serenity.injection.modules.ExoplayerVideoModule
import us.nineworlds.serenity.ui.activity.SerenityActivity
import us.nineworlds.serenity.ui.util.DisplayUtils.overscanCompensation
import javax.inject.Inject
import javax.inject.Provider

@OpenForTesting
class ExoplayerVideoActivity : SerenityActivity(), ExoplayerContract.ExoplayerView {

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

  @BindView(R.id.player_view)
  internal lateinit var playerView: PlayerView

  @BindView(R.id.data_loading_container)
  internal lateinit var dataLoadingContainer: FrameLayout

  lateinit var player: SimpleExoPlayer

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
    setContentView(R.layout.activity_exoplayer_video)
    bind(this)
    overscanCompensation(this, window.decorView)
    val intent = this.intent
    intent?.let {intent ->
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
    if ( androidHelper.buildNumber() <= Build.VERSION_CODES.M) {
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
    player.addListener(presenter)

    playerView.player = player
    playerView.setControllerVisibilityListener(presenter)

    (playerView.player as SimpleExoPlayer).playWhenReady = true

    val mediaSource: MediaSource = buildMediaSource(Uri.parse(videoUrl))

    videoKeyHandler = VideoKeyCodeHandlerDelegate(player, this, presenter)

    player.prepare(mediaSource, offset <= 0, false)
    log.debug("Player offset: $offset")
    if (offset > 0) {
      player.seekTo(offset.toLong())
    }
    progressReportinghandler.postDelayed(progressRunnable, Companion.PROGRESS_UPDATE_DELAY.toLong())
  }

  internal fun createSimpleExoplayer(): SimpleExoPlayer {
    if (trackSelector is DefaultTrackSelector) {
      Timber.d("Enabling Tunneling Mode")
      val parameters = DefaultTrackSelector.ParametersBuilder(this).setTunnelingEnabled(true).build()
      (trackSelector as DefaultTrackSelector).parameters = parameters
    }

    return SimpleExoPlayer.Builder(this)
            .setTrackSelector(trackSelector)
            .build()
  }

  internal fun buildMediaSource(uri: Uri): MediaSource {
    val mediaItem = MediaItem.fromUri(uri)
    val mediaSourceFactory = ProgressiveMediaSource.Factory(mediaDataSourceFactory, DefaultExtractorsFactory())

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
}

