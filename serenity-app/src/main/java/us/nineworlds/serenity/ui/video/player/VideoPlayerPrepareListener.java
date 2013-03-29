/**
 * The MIT License (MIT)
 * 
 * Copyright (c) 2012-2013 David Carver
 * 
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS
 * OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
 * OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF
 * OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package us.nineworlds.serenity.ui.video.player;

import us.nineworlds.serenity.core.services.WatchedVideoAsyncTask;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.SurfaceView;
import android.widget.RelativeLayout;

/**
 * A prepare listener that handles how a video should start playing.
 * 
 * It checks to see if the video has been previously viewed and if so will
 * present a dialog to allow resuming of a video from where it was
 * previously last viewed. Otherwise it will start play back of the video.
 * It also launches the Watched status update task and the progress update
 * handler.
 * 
 * @author dcarver
 * 
 */
public class VideoPlayerPrepareListener implements OnPreparedListener {

	private Context context;
	private SurfaceView surfaceView;
	private MediaController mediaController;
	private int resumeOffset;
	private MediaPlayer mediaPlayer;
	private String videoId;
	private String plexAspectRatio;
	private Handler progressReportingHandler;
	private Runnable progressRunnable;

	public VideoPlayerPrepareListener(Context c, MediaPlayer mp, MediaController con, SurfaceView v, int resumeOffset, String videoId, String aspectRatio, Handler progress, Runnable progresrun) {
		context = c;
		mediaController = con;
		surfaceView = v;
		mediaPlayer = mp;
		progressReportingHandler = progress;
		progressRunnable = progresrun;
		this.videoId = videoId; 
		plexAspectRatio = aspectRatio;
		
		this.resumeOffset = resumeOffset;
	}

	public void onPrepared(MediaPlayer mp) {
		android.view.ViewGroup.LayoutParams lp = setupAspectRatio(surfaceView, plexAspectRatio);
		surfaceView.setLayoutParams(lp);
		mediaController.setEnabled(true);

		if (resumeOffset > 0) {
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					context, android.R.style.Theme_Holo_Dialog);

			alertDialogBuilder.setTitle("Resume Video");
			alertDialogBuilder
					.setMessage("Resume the video or start from beginning?")
					.setCancelable(false)
					.setPositiveButton("Resume",
							new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog,
										int which) {
									if (!mediaPlayer.isPlaying()) {
										mediaPlayer.start();
									}
									mediaPlayer.seekTo(resumeOffset);
									setMetaData();
								}
							})
					.setNegativeButton("Restart",
							new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog,
										int which) {
									mediaPlayer.start();
									setMetaData();
								}
							});

			alertDialogBuilder.create();
			alertDialogBuilder.show();
			return;
		} else {
			mediaPlayer.start();
			setMetaData();
		}
	}
	
	/**
	 * 
	 */
	protected void setMetaData() {
		new WatchedVideoAsyncTask().execute(videoId);
		mediaController.show(SerenitySurfaceViewVideoActivity.CONTROLLER_DELAY);
		if (progressReportingHandler != null) {
			progressReportingHandler.postDelayed(progressRunnable, 5000);
		}
	}
	
	/**
	 * Setup the aspect ratio for the SurfaceView
	 * 
	 * @return
	 */
	protected android.view.ViewGroup.LayoutParams setupAspectRatio(SurfaceView surfaceView, String plexAspectRatio) {
		RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) surfaceView
				.getLayoutParams();

		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		boolean preferPlexAspectRatio = prefs.getBoolean("plex_aspect_ratio",
				false);

		int surfaceViewHeight = surfaceView.getHeight();
		int surfaceViewWidth = surfaceView.getWidth();

		float videoWidth = mediaPlayer.getVideoWidth();
		float videoHeight = mediaPlayer.getVideoHeight();

		float ratioWidth = surfaceViewWidth / videoWidth;
		float ratioHeight = surfaceViewHeight / videoHeight;
		float aspectRatio = videoWidth / videoHeight;

		if (videoHeight == 480 && videoWidth == 720) {
			aspectRatio = (float) 1.78;
		}

		if (preferPlexAspectRatio && plexAspectRatio != null) {
			aspectRatio = Float.parseFloat(plexAspectRatio);
		}

		if (ratioWidth > ratioHeight) {
			lp.width = (int) (surfaceViewHeight * aspectRatio);
			lp.height = surfaceViewHeight;
		} else {
			lp.width = surfaceViewWidth;
			lp.height = (int) (surfaceViewWidth / aspectRatio);
		}

		if (lp.width > surfaceViewWidth) {
			lp.width = surfaceViewWidth;
		}

		if (lp.height > surfaceViewHeight) {
			lp.height = surfaceViewHeight;
		}

		return lp;
	}
	
	
}