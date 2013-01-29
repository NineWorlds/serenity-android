/**
 * The MIT License (MIT)
 * Copyright (c) 2012 David Carver
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

package com.github.kingargyle.plexappclient.ui.video.player;

import com.github.kingargyle.plexappclient.R;
import com.github.kingargyle.plexappclient.ui.video.player.MediaController.MediaPlayerControl;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewDebug.HierarchyTraceType;
import android.widget.Button;

/**
 * @author dcarver
 *
 */
public class SerenitySurfaceViewVideoActivity extends Activity implements SurfaceHolder.Callback {
	
	static final String TAG = "SerenitySurfaceViewVideoActivity";
	private MediaPlayer mediaPlayer;
	private String videoURL;
	private SurfaceView surfaceView;
	private MediaController mediaController;	
	

	/* (non-Javadoc)
	 * @see android.view.SurfaceHolder.Callback#surfaceChanged(android.view.SurfaceHolder, int, int, int)
	 */
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		if (mediaController != null) {
			if (mediaController.isShowing()) {
				mediaController.hide();
			}
			mediaController.show();
		}
		
	}

	/* (non-Javadoc)
	 * @see android.view.SurfaceHolder.Callback#surfaceCreated(android.view.SurfaceHolder)
	 */
	public void surfaceCreated(SurfaceHolder holder) {
		try {
			mediaPlayer.setDisplay(holder);
			mediaPlayer.setDataSource(videoURL);
			mediaPlayer.prepare();
			
			int width = mediaPlayer.getVideoWidth();
			int height = mediaPlayer.getVideoHeight();
			
			int swidth = getWindowManager().getDefaultDisplay().getWidth();
			
			android.view.ViewGroup.LayoutParams lp = surfaceView.getLayoutParams();
			
			lp.width = swidth;
			
			lp.height = (int) (((float)height / (float) width ) * (float) swidth );
			
			surfaceView.setLayoutParams(lp);
			
			mediaController.setEnabled(true);
			
			mediaPlayer.start();
			if (mediaPlayer.isPlaying()) {
				mediaController.show();
			}

			
		} catch (Exception ex) {
			Log.e(TAG, "Video Playback Error. ", ex);
		}
		
	}

	/* (non-Javadoc)
	 * @see android.view.SurfaceHolder.Callback#surfaceDestroyed(android.view.SurfaceHolder)
	 */
	public void surfaceDestroyed(SurfaceHolder holder) {
		mediaPlayer.release();
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.video_playback);
		
		videoURL = getIntent().getExtras().getString("videoUrl");
		
		
		mediaPlayer = new MediaPlayer();
		surfaceView =  (SurfaceView) findViewById(R.id.surfaceView);
		surfaceView.setKeepScreenOn(true);
		
		SurfaceHolder holder = surfaceView.getHolder();
		holder.addCallback(this);
		holder.setSizeFromLayout();
		
		mediaController = new MediaController(this);
		mediaController.setAnchorView(surfaceView);
		mediaController.setMediaPlayer(new MediaPlayerControl() {
			
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
		});
		
		//mediaPlayer.start();
	}
}
