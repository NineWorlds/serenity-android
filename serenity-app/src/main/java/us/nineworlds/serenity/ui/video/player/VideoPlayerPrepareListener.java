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

import us.nineworlds.serenity.R;
import us.nineworlds.serenity.SerenityApplication;
import us.nineworlds.serenity.core.util.TimeUtil;
import android.app.AlertDialog;
import android.app.Dialog;
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
	private String plexAspectRatio;
    private final boolean autoResume;
    private Handler progressReportingHandler;
	private Runnable progressRunnable;
	private SharedPreferences preferences;

	public VideoPlayerPrepareListener(Context c, MediaPlayer mp, MediaController con, SurfaceView v, int resumeOffset, boolean autoResume, String aspectRatio, Handler progress, Runnable progresrun) {
		context = c;
		mediaController = con;
		surfaceView = v;
		mediaPlayer = mp;
        this.autoResume = autoResume;
        progressReportingHandler = progress;
		progressRunnable = progresrun;
		plexAspectRatio = aspectRatio;
		this.resumeOffset = resumeOffset;
	}

	@Override
	public void onPrepared(MediaPlayer mp) {
		preferences = PreferenceManager.getDefaultSharedPreferences(context);
		
		android.view.ViewGroup.LayoutParams lp = setupAspectRatio(surfaceView, plexAspectRatio);
		surfaceView.setLayoutParams(lp);
		mediaController.setEnabled(true);

		if (resumeOffset > 0 && SerenityApplication.getVideoPlaybackQueue().isEmpty()) {
            if (autoResume) {
                if (!mediaPlayer.isPlaying()) {
                    mediaPlayer.start();
                }
                mediaPlayer.seekTo(resumeOffset);
                setMetaData();
            } else {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        context, android.R.style.Theme_Holo_Dialog);

                alertDialogBuilder.setTitle(R.string.resume_video);
                alertDialogBuilder
                        .setMessage(context.getResources().getText(R.string.resume_the_video_from_) + TimeUtil.formatDuration(resumeOffset)  + context.getResources().getText(R.string._or_restart_))
                        .setCancelable(false)
                        .setPositiveButton(R.string.resume,
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                            int which) {
                                        if (!mediaPlayer.isPlaying()) {
                                            mediaPlayer.start();
                                        }
                                        mediaPlayer.seekTo(resumeOffset);
                                        setMetaData();
                                    }
                                })
                        .setNegativeButton(R.string.restart,
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                            int which) {
                                        mediaPlayer.start();
                                        setMetaData();
                                    }
                                });

                alertDialogBuilder.create();
                AlertDialog dialog = alertDialogBuilder.show();
                dialog.getButton(DialogInterface.BUTTON_POSITIVE).requestFocusFromTouch();
                return;
            }
		} else {
			mediaPlayer.start();			
			setMetaData();
		}
	}
	
	
	/**
	 * 
	 */
	protected void setMetaData() {
		boolean showOSD = preferences.getBoolean("internal_player_osd", true);
		if (showOSD) {
			mediaController.show(SerenitySurfaceViewVideoActivity.CONTROLLER_DELAY);
		}
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