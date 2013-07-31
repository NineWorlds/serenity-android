package us.nineworlds.serenity.ui.browser.music.tracks;

import android.media.MediaPlayer;
import android.widget.MediaController.MediaPlayerControl;

/**
 * A simple media player control. Handles the main events that can occur
 * while using the player to play a video.
 * 
 * @author dcarver
 * 
 */
public class AudioMediaPlayerControl implements MediaPlayerControl {
	MediaPlayer mediaPlayer;
	
	public AudioMediaPlayerControl(MediaPlayer p) {
		mediaPlayer = p;
	}
	
	@Override
	public void start() {
		mediaPlayer.start();
	}

	@Override
	public void seekTo(int pos) {
		mediaPlayer.seekTo(pos);
	}

	@Override
	public void pause() {
		mediaPlayer.pause();
	}

	@Override
	public boolean isPlaying() {
		return mediaPlayer.isPlaying();
	}

	@Override
	public int getDuration() {
		return mediaPlayer.getDuration();
	}

	@Override
	public int getCurrentPosition() {
		return mediaPlayer.getCurrentPosition();
	}

	@Override
	public int getBufferPercentage() {
		return 0;
	}

	@Override
	public boolean canSeekForward() {
		return true;
	}

	@Override
	public boolean canSeekBackward() {
		return true;
	}

	@Override
	public boolean canPause() {
		return true;
	}

}
