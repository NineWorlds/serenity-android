/*
 * Copyright (C) 2011 VOV IO (http://vov.io/)
 * 
 * Based on the code from the Android Open Player
 * http://code.google.com/p/android-oplayer/
 */

package com.github.kingargyle.plexappclient.ui.video.player;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.github.kingargyle.plexappclient.R;
import com.github.kingargyle.plexappclient.core.imageloader.OSDImageLoader;
import com.github.kingargyle.plexappclient.ui.util.ImageInfographicUtils;

import android.content.Context;
import android.graphics.Rect;
import android.media.AudioManager;
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
import android.widget.Gallery;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

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
 */
public class MediaController extends FrameLayout {
	/**
	 * 
	 */
	private static final int MILLISECONDS_PER_MINUTE = 60000;
	/**
	 * 
	 */
	private static final int MILLISECONDS_PER_HOUR = 3600000;
	private MediaPlayerControl mPlayer;
	private Context mContext;
	private PopupWindow mWindow;
	private int mAnimStyle;
	private View mAnchor;
	private View mRoot;
	private ProgressBar mProgress;
	private TextView mEndTime, mCurrentTime;
	private TextView mFileName;
	// private OutlineTextView mInfoView;
	private String mTitle;
	private long mDuration;
	private boolean mShowing;
	private boolean mDragging;
	private boolean mInstantSeeking = true;
	private static final int sDefaultTimeout = 3000;
	private static final int FADE_OUT = 1;
	private static final int SHOW_PROGRESS = 2;
	private boolean mFromXml = false;
	private ImageButton mPauseButton;
	private String summary;
	private String title;
	private String posterURL;
	private String resolution;
	private String videoFormat;
	private String audioFormat;
	private String audioChannels;

	private AudioManager mAM;
	
	// Sets up a Executor service for handling image loading
	private ExecutorService imageExecutorService;


	public MediaController(Context context, AttributeSet attrs) {
		super(context, attrs);
		mRoot = this;
		mFromXml = true;
		initController(context);
		imageExecutorService = Executors.newSingleThreadExecutor();
	}

	public MediaController(Context context, String summary, String title, String posterURL, String resolution, String videoFormat, String audioFormat, String audioChannels) {
		super(context);
		imageExecutorService = Executors.newSingleThreadExecutor();
		if (!mFromXml && initController(context)) {
			initFloatingWindow();
		}
		this.summary = summary;
		this.title = title;
		this.posterURL = posterURL;
		this.resolution = resolution;
		this.audioChannels = audioChannels;
		this.videoFormat = videoFormat;
		this.audioFormat = audioFormat;
	}

	private boolean initController(Context context) {
		mContext = context;
		mAM = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
		return true;
	}

	@Override
	public void onFinishInflate() {
		if (mRoot != null)
			initControllerView(mRoot);
	}

