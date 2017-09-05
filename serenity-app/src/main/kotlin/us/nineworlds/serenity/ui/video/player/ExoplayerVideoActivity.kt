package us.nineworlds.serenity.ui.video.player

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import butterknife.BindView
import butterknife.ButterKnife.bind
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.trackselection.MappingTrackSelector
import com.google.android.exoplayer2.ui.SimpleExoPlayerView
import com.google.android.exoplayer2.upstream.DataSource
import us.nineworlds.serenity.OpenForTesting
import us.nineworlds.serenity.R
import us.nineworlds.serenity.injection.VideoPlayerHandler
import us.nineworlds.serenity.ui.activity.SerenityActivity
import us.nineworlds.serenity.ui.util.DisplayUtils.overscanCompensation
import javax.inject.Inject
import javax.inject.Provider

@OpenForTesting
class ExoplayerVideoActivity : SerenityActivity(), ExoplayerContract.ExoplayerView {

  @Inject internal lateinit var mediaDataSourceFactory: DataSource.Factory
  @field:[Inject VideoPlayerHandler] internal lateinit var videoPlayerHandler: Handler
  @Inject internal lateinit var trackSelector: MappingTrackSelector
  @Inject internal lateinit var eventLogger: EventLogger

  @InjectPresenter
  internal lateinit var presenter: ExoplayerPresenter

  @Inject protected lateinit var presenterProvider: Provider<ExoplayerPresenter>

  @BindView(R.id.player_view)
  internal lateinit var playerView: SimpleExoPlayerView

  internal var player: SimpleExoPlayer? = null

  @ProvidePresenter
  fun providePresenter(): ExoplayerPresenter = presenterProvider.get()

  override fun createSideMenu() {
    // Do Nothing as a side menu isn't needed in this activity
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_exoplayer_video)
    bind(this)
    overscanCompensation(this, window.decorView)
  }

  public override fun onStart() {
    super.onStart()
    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
      presenter.playBackFromVideoQueue()
    }
  }

  public override fun onResume() {
    super.onResume()
    if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M || player == null) {
      presenter.playBackFromVideoQueue()
    }
  }

  public override fun onPause() {
    super.onPause()
    if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
      releasePlayer()
    }
  }

  public override fun onStop() {
    super.onStop()
    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
      releasePlayer()
    }
  }

  public override fun onNewIntent(intent: Intent?) {
    releasePlayer()
    setIntent(intent)
  }

  override fun initializePlayer(videoUrl: String) {
    Log.d("ExoPlayerVideoActivity", "Plex Direct Play URL: " + videoUrl)
    player = createSimpleExoplayer()
    player?.addListener(eventLogger)
    player?.setAudioDebugListener(eventLogger)
    player?.setVideoDebugListener(eventLogger)
    player?.setMetadataOutput(eventLogger)

    playerView.player = player
    player?.playWhenReady = true

    val mediaSource: MediaSource = buildMediaSource(Uri.parse(videoUrl))
    player?.prepare(mediaSource)
  }

  internal fun createSimpleExoplayer() = ExoPlayerFactory.newSimpleInstance(this, trackSelector)

  internal fun buildMediaSource(uri: Uri): MediaSource {
    return ExtractorMediaSource(uri, mediaDataSourceFactory, DefaultExtractorsFactory(),
        videoPlayerHandler, eventLogger)
  }

  internal fun releasePlayer() {
    if (player != null) {
      player?.release()
      player = null
    }
  }
}

