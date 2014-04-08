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
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.media.AudioManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
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

import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

/**
 * A view containing controls for a MediaPlayer. Typically contains the buttons
 * like "Play/Pause" and a progress slider. It takes care of synchronizing the
 * controls with the state of the MediaPlayer.
 * <p>
 * The way to use this class is to a) instantiate it programatically or b)
 * create it in your xml layout.
 * 
 * a) The MediaController will create a default set of controls and put them in
 * a window floating above your application. Specifically, the controls will
 * float above the view specified with setAnchorView(). By default, the window
 * will disappear if left idle for three seconds and reappear when the user
 * touches the anchor view. To customize the MediaController's style, layout and
 * controls you should extend MediaController and override the {#link
 * {@link #makeControllerView()} method.
 * 
 * b) The MediaController is a FrameLayout, you can put it in your layout xml
 * and get it through {@link #findViewById(int)}.
 * 
 * NOTES: In each way, if you want customize the MediaController, the SeekBar's
 * id must be mediacontroller_progress, the Play/Pause's must be
 * mediacontroller_pause, current time's must be mediacontroller_time_current,
 * total time's must be mediacontroller_time_total, file name's must be
 * mediacontroller_file_name. And your resources must have a pause_button
 * drawable and a play_button drawable.
 * <p>
 * Functions like show() and hide() have no effect when MediaController is
 * created in an xml layout.
 * 
 * DAC - 2013-03-28 - Cleaned up the code and removed the handling of the key
 * events. Also made it so that the mediacontroller popup windows is focusable
 * otherwise no keyevents were being fired.
 * 
 */
public class MediaController extends FrameLayout {
	private MediaPlayerControl mPlayer;
	private Context mContext;
	private PopupWindow mWindow;
	private int mAnimStyle;
	private View mAnchor;
	private View mRoot;
	private SeekBar mProgress;
	private TextView mEndTime, mCurrentTime;
	private long mDuration;
	private boolean mShowing;
	private boolean mDragging;
	private boolean mInstantSeeking = true;
	private static final int sDefaultTimeout = 3000;
	private static final int FADE_OUT = 1;
	private static final int SHOW_PROGRESS = 2;
	private boolean mFromXml = false;
	private ImageButton mPauseButton;
	private ImageButton mSkipForwardButton;
	private ImageButton mSkipBackwardButton;
	private String summary;
	private String title;
	private String posterURL;
	private String resolution;
	private String videoFormat;
	private String audioFormat;
	private String audioChannels;
	private AudioManager mAM;

	private final ImageLoader imageLoader;

	public MediaController(Context context, AttributeSet attrs) {
		super(context, attrs);
		mRoot = this;
		mFromXml = true;
		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
				.cacheInMemory().cacheOnDisc()
				.bitmapConfig(Bitmap.Config.RGB_565)
				.showImageOnFail(R.drawable.default_error)
				.showStubImage(R.drawable.default_video_cover).build();

		ImageLoaderConfiguration imageLoaderconfig = new ImageLoaderConfiguration.Builder(
				context).memoryCacheExtraOptions(1280, 720)
				.taskExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
				.taskExecutorForCachedImages(AsyncTask.THREAD_POOL_EXECUTOR)
				.threadPoolSize(5)
				.tasksProcessingOrder(QueueProcessingType.FIFO)
				.denyCacheImageMultipleSizesInMemory()
				.defaultDisplayImageOptions(defaultOptions)
				.memoryCache(new WeakMemoryCache()).build();

		imageLoader = ImageLoader.getInstance();
		imageLoader.init(imageLoaderconfig);
		initController(context);
	}

	public MediaController(Context context, String summary, String title,
			String posterURL, String resolution, String videoFormat,
			String audioFormat, String audioChannels, String mediaTagId) {
		super(context);
		this.summary = summary;
		this.title = title;
		this.posterURL = posterURL;
		this.resolution = resolution;
		this.audioChannels = audioChannels;
		this.videoFormat = videoFormat;
		this.audioFormat = audioFormat;
		imageLoader = SerenityApplication.getImageLoader();

		if (!mFromXml && initController(context)) {
			initFloatingWindow();
		}
	}

	private boolean initController(Context context) {
		mContext = context;
		mAM = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
		return true;
	}

