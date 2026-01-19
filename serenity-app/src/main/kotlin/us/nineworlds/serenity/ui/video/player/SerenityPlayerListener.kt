package us.nineworlds.serenity.ui.video.player

import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector
import timber.log.Timber

@UnstableApi
class SerenityPlayerListener(private val presenter: ExoplayerPresenter, private val videoactivity: ExoplayerVideoActivity) : Player.Listener {

    override fun onPlaybackStateChanged(playbackState: Int) {
        if (Player.STATE_ENDED == playbackState) {
            videoactivity.playbackEnded()
        }
    }

    override fun onPlayerError(error: PlaybackException) {
        // We look for Decoder Init or Audio Track Init failures,
        // which are the classic symptoms of tunneling hardware issues.
        val isDecoderFailure = error.errorCode == PlaybackException.ERROR_CODE_DECODER_INIT_FAILED
        val isAudioTrackFailure = error.errorCode == PlaybackException.ERROR_CODE_AUDIO_TRACK_INIT_FAILED

        if (isDecoderFailure || isAudioTrackFailure) {
            val currentParameters = videoactivity.trackSelector.parameters as DefaultTrackSelector.Parameters

            // Only attempt fallback if tunneling is actually currently enabled
            if (currentParameters.tunnelingEnabled) {
                Timber.w("Tunneling failed for this stream. Falling back to standard playback.")

                // 1. Save current state
                val currentMediaItem = videoactivity.player.currentMediaItem
                val currentPosition = videoactivity.player.currentPosition
                val playWhenReady = videoactivity.player.playWhenReady

                // 2. Update parameters to disable tunneling
                videoactivity.trackSelector.parameters = currentParameters.buildUpon()
                    .setTunnelingEnabled(false)
                    .build()

                // 3. Re-prepare the player
                currentMediaItem?.let {
                    videoactivity.player.setMediaItem(it, currentPosition)
                    videoactivity.player.prepare()
                    videoactivity.player.playWhenReady = playWhenReady
                }
            }
        }
    }
}
