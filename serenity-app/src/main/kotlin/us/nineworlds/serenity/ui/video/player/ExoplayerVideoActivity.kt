package us.nineworlds.serenity.ui.video.player

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import butterknife.BindView
import butterknife.ButterKnife.bind
import com.arellomobile.mvp.presenter.InjectPresenter
import us.nineworlds.serenity.R
import us.nineworlds.serenity.core.model.VideoContentInfo
import us.nineworlds.serenity.injection.VideoPlayerHandler
import us.nineworlds.serenity.ui.activity.SerenityActivity
import us.nineworlds.serenity.ui.util.DisplayUtils.overscanCompensation
import javax.inject.Inject


class ExoplayerVideoActivity : SerenityActivity(), ExoplayerContract.ExoplayerView {

  @Inject lateinit var prefs: SharedPreferences
  @Inject lateinit var mediaDataSourceFactory: DataSource.Factory
  @field:[Inject VideoPlayerHandler] lateinit var videoPlayerHandler: Handler
  @Inject lateinit var trackSelector: MappingTrackSelector
  @Inject lateinit var eventLogger: EventLogger

  @InjectPresenter
  lateinit var presenter: ExoplayerPresenter

  @BindView(R.id.player_view)
  lateinit var playerView: SimpleExoPlayerView

  var player: SimpleExoPlayer? = null


  override fun createSideMenu() {
    // Do Nothing as a side menu isn't needed in this activity
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_exoplayer_video)
    bind(this)
    overscanCompensation(this, window.decorView)
  }

  override fun onStart() {
    super.onStart()
    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
      presenter.playBackFromVideoQueue()
    }
  }

  override fun onResume() {
    super.onResume()
    if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M || player == null) {
      presenter.playBackFromVideoQueue()
    }
  }

  override fun onPause() {
    super.onPause()
    if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
      releasePlayer()
    }
  }

  override fun onStop() {
    super.onStop()
    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
      releasePlayer()
    }
  }

  override fun onNewIntent(intent: Intent?) {
    releasePlayer()
    setIntent(intent)
  }

  override fun initializePlayer(video: VideoContentInfo) {
    val renderersFactory = DefaultRenderersFactory(this,
        null, DefaultRenderersFactory.EXTENSION_RENDERER_MODE_OFF)
    player = ExoPlayerFactory.newSimpleInstance(renderersFactory, trackSelector)
    player?.addListener(eventLogger)
    player?.setAudioDebugListener(eventLogger)
    player?.setVideoDebugListener(eventLogger)
    player?.setMetadataOutput(eventLogger)

    playerView.player = player
    player?.playWhenReady = true

    val mediaSource: MediaSource = buildMediaSource(Uri.parse(video.directPlayUrl))
    player?.prepare(mediaSource)
  }

  fun buildMediaSource(uri: Uri): MediaSource {
    return ExtractorMediaSource(uri, mediaDataSourceFactory, DefaultExtractorsFactory(),
        videoPlayerHandler, eventLogger)
  }

  private fun releasePlayer() {
    if (player != null) {
      player?.release();
      player = null;
    }
  }
}