	@Override
	public void onFinishInflate() {
		if (mRoot != null) {
			initControllerView(mRoot);
		}
	}

	private void initFloatingWindow() {
		mWindow = new PopupWindow(mContext);
		mWindow.setFocusable(true);
		mWindow.setBackgroundDrawable(null);
		mWindow.setOutsideTouchable(true);
		mWindow.update();
		mAnimStyle = android.R.style.Animation;
		setFocusable(true);
		setFocusableInTouchMode(true);
		requestFocus();
	}

	/**
	 * Set the view that acts as the anchor for the control view. This can for
	 * example be a VideoView, or your Activity's main view.
	 * 
	 * @param view
	 *            The view to which to anchor the controller when it is visible.
	 */
	public void setAnchorView(View view) {
		mAnchor = view;
		if (!mFromXml) {
			removeAllViews();
			mRoot = makeControllerView();
			mWindow.setContentView(mRoot);
			mWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
			mWindow.setHeight(android.view.ViewGroup.LayoutParams.MATCH_PARENT);

		}
		initControllerView(mRoot);
	}

	/**
	 * Create the view that holds the widgets that control playback. Derived
	 * classes can override this to create their own.
	 * 
	 * @return The controller view.
	 */
	protected View makeControllerView() {
		if (SerenityApplication.isRunningOnOUYA()) {
			return ((LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
					.inflate(R.layout.serenity_media_controller_ouya, this);
		}

		return ((LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
				R.layout.serenity_media_controller, this);
	}

	private void initControllerView(View v) {
		mPauseButton = (ImageButton) v
				.findViewById(R.id.mediacontroller_play_pause);
		if (mPauseButton != null) {
			mPauseButton.requestFocus();
			mPauseButton.setOnClickListener(mPauseListener);
		}

		mSkipBackwardButton = (ImageButton) v
				.findViewById(R.id.osd_rewind_control);
		if (mSkipBackwardButton != null) {
			mSkipBackwardButton.setOnClickListener(mSkipBackwardListener);
		}

		mSkipForwardButton = (ImageButton) v.findViewById(R.id.osd_ff_control);
		if (mSkipForwardButton != null) {
			mSkipForwardButton.setOnClickListener(mSkipForwardListener);
		}

		mProgress = (SeekBar) v.findViewById(R.id.mediacontroller_seekbar);
		if (mProgress != null) {
			if (mProgress instanceof SeekBar) {
				SeekBar seeker = mProgress;
				seeker.setOnSeekBarChangeListener(mSeekListener);
				seeker.setThumbOffset(1);
			}
			mProgress.setMax(1000);
		}

		mEndTime = (TextView) v.findViewById(R.id.mediacontroller_time_total);
		mCurrentTime = (TextView) v
				.findViewById(R.id.mediacontroller_time_current);

		TextView textTitle = (TextView) v
				.findViewById(R.id.mediacontroller_title);
		textTitle.setText(title);

		TextView summaryView = (TextView) v
				.findViewById(R.id.mediacontroller_summary);
		summaryView.setText(summary);

		LinearLayout infoGraphic = (LinearLayout) v
				.findViewById(R.id.mediacontroller_infographic_layout);
		ImageInfographicUtils iiu = new ImageInfographicUtils(75, 70);
		if (resolution != null) {
			ImageView rv = iiu.createVideoResolutionImage(resolution,
					v.getContext());
			if (rv != null) {
				infoGraphic.addView(rv);
			}
		}

		if (videoFormat != null) {
			ImageView vr = iiu.createVideoCodec(videoFormat, v.getContext());
			if (vr != null) {
				infoGraphic.addView(vr);
			}
		}

		if (audioFormat != null) {
			ImageView ar = iiu.createAudioCodecImage(audioFormat,
					v.getContext());
			if (ar != null) {
				infoGraphic.addView(ar);
			}
		}

		if (audioChannels != null) {
			ImageView ar = iiu.createAudioChannlesImage(audioChannels,
					v.getContext());
			if (ar != null) {
				infoGraphic.addView(ar);
			}
		}

		ImageView posterView = (ImageView) v
				.findViewById(R.id.mediacontroller_poster_art);
		posterView.setScaleType(ImageView.ScaleType.FIT_CENTER);

		if (posterURL != null) {
			SerenityApplication.displayImage(posterURL, posterView);
		}

		TextView txtNextVideo = (TextView) v
				.findViewById(R.id.mediacontroller_next_video);
		if (txtNextVideo != null) {
			if (!SerenityApplication.getVideoPlaybackQueue().isEmpty()) {
				VideoContentInfo nextVideo = SerenityApplication
						.getVideoPlaybackQueue().peek();
				txtNextVideo.setText("On Deck: " + nextVideo.getTitle());
				txtNextVideo.setVisibility(View.VISIBLE);
			} else {
				txtNextVideo.setVisibility(View.GONE);
			}
		}

	}

	public void setMediaPlayer(MediaPlayerControl player) {
		mPlayer = player;
	}

	/**
	 * Control the action when the seekbar dragged by user
	 * 
	 * @param seekWhenDragging
	 *            True the media will seek periodically
	 */
	public void setInstantSeeking(boolean seekWhenDragging) {
		mInstantSeeking = seekWhenDragging;
	}

	public void show() {
		show(sDefaultTimeout);
	}

	/**
	 * Set the content of the file_name TextView
	 * 
	 * @param name
	 */
	public void setFileName(String name) {
	}

	/**
	 * Set the View to hold some information when interact with the
	 * MediaController
	 * 
	 * @param v
	 */
	public void setInfoView(TextView v) {
		// mInfoView = v;
	}

	private void disableUnsupportedButtons() {
		try {
			if (mPauseButton != null && !mPlayer.canPause()) {
				mPauseButton.setEnabled(false);
			}
		} catch (IncompatibleClassChangeError ex) {
		}
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
		mAnimStyle = animationStyle;
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
		if (!mShowing && mAnchor != null && mAnchor.getWindowToken() != null) {
			if (mPauseButton != null) {
				mPauseButton.requestFocus();
			}
			disableUnsupportedButtons();

			if (mFromXml) {
				setVisibility(View.VISIBLE);
			} else {
				int[] location = new int[2];

				mAnchor.getLocationOnScreen(location);
				Rect anchorRect = new Rect(location[0], location[1],
						location[0] + mAnchor.getWidth(), location[1]
								+ mAnchor.getHeight());

				mWindow.setAnimationStyle(mAnimStyle);
				mWindow.showAtLocation(mAnchor, Gravity.NO_GRAVITY,
						anchorRect.left, anchorRect.bottom);
			}
			mAnchor.setSystemUiVisibility(View.STATUS_BAR_VISIBLE);
			mShowing = true;
			if (mShownListener != null) {
				mShownListener.onShown();
			}
		}
		mHandler.sendEmptyMessage(SHOW_PROGRESS);

		if (timeout != 0) {
			mHandler.removeMessages(FADE_OUT);
			mHandler.sendMessageDelayed(mHandler.obtainMessage(FADE_OUT),
					timeout);
		}
	}

	public boolean isShowing() {
		return mShowing;
	}

	public void hide() {
		if (mAnchor == null) {
			return;
		}

		if (mShowing) {
			try {
				mHandler.removeMessages(SHOW_PROGRESS);
				if (mFromXml) {
					setVisibility(View.GONE);
				} else {
					mWindow.dismiss();
				}
				mAnchor.setSystemUiVisibility(View.STATUS_BAR_HIDDEN);
			} catch (IllegalArgumentException ex) {
				Log.d("SerentityMediaController",
						"MediaController already removed", ex);
			}
			mShowing = false;
			if (mHiddenListener != null) {
				mHiddenListener.onHidden();
			}
		}
	}

	public interface OnShownListener {
		public void onShown();
	}

	private OnShownListener mShownListener;

	public void setOnShownListener(OnShownListener l) {
		mShownListener = l;
	}

	public interface OnHiddenListener {
		public void onHidden();
	}

	private OnHiddenListener mHiddenListener;

	public void setOnHiddenListener(OnHiddenListener l) {
		mHiddenListener = l;
	}

	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			long pos;
			switch (msg.what) {
			case FADE_OUT:
				hide();
				break;
			case SHOW_PROGRESS:
				if (!mDragging && mShowing) {
					pos = setProgress();
					msg = obtainMessage(SHOW_PROGRESS);
					sendMessageDelayed(msg, 1000 - (pos % 1000));
				}
				break;
			}
		}
	};

	private long setProgress() {
		if (mPlayer == null || mDragging) {
			return 0;
		}

		long position = 0;

		try {
			position = mPlayer.getCurrentPosition();
			long duration = mPlayer.getDuration();
			if (mProgress != null) {
				if (duration > 0) {
					long pos = 1000L * position / duration;
					mProgress.setProgress((int) pos);
				}
				int percent = mPlayer.getBufferPercentage();
				mProgress.setSecondaryProgress(percent * 10);
			}

			mDuration = duration;

			if (mEndTime != null) {
				mEndTime.setText(TimeUtil.formatDuration(mDuration));
			}

			if (mCurrentTime != null) {
				mCurrentTime.setText(TimeUtil.formatDuration(position));
			}
		} catch (IllegalStateException ex) {
			Log.i(getClass().getName(),
					"Player has been either released or in an error state.");
		}

		return position;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		show(sDefaultTimeout);
		return true;
	}

	@Override
	public boolean onTrackballEvent(MotionEvent ev) {
		show(sDefaultTimeout);
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View#onKeyDown(int, android.view.KeyEvent)
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		Activity c = (Activity) getContext();
		return c.onKeyDown(keyCode, event);
	}

	private final View.OnClickListener mPauseListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			doPauseResume();
			show(sDefaultTimeout);
		}
	};

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

	public void doPauseResume() {
		if (mPlayer.isPlaying()) {
			mPlayer.pause();
		} else {
			mPlayer.start();
		}
	}

	private final OnSeekBarChangeListener mSeekListener = new OnSeekBarChangeListener() {
		@Override
		public void onStartTrackingTouch(SeekBar bar) {
			mDragging = true;
			show(3600000);
			mHandler.removeMessages(SHOW_PROGRESS);
			if (mInstantSeeking) {
				mAM.setStreamMute(AudioManager.STREAM_MUSIC, true);
			}
		}

		@Override
		public void onProgressChanged(SeekBar bar, int progress,
				boolean fromuser) {
			try {
				if (!fromuser) {
					return;
				}

				long newposition = (mDuration * progress) / 1000;
				String time = new SimpleDateFormat("HH:mm:ss",
						Locale.getDefault()).format(new Date(newposition));
				if (mInstantSeeking) {
					mPlayer.seekTo(newposition);
				}

				if (mCurrentTime != null) {
					mCurrentTime.setText(time);
				}
			} catch (IllegalStateException e) {
				Log.d(getClass().getName(),
						"Seeking failed due to media player in an illegalstate.",
						e);
			}
		}

		@Override
		public void onStopTrackingTouch(SeekBar bar) {
			try {
				if (!mInstantSeeking) {
					mPlayer.seekTo((mDuration * bar.getProgress()) / 1000);
				}
				show(sDefaultTimeout);
				mHandler.removeMessages(SHOW_PROGRESS);
				mAM.setStreamMute(AudioManager.STREAM_MUSIC, false);
				mDragging = false;
				mHandler.sendEmptyMessageDelayed(SHOW_PROGRESS, 1000);
			} catch (IllegalStateException e) {
				Log.d(getClass().getName(),
						"Seeking failed due to media player in an illegalstate.",
						e);
			}
		}
	};

	private final View.OnClickListener mSkipForwardListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			try {
				long skipOffset = 10000 + mPlayer.getCurrentPosition();
				long duration = mPlayer.getDuration();
				if (skipOffset > duration) {
					skipOffset = duration - 1;
				}
				mPlayer.seekTo(skipOffset);
				show();
			} catch (IllegalStateException e) {
				Log.d(getClass().getName(),
						"Seeking failed due to media player in an illegalstate.",
						e);
			}
		}
	};

	private final View.OnClickListener mSkipBackwardListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			try {
				long skipOffset = mPlayer.getCurrentPosition() - 10000;
				if (skipOffset < 0) {
					skipOffset = 1;
				}
				mPlayer.seekTo(skipOffset);
				show();
			} catch (IllegalStateException e) {
				Log.d(getClass().getName(),
						"Seeking failed due to media player in an illegalstate.",
						e);
			}
		}
	};

	@Override
	public void setEnabled(boolean enabled) {
		if (mPauseButton != null) {
			mPauseButton.setEnabled(enabled);
		}
		if (mProgress != null) {
			mProgress.setEnabled(enabled);
		}
		disableUnsupportedButtons();
		super.setEnabled(enabled);
	}

}