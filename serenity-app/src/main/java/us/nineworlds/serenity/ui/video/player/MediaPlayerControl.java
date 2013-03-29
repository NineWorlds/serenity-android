package us.nineworlds.serenity.ui.video.player;
public interface MediaPlayerControl {
	void start();

	void pause();

	long getDuration();

	long getCurrentPosition();

	void seekTo(long pos);

	boolean isPlaying();

	int getBufferPercentage();

	boolean canPause();

	boolean canSeekBackward();

	boolean canSeekForward();
}
