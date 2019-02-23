package us.nineworlds.serenity.ui.video.player

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.KeyEvent
import android.view.View
import android.widget.FrameLayout
import butterknife.BindView
import butterknife.ButterKnife.bind
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.MappingTrackSelector
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DataSource
import timber.log.Timber
import toothpick.Toothpick
import us.nineworlds.serenity.R
import us.nineworlds.serenity.common.annotations.InjectionConstants
import us.nineworlds.serenity.common.annotations.OpenForTesting
import us.nineworlds.serenity.core.logger.Logger
import us.nineworlds.serenity.core.util.AndroidHelper
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
  lateinit var trackSelector: MappingTrackSelector

  @InjectPresenter
  lateinit var presenter: ExoplayerPresenter

  @Inject
  lateinit var presenterProvider: Provider<ExoplayerPresenter>

  @Inject
  lateinit var log: Logger

  @Inject
  lateinit var androidHelper: AndroidHelper

  @BindView(R.id.player_view)
  internal lateinit var playerView: PlayerView

  @BindView(R.id.data_loading_container)
  internal lateinit var dataLoadingContainer: FrameLayout

  lateinit var player: SimpleExoPlayer

  val videoPlayerHandler = Handler()

  private val progressReportinghandler = Handler()
  private val progressRunnable = ProgressRunnable()
  private var videoKeyHandler: VideoKeyCodeHandlerDelegate? = null

  internal val PROGRESS_UPDATE_DELAY = 10000L

  override fun screenName(): String = "Exoplayer Video Player"

  @ProvidePresenter
  fun providePresenter(): ExoplayerPresenter = presenterProvider.get()

  override fun createSideMenu() {
    // Do Nothing as a side menu isn't needed in this activity
  }

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
  }

  public override fun onStart() {
    super.onStart()
    if (androidHelper.buildNumber() > Build.VERSION_CODES.M) {
      presenter.playBackFromVideoQueue()
    }
  }

  public override fun onResume() {
    super.onResume()
    if (androidHelper.buildNumber() <= Build.VERSION_CODES.M) {
      presenter.playBackFromVideoQueue()
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

  public override fun onNewIntent(intent: Intent?) {
    releasePlayer()
    setIntent(intent)
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
    progressReportinghandler.postDelayed(progressRunnable, PROGRESS_UPDATE_DELAY.toLong())
  }

  override fun pause() {
    player.playWhenReady = false
    presenter.stopPlaying()
    progressReportinghandler.removeCallbacks(progressRunnable)
  }

  override fun initializePlayer(videoUrl: String, offset: Int) {
    log.debug("Direct Play URL: " + videoUrl)
    player = createSimpleExoplayer()
    player.addListener(presenter)

    playerView.player = player
    playerView.setControllerVisibilityListener(presenter)

    playerView.player.playWhenReady = true

    val mediaSource: MediaSource = buildMediaSource(Uri.parse(videoUrl))


    videoKeyHandler = VideoKeyCodeHandlerDelegate(player, this, presenter)
    progressReportinghandler.postDelayed(progressRunnable, PROGRESS_UPDATE_DELAY.toLong())

    if (offset > 0) {
      player.seekTo(offset.toLong())
    }
    player.prepare(mediaSource, offset <= 0, false)
  }

  internal fun createSimpleExoplayer(): SimpleExoPlayer {
    if (trackSelector is DefaultTrackSelector) {
      Timber.d("Enabling Tunneling Mode")
      (trackSelector as DefaultTrackSelector).setTunnelingAudioSessionId(C.generateAudioSessionIdV21(this))
    }
    return ExoPlayerFactory.newSimpleInstance(this, trackSelector)
  }

  internal fun buildMediaSource(uri: Uri): MediaSource = ExtractorMediaSource(
    uri, mediaDataSourceFactory,
    DefaultExtractorsFactory(),
    videoPlayerHandler, null
  )

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
          progressReportinghandler.postDelayed(this, PROGRESS_UPDATE_DELAY)
          return
        }
        presenter.updateWatchedStatus()
      } catch (ex: IllegalStateException) {
        log.error("Illegalstate exception occurred durring progress update. No further updates will occur.", ex)
      }
    }
  }
}

