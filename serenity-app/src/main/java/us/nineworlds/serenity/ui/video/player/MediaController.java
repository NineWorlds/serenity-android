/*
 * Copyright (C) 2011 VOV IO (http://vov.io/)
 * 
 * Based on the code from the Android Open Player
 * http://code.google.com/p/android-oplayer/
 */

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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import us.nineworlds.serenity.R;
import us.nineworlds.serenity.SerenityApplication;
import us.nineworlds.serenity.core.model.VideoContentInfo;
import us.nineworlds.serenity.core.util.TimeUtil;
import us.nineworlds.serenity.ui.util.ImageInfographicUtils;
import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

/**
 * Based on the Android Open Source MediaController but drastically refactored
 * and cleaned up.
 * 
 * DAC - 2013-03-28 - Cleaned up the code and removed the handling of the key
 * events. Also made it so that the mediacontroller popup windows is focusable
 * otherwise no keyevents were being fired.
 * 
 * DAC - 2014-05-20 - Fix issue on Android 4.3 or higher were mediacontroller
 * window position was not being calculated correctly.
 * 
 */
public class MediaController extends FrameLayout {

	private static final int FADE_OUT = 1;
	private static final int SHOW_PROGRESS = 2;
	private static final int DEFAULT_TIME_OUT = 3000;

	private AudioManager audioManager;
	private View anchorView;
	private int animationStyle;
	private Context context;
	private boolean draggingSeeker;
	private long playbackDuration;
	private MediaPlayerControl mediaPlayerControl;
	private TextView endTimeTextView, currentTimeTextView;

	private boolean fromLayoutXML = false;

	private final Handler showfadeMessgeHandler = new ShowMessageHandler();
	private OnHiddenListener onHideListener;
	private boolean instantSeeking = true;

	private ImageButton pauseImageButton;

	private SeekBar progressSeekBar;

	private View rootView;

	private final OnSeekBarChangeListener mSeekListener = new SeekOnSeekBarChangeListener();
	private boolean mediaControllerShowing;
	private OnShownListener onShownListener;

	private PopupWindow mediaControllerHUD;
	private MediaControllerDataObject mediaMetaData;

	@Deprecated
	public MediaController(Context context, AttributeSet attrs) {
		super(context, attrs);
		rootView = this;
		fromLayoutXML = true;
		initController(context);
	}

	public MediaController(MediaControllerDataObject mediaMetaData) {
		super(mediaMetaData.getContext());
		this.mediaMetaData = mediaMetaData;
		if (!fromLayoutXML && initController(mediaMetaData.getContext())) {
			initFloatingWindow();
		}
	}

	protected void createDurationViews(View v) {
		endTimeTextView = (TextView) v
				.findViewById(R.id.mediacontroller_time_total);
		currentTimeTextView = (TextView) v
				.findViewById(R.id.mediacontroller_time_current);
	}

	protected void createNextVideoButton(View v) {
		TextView txtNextVideo = (TextView) v
				.findViewById(R.id.mediacontroller_next_video);
		if (txtNextVideo == null) {
			return;
		}

		if (SerenityApplication.getVideoPlaybackQueue().isEmpty()) {
			txtNextVideo.setVisibility(View.GONE);
			return;
		}

		VideoContentInfo nextVideo = SerenityApplication
				.getVideoPlaybackQueue().peek();
		txtNextVideo.setText(getResources().getString(
				R.string.mediacontroller_on_deck)
				+ nextVideo.getTitle());
		txtNextVideo.setVisibility(View.VISIBLE);
	}

	protected void createPauseButton(View v) {
		pauseImageButton = (ImageButton) v
				.findViewById(R.id.mediacontroller_play_pause);
		if (pauseImageButton != null) {
			pauseImageButton.requestFocus();
			pauseImageButton.setOnClickListener(new PauseOnClickListener(this));
		}
	}

	protected void createPoster(View v) {
		ImageView posterView = (ImageView) v
				.findViewById(R.id.mediacontroller_poster_art);
		posterView.setScaleType(ImageView.ScaleType.FIT_CENTER);

		if (mediaMetaData.getPosterURL() != null) {
			SerenityApplication.displayImageRoundedCorners(
					mediaMetaData.getPosterURL(), posterView);
		}
	}

