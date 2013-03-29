package us.nineworlds.serenity.ui.video.player;

import android.media.MediaPlayer;

/**
 * A simple media player control. Handles the main events that can occur
 * while using the player to play a video.
 * 
 * @author dcarver
 * 
 */
public class SerenityMediaPlayerControl implements MediaPlayerControl {
	MediaPlayer mediaPlayer;
	
	public SerenityMediaPlayerControl(MediaPlayer p) {
		mediaPlayer = p;
	}
	
	public void start() {
		mediaPlayer.start();
	}

	public void seekTo(long pos) {
		mediaPlayer.seekTo((int) pos);
	}

	public void pause() {
		mediaPlayer.pause();
	}

	public boolean isPlaying() {
		return mediaPlayer.isPlaying();
	}

	public long getDuration() {
		return mediaPlayer.getDuration();
	}

	public long getCurrentPosition() {
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
