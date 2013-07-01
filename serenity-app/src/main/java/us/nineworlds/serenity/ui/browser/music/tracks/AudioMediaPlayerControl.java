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
	
	public void start() {
		mediaPlayer.start();
	}

	public void seekTo(int pos) {
		mediaPlayer.seekTo(pos);
	}

	public void pause() {
		mediaPlayer.pause();
	}

	public boolean isPlaying() {
		return mediaPlayer.isPlaying();
	}

	public int getDuration() {
		return mediaPlayer.getDuration();
	}

	public int getCurrentPosition() {
		return mediaPlayer.getCurrentPosition();
	}

	public int getBufferPercentage() {
		return 0;
	}

	public boolean canSeekForward() {
		return true;
	}

	public boolean canSeekBackward() {
		return true;
	}

	public boolean canPause() {
		return true;
	}

}