	protected void createProgressBar(View v) {
		progressSeekBar = (SeekBar) v
				.findViewById(R.id.mediacontroller_seekbar);
		if (progressSeekBar != null) {
			if (progressSeekBar instanceof SeekBar) {
				SeekBar seeker = progressSeekBar;
				seeker.setOnSeekBarChangeListener(mSeekListener);
				seeker.setThumbOffset(1);
			}
			progressSeekBar.setMax(1000);
		}
	}

	protected void createSkipBackwardsButton(View v) {
		ImageButton skipBackwardButton = (ImageButton) v
				.findViewById(R.id.osd_rewind_control);
		if (skipBackwardButton != null) {
			skipBackwardButton
					.setOnClickListener(new SkipBackwardOnClickListener(this));
		}
	}

	protected void createSkipForwardButton(View v) {
		ImageButton skipForwardButton = (ImageButton) v
				.findViewById(R.id.osd_ff_control);
		if (skipForwardButton != null) {
			skipForwardButton
					.setOnClickListener(new SkipForwardOnClickListener(this));
		}
	}

	private void disableUnsupportedButtons() {
		try {
			if (pauseImageButton != null && !mediaPlayerControl.canPause()) {
				pauseImageButton.setEnabled(false);
			}
		} catch (IncompatibleClassChangeError ex) {
		}
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		int keyCode = event.getKeyCode();
		Activity c = (Activity) getContext();
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			c.onKeyDown(keyCode, event);
			return true;
		}
		return c.dispatchKeyEvent(event);
	}

	public MediaPlayerControl getMediaPlayerControl() {
		return mediaPlayerControl;
	}

	public void hide() {
		if (anchorView == null) {
			return;
		}

		if (!mediaControllerShowing) {
			return;
		}

		try {
			showfadeMessgeHandler.removeMessages(SHOW_PROGRESS);
			if (fromLayoutXML) {
				setVisibility(View.GONE);
			} else {
				mediaControllerHUD.dismiss();
			}
			anchorView.setSystemUiVisibility(View.STATUS_BAR_HIDDEN);
		} catch (IllegalArgumentException ex) {
			Log.d("SerentityMediaController",
					"MediaController already removed", ex);
		}
		mediaControllerShowing = false;
		if (onHideListener != null) {
			onHideListener.onHidden();
		}
	}

	private boolean initController(Context context) {
		this.context = context;
		audioManager = (AudioManager) context
				.getSystemService(Context.AUDIO_SERVICE);
		return true;
	}

	protected void initControllerView(View v) {
		createPauseButton(v);
		createSkipBackwardsButton(v);
		createSkipForwardButton(v);
		createProgressBar(v);
		createDurationViews(v);
		createPoster(v);

		initTitle(v);
		initSummary(v);
		initVideoMetaData(v);

		createNextVideoButton(v);

	}

	private void initFloatingWindow() {
		mediaControllerHUD = new PopupWindow(context);
		mediaControllerHUD.setFocusable(true);
		mediaControllerHUD.setBackgroundDrawable(null);
		mediaControllerHUD.setOutsideTouchable(true);
		mediaControllerHUD.update();
		animationStyle = R.style.PopupAnimation;
		setFocusable(true);
		setFocusableInTouchMode(true);
		requestFocus();
	}

	protected void initSummary(View v) {
		TextView summaryView = (TextView) v
				.findViewById(R.id.mediacontroller_summary);
		summaryView.setText(mediaMetaData.getSummary());
	}

	protected void initTitle(View v) {
		TextView textTitle = (TextView) v
				.findViewById(R.id.mediacontroller_title);
		textTitle.setText(mediaMetaData.getTitle());
	}

	/**
	 * @param v
	 */
	protected void initVideoMetaData(View v) {
		LinearLayout infoGraphic = (LinearLayout) v
				.findViewById(R.id.mediacontroller_infographic_layout);
		ImageInfographicUtils iiu = new ImageInfographicUtils(75, 70);
		if (mediaMetaData.getResolution() != null) {
			ImageView rv = iiu.createVideoResolutionImage(
					mediaMetaData.getResolution(), v.getContext());
			if (rv != null) {
				infoGraphic.addView(rv);
			}
		}

		if (mediaMetaData.getVideoFormat() != null) {
			ImageView vr = iiu.createVideoCodec(mediaMetaData.getVideoFormat(),
					v.getContext());
			if (vr != null) {
				infoGraphic.addView(vr);
			}
		}

		if (mediaMetaData.getAudioFormat() != null) {
			ImageView ar = iiu.createAudioCodecImage(
					mediaMetaData.getAudioFormat(), v.getContext());
			if (ar != null) {
				infoGraphic.addView(ar);
			}
		}

		if (mediaMetaData.getAudioChannels() != null) {
			ImageView ar = iiu.createAudioChannlesImage(
					mediaMetaData.getAudioChannels(), v.getContext());
			if (ar != null) {
				infoGraphic.addView(ar);
			}
		}
	}

	public boolean isShowing() {
		return mediaControllerShowing;
	}

	/**
	 * Create the view that holds the widgets that control playback. Derived
	 * classes can override this to create their own.
	 * 
	 * @return The controller view.
	 */
	protected View makeControllerView() {
		if (SerenityApplication.isRunningOnOUYA()) {
			return View.inflate(context,
					R.layout.serenity_media_controller_ouya, this);
		}

		return View.inflate(context, R.layout.serenity_media_controller, this);
	}

	@Override
	public void onFinishInflate() {
		if (rootView != null) {
			initControllerView(rootView);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		Activity c = (Activity) getContext();
		return c.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		show(DEFAULT_TIME_OUT);
		return true;
	}

	@Override
	public boolean onTrackballEvent(MotionEvent ev) {
		show(DEFAULT_TIME_OUT);
		return false;
	}

	/**
	 * 
	 */
	protected void positionPopupWindow() {
		int[] location = new int[2];

		anchorView.getLocationOnScreen(location);

		rootView.measure(MeasureSpec.makeMeasureSpec(anchorView.getWidth(),
				MeasureSpec.AT_MOST), MeasureSpec.makeMeasureSpec(
				anchorView.getHeight(), MeasureSpec.AT_MOST));

		int x = location[0] / 2;
		int y = location[1] + anchorView.getHeight()
				- rootView.getMeasuredHeight();
		mediaControllerHUD.showAtLocation(anchorView, Gravity.NO_GRAVITY, x, y);

		mediaControllerHUD.setAnimationStyle(animationStyle);
	}

	/**
	 * Set the view that acts as the anchor for the control view. This can for
	 * example be a VideoView, or your Activity's main view.
	 * 
	 * @param view
	 *            The view to which to anchor the controller when it is visible.
	 */
	public void setAnchorView(View view) {
		anchorView = view;
		if (!fromLayoutXML) {
			removeAllViews();
			rootView = makeControllerView();
			mediaControllerHUD.setContentView(rootView);
			mediaControllerHUD.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
			mediaControllerHUD
					.setHeight(android.view.ViewGroup.LayoutParams.MATCH_PARENT);

		}
		initControllerView(rootView);
	}

	/**
	 * <p>
	 * Change the animation style resource for this controller.
	 * </p>
	 * 
	 * <p>
	 * If the controller is showing, calling this method will take effect only
	 * the next time the controller is shown.
	 * </p>
	 * 
	 * @param animationStyle
	 *            animation style to use when the controller appears and
	 *            disappears. Set to -1 for the default animation, 0 for no
	 *            animation, or a resource identifier for an explicit animation.
	 * 
	 */
	public void setAnimationStyle(int animationStyle) {
		this.animationStyle = animationStyle;
	}

	@Override
	public void setEnabled(boolean enabled) {
		if (pauseImageButton != null) {
			pauseImageButton.setEnabled(enabled);
		}
		if (progressSeekBar != null) {
			progressSeekBar.setEnabled(enabled);
		}
		disableUnsupportedButtons();
		super.setEnabled(enabled);
	}

	/**
	 * Control the action when the seekbar dragged by user
	 * 
	 * @param seekWhenDragging
	 *            True the media will seek periodically
	 */
	public void setInstantSeeking(boolean seekWhenDragging) {
		instantSeeking = seekWhenDragging;
	}

	public void setMediaPlayer(MediaPlayerControl player) {
		mediaPlayerControl = player;
	}

	/**
	 * @param mPlayer
	 *            the mPlayer to set
	 */
	public void setMediaPlayerControl(MediaPlayerControl mPlayer) {
		this.mediaPlayerControl = mPlayer;
	}

	public void setOnHiddenListener(OnHiddenListener l) {
		onHideListener = l;
	}

	public void setOnShownListener(OnShownListener l) {
		onShownListener = l;
	}

	private long setProgress() {
		if (mediaPlayerControl == null || draggingSeeker) {
			return 0;
		}

		long position = 0;

		try {
			position = mediaPlayerControl.getCurrentPosition();
			long duration = mediaPlayerControl.getDuration();
			if (progressSeekBar != null) {
				if (duration > 0) {
					long pos = 1000L * position / duration;
					progressSeekBar.setProgress((int) pos);
				}
				int percent = mediaPlayerControl.getBufferPercentage();
				progressSeekBar.setSecondaryProgress(percent * 10);
			}

			playbackDuration = duration;

			if (endTimeTextView != null) {
				endTimeTextView.setText(TimeUtil
						.formatDuration(playbackDuration));
			}

			if (currentTimeTextView != null) {
				currentTimeTextView.setText(TimeUtil.formatDuration(position));
			}
		} catch (IllegalStateException ex) {
			Log.i(getClass().getName(),
					"Player has been either released or in an error state.");
		}

		return position;
	}

	public void show() {
		show(DEFAULT_TIME_OUT);
	}

	/**
	 * Show the controller on screen. It will go away automatically after
	 * 'timeout' milliseconds of inactivity.
	 * 
	 * @param timeout
	 *            The timeout in milliseconds. Use 0 to show the controller
	 *            until hide() is called.
	 */
	public void show(int timeout) {
		if (!mediaControllerShowing && anchorView != null
				&& anchorView.getWindowToken() != null) {
			if (pauseImageButton != null) {
				pauseImageButton.requestFocus();
			}
			disableUnsupportedButtons();

			if (fromLayoutXML) {
				setVisibility(View.VISIBLE);
			} else {
				positionPopupWindow();
			}
			anchorView.setSystemUiVisibility(View.STATUS_BAR_VISIBLE);
			mediaControllerShowing = true;
			if (onShownListener != null) {
				onShownListener.onShown();
			}
		}
		showfadeMessgeHandler.sendEmptyMessage(SHOW_PROGRESS);

		if (timeout != 0) {
			showfadeMessgeHandler.removeMessages(FADE_OUT);
			showfadeMessgeHandler.sendMessageDelayed(
					showfadeMessgeHandler.obtainMessage(FADE_OUT), timeout);
		}
	}

	public interface OnHiddenListener {
		public void onHidden();
	}

	public interface OnShownListener {
		public void onShown();
	}

	protected class SeekOnSeekBarChangeListener implements
			OnSeekBarChangeListener {
		@Override
		public void onProgressChanged(SeekBar bar, int progress,
				boolean fromuser) {
			try {
				if (!fromuser) {
					return;
				}

				long newposition = (playbackDuration * progress) / 1000;
				String time = new SimpleDateFormat("HH:mm:ss",
						Locale.getDefault()).format(new Date(newposition));
				if (instantSeeking) {
					mediaPlayerControl.seekTo(newposition);
				}

				if (currentTimeTextView != null) {
					currentTimeTextView.setText(time);
				}
			} catch (IllegalStateException e) {
				Log.d(getClass().getName(),
						"Seeking failed due to media player in an illegalstate.",
						e);
			}
		}

		@Override
		public void onStartTrackingTouch(SeekBar bar) {
			draggingSeeker = true;
			show(3600000);
			showfadeMessgeHandler.removeMessages(SHOW_PROGRESS);
			if (instantSeeking) {
				audioManager.setStreamMute(AudioManager.STREAM_MUSIC, true);
			}
		}

		@Override
		public void onStopTrackingTouch(SeekBar bar) {
			try {
				if (!instantSeeking) {
					mediaPlayerControl.seekTo((playbackDuration * bar
							.getProgress()) / 1000);
				}
				show(DEFAULT_TIME_OUT);
				showfadeMessgeHandler.removeMessages(SHOW_PROGRESS);
				audioManager.setStreamMute(AudioManager.STREAM_MUSIC, false);
				draggingSeeker = false;
				showfadeMessgeHandler.sendEmptyMessageDelayed(SHOW_PROGRESS,
						1000);
			} catch (IllegalStateException e) {
				Log.d(getClass().getName(),
						"Seeking failed due to media player in an illegalstate.",
						e);
			}
		}
	}

	protected class ShowMessageHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			long pos;
			switch (msg.what) {
			case FADE_OUT:
				hide();
				break;
			case SHOW_PROGRESS:
				if (!draggingSeeker && mediaControllerShowing) {
					pos = setProgress();
					msg = obtainMessage(SHOW_PROGRESS);
					sendMessageDelayed(msg, 1000 - (pos % 1000));
				}
				break;
			}
		}
	}

}