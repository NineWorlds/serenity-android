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
	
	@Override
	public void start() {
		mediaPlayer.start();
	}

	@Override
	public void seekTo(long pos) {
		mediaPlayer.seekTo((int) pos);
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
	public long getDuration() {
		return mediaPlayer.getDuration();
	}

	@Override
	public long getCurrentPosition() {
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