	private void initFloatingWindow() {
		mWindow = new PopupWindow(mContext);
		mWindow.setFocusable(false);
		mWindow.setBackgroundDrawable(null);
		mWindow.setOutsideTouchable(true);
		mAnimStyle = android.R.style.Animation;
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
			mWindow.setWidth(LayoutParams.FILL_PARENT);
			mWindow.setHeight(LayoutParams.WRAP_CONTENT);
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

		mProgress = (ProgressBar) v.findViewById(R.id.mediacontroller_seekbar);
		if (mProgress != null) {
			if (mProgress instanceof SeekBar) {
				SeekBar seeker = (SeekBar) mProgress;
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
		
		LinearLayout infoGraphic = (LinearLayout) v.findViewById(R.id.mediacontroller_infographic_layout);
		ImageInfographicUtils iiu = new ImageInfographicUtils(75, 70);
		if (resolution != null) {
			ImageView rv = iiu.createVideoResolutionImage(resolution, v.getContext());
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
			ImageView ar = iiu.createAudioCodecImage(audioFormat, v.getContext());
			if (ar != null) {
				infoGraphic.addView(ar);
			}
		}
		
		if (audioChannels != null) {
			ImageView ar = iiu.createAudioChannlesImage(audioChannels, v.getContext());
			if (ar != null) {
				infoGraphic.addView(ar);
			}
		}
		
		
		ImageView posterView = (ImageView) v.findViewById(R.id.mediacontroller_poster_art);
		posterView.setScaleType(ImageView.ScaleType.FIT_CENTER);
		imageExecutorService.execute(new OSDImageLoader(posterURL, posterView, R.drawable.default_video_cover));

		
	}

	public void setMediaPlayer(MediaPlayerControl player) {
		mPlayer = player;
		updatePausePlay();
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
		mTitle = name;
		if (mFileName != null)
			mFileName.setText(mTitle);
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
			if (mPauseButton != null && !mPlayer.canPause())
				mPauseButton.setEnabled(false);
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
			if (mPauseButton != null)
				mPauseButton.requestFocus();
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
			if (mShownListener != null)
				mShownListener.onShown();
		}
		updatePausePlay();
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
		if (mAnchor == null)
			return;

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
			if (mHiddenListener != null)
				mHiddenListener.onHidden();
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

	private Handler mHandler = new Handler() {
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
					updatePausePlay();
				}
				break;
			}
		}
	};

	private long setProgress() {
		if (mPlayer == null || mDragging)
			return 0;

		long position = mPlayer.getCurrentPosition();
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
			mEndTime.setText(formatDuration(mDuration));
		}

		if (mCurrentTime != null) {
			mCurrentTime.setText(formatDuration(position));
		}

		return position;
	}

	
	/**
	 * Return a formated duration in hh:mm:ss format.
	 * @param duration number of milliseconds that have passed.
	 * @return formatted string
	 */
	protected String formatDuration(long duration) {
		long tempdur = duration;
		long hours = TimeUnit.MILLISECONDS.toHours(duration);
		
        tempdur = tempdur - (hours * MILLISECONDS_PER_HOUR);
        
        long minutes = tempdur / MILLISECONDS_PER_MINUTE;
        tempdur = tempdur - (minutes * MILLISECONDS_PER_MINUTE);
        
        long seconds = tempdur / 1000;
        
		return String.format("%02d:%02d:%02d", hours, minutes, seconds);
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

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		int keyCode = event.getKeyCode();
		if (event.getRepeatCount() == 0
				&& (keyCode == KeyEvent.KEYCODE_HEADSETHOOK
						|| keyCode == KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE || keyCode == KeyEvent.KEYCODE_SPACE)) {
			doPauseResume();
			show(sDefaultTimeout);
			if (mPauseButton != null)
				mPauseButton.requestFocus();
			return true;
		} else if (keyCode == KeyEvent.KEYCODE_MEDIA_STOP) {
			if (mPlayer.isPlaying()) {
				mPlayer.pause();
				updatePausePlay();
			}
			return true;
		} else if (keyCode == KeyEvent.KEYCODE_BACK
				|| keyCode == KeyEvent.KEYCODE_MENU
				|| keyCode == KeyEvent.KEYCODE_INFO) {
			hide();
			return true;
		} else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
			View button = this.getFocusedChild();
			button.requestFocus(FOCUS_LEFT);
			return true;
		} else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
			View button = this.getFocusedChild();
			button.requestFocus(FOCUS_DOWN);
			return true;
		} else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
			View button = this.getFocusedChild();
			button.requestFocus(FOCUS_RIGHT);
			return true;
		} else if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
			View button = this.getFocusedChild();
			button.requestFocus(FOCUS_UP);
			return true;
		} else if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
			View button = this.getFocusedChild();
			button.performClick();
			return true;
		} else {
			show(sDefaultTimeout);
		}
		return super.dispatchKeyEvent(event);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View#onKeyDown(int, android.view.KeyEvent)
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (event.getRepeatCount() == 0
				&& (keyCode == KeyEvent.KEYCODE_HEADSETHOOK
						|| keyCode == KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE || keyCode == KeyEvent.KEYCODE_SPACE)) {
			doPauseResume();
			show(sDefaultTimeout);
			if (mPauseButton != null)
				mPauseButton.requestFocus();
			return true;
		} else if (keyCode == KeyEvent.KEYCODE_MEDIA_STOP) {
			if (mPlayer.isPlaying()) {
				mPlayer.pause();
				updatePausePlay();
			}
			return true;
		} else if (keyCode == KeyEvent.KEYCODE_BACK
				|| keyCode == KeyEvent.KEYCODE_MENU
				|| keyCode == KeyEvent.KEYCODE_INFO) {
			hide();
			return true;
		} else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
			View button = this.getFocusedChild();
			button.requestFocus(FOCUS_LEFT);
			return true;
		} else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
			View button = this.getFocusedChild();
			button.requestFocus(FOCUS_DOWN);
			return true;
		} else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
			View button = this.getFocusedChild();
			button.requestFocus(FOCUS_RIGHT);
			return true;
		} else if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
			View button = this.getFocusedChild();
			button.requestFocus(FOCUS_UP);
			return true;
		} else if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
			View button = this.getFocusedChild();
			button.performClick();
			return true;
		} else {
			show(sDefaultTimeout);
		}
		// TODO Auto-generated method stub
		return super.onKeyDown(keyCode, event);
	}

	private View.OnClickListener mPauseListener = new View.OnClickListener() {
		public void onClick(View v) {
			doPauseResume();
			show(sDefaultTimeout);
		}
	};

	private void updatePausePlay() {
		if (mRoot == null || mPauseButton == null)
			return;

		if (mPlayer.isPlaying())
			mPauseButton.setImageResource(R.drawable.osd_pause_nf);
		else
			mPauseButton.setImageResource(R.drawable.osd_play_nf);
	}

	private void doPauseResume() {
		if (mPlayer.isPlaying())
			mPlayer.pause();
		else
			mPlayer.start();
		updatePausePlay();
	}

	private OnSeekBarChangeListener mSeekListener = new OnSeekBarChangeListener() {
		public void onStartTrackingTouch(SeekBar bar) {
			mDragging = true;
			show(3600000);
			mHandler.removeMessages(SHOW_PROGRESS);
			if (mInstantSeeking)
				mAM.setStreamMute(AudioManager.STREAM_MUSIC, true);
			// if (mInfoView != null) {
			// mInfoView.setText("");
			// mInfoView.setVisibility(View.VISIBLE);
			// }
		}

		public void onProgressChanged(SeekBar bar, int progress,
				boolean fromuser) {
			if (!fromuser)
				return;

			long newposition = (mDuration * progress) / 1000;
			String time = new SimpleDateFormat("HH:mm:ss", Locale.getDefault())
					.format(new Date(newposition));
			if (mInstantSeeking)
				mPlayer.seekTo(newposition);
			// if (mInfoView != null)
			// mInfoView.setText(time);
			if (mCurrentTime != null)
				mCurrentTime.setText(time);
		}

		public void onStopTrackingTouch(SeekBar bar) {
			if (!mInstantSeeking)
				mPlayer.seekTo((mDuration * bar.getProgress()) / 1000);
			// if (mInfoView != null) {
			// mInfoView.setText("");
			// mInfoView.setVisibility(View.GONE);
			// }
			show(sDefaultTimeout);
			mHandler.removeMessages(SHOW_PROGRESS);
			mAM.setStreamMute(AudioManager.STREAM_MUSIC, false);
			mDragging = false;
			mHandler.sendEmptyMessageDelayed(SHOW_PROGRESS, 1000);
		}
	};

	@Override
	public void setEnabled(boolean enabled) {
		if (mPauseButton != null)
			mPauseButton.setEnabled(enabled);
		if (mProgress != null)
			mProgress.setEnabled(enabled);
		disableUnsupportedButtons();
		super.setEnabled(enabled);
	}

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

}