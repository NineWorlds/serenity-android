/**
 * The MIT License (MIT)
 * Copyright (c) 2012-2013 David Carver
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

package us.nineworlds.serenity;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import us.nineworlds.plex.rest.PlexappFactory;
import us.nineworlds.plex.rest.config.IConfiguration;
import us.nineworlds.serenity.core.ServerConfig;
import us.nineworlds.serenity.core.imageloader.OKHttpImageLoader;
import us.nineworlds.serenity.core.model.Server;
import us.nineworlds.serenity.core.model.VideoContentInfo;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.preference.PreferenceManager;
import android.widget.ImageView;

import com.castillo.dd.PendingDownload;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Tracker;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
import com.squareup.okhttp.OkHttpClient;

/**
 * Global manager for the Serenity application
 * 
 * @author dcarver
 * 
 */
public class SerenityApplication extends Application {

	private static final String COM_GOOGLE_ANDROID_TV = "com.google.android.tv";
	private static boolean enableTracking = true;
	private static ImageLoader imageLoader;
	private static DisplayImageOptions movieOptions;
	private static DisplayImageOptions musicOptions;
	private static List<PendingDownload> pendingDownloads;
	protected static PlexappFactory plexFactory;

	private static ConcurrentHashMap<String, Server> plexmediaServers = new ConcurrentHashMap<String, Server>();

	public static final int PROGRESS = 0xDEADBEEF;

	private static DisplayImageOptions reflectiveOptions;

	private static LinkedList<VideoContentInfo> videoQueue = new LinkedList<VideoContentInfo>();

	public static void disableTracking() {
		enableTracking = false;
	}

	public static void displayImage(String imageUrl, ImageView view) {
		displayImage(imageUrl, view, null);
	}

	public static void displayImage(String imageUrl, ImageView view,
			DisplayImageOptions displayImageOptions) {
		displayImage(imageUrl, view, displayImageOptions, null);
	}

	public static void displayImage(String imageUrl, ImageView view,
			DisplayImageOptions displayImageOptions,
			ImageLoadingListener imageLoaderListener) {
		final ImageViewAware imageViewAware = new ImageViewAware(view);
		imageLoader.displayImage(plexFactory.getImageURL(imageUrl,
				imageViewAware.getWidth(), imageViewAware.getHeight()), view,
				displayImageOptions, imageLoaderListener);
	}

	public static void displayImage(String imageUrl, ImageView view, int resId) {
		final ImageViewAware imageViewAware = new ImageViewAware(view);
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.cacheInMemory(true).cacheOnDisc(true)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.showImageForEmptyUri(resId).build();
		imageLoader.displayImage(imageUrl, imageViewAware, options);
	}

	public static ImageLoader getImageLoader() {
		return imageLoader;
	}

	public static DisplayImageOptions getMovieOptions() {
		return movieOptions;
	}

	public static DisplayImageOptions getMusicOptions() {
		return musicOptions;
	}

	public static List<PendingDownload> getPendingDownloads() {
		return pendingDownloads;
	}

	public static PlexappFactory getPlexFactory() {
		return plexFactory;
	}

	public static ConcurrentHashMap<String, Server> getPlexMediaServers() {
		return plexmediaServers;
	}

	public static DisplayImageOptions getReflectiveOptions() {
		return reflectiveOptions;
	}

	/**
	 * Retrieves the video playback queue. Items will be added and removed from
	 * the queue and used by the video player for playback of Episodes and
	 * Movies.
	 * 
	 * When an episode is finished playing it should be removed from the queue.
	 * 
	 * This queue is thread safe.
	 * 
	 * @return
	 */
	public static LinkedList<VideoContentInfo> getVideoPlaybackQueue() {
		return videoQueue;
	}

	public static boolean isAndroidTV(Context context) {
		final PackageManager pm = context.getPackageManager();

		if (Build.MODEL.startsWith("AFT")
				&& Build.MANUFACTURER.equals("Amazon")) {
			return true;
		}
		return pm.hasSystemFeature("android.hardware.type.television");
	}

	public static boolean isGoogleTV(Context context) {
		final PackageManager pm = context.getPackageManager();
		return pm.hasSystemFeature(COM_GOOGLE_ANDROID_TV);
	}

	/**
	 * Checks if the app is running on OUYA.
	 * 
	 * @return true if the app is running on OUYA
	 */
	public static boolean isRunningOnOUYA() {
		if ("OUYA".equals(Build.MANUFACTURER)) {
			return true;
		}
		return false;
	}

	public static boolean isTrackingEnabled() {
		return enableTracking;
	}

	/**
	 * 
	 */
	public SerenityApplication() {
		pendingDownloads = new ArrayList<PendingDownload>();

	}

	protected void configureImageLoader() {
		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
				.cacheInMemory(true).cacheOnDisc(true)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.resetViewBeforeLoading(true)
				.showImageForEmptyUri(R.drawable.default_video_cover).build();

		musicOptions = new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisc(true).bitmapConfig(Bitmap.Config.RGB_565)
				.showImageForEmptyUri(R.drawable.default_music).build();

		movieOptions = new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisc(true).bitmapConfig(Bitmap.Config.RGB_565)
				.resetViewBeforeLoading(true).build();

		reflectiveOptions = new DisplayImageOptions.Builder()
				.cacheInMemory(true).cacheOnDisc(true)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.displayer(new RoundedBitmapDisplayer(10)).build();

		ImageLoaderConfiguration imageLoaderconfig = new ImageLoaderConfiguration.Builder(
				this)
				.threadPoolSize(5)
				.tasksProcessingOrder(QueueProcessingType.FIFO)
				.imageDownloader(
						new OKHttpImageLoader(this, new OkHttpClient()))
				.defaultDisplayImageOptions(defaultOptions).build();

		imageLoader = ImageLoader.getInstance();
		imageLoader.init(imageLoaderconfig);
	}

	/**
	 * 
	 */
	public void init() {
		configureImageLoader();
		initializePlexappFactory();
		if (enableTracking) {
			installAnalytics();
		}
		sendStartedApplicationEvent();
	}

	/**
	 * 
	 */
	protected void initializePlexappFactory() {
		IConfiguration config = ServerConfig.getInstance(this);
		plexFactory = PlexappFactory.getInstance(config);
	}

	/**
	 * 
	 */
	protected void installAnalytics() {
		EasyTracker.getInstance().setContext(this);
	}

	@Override
	public void onCreate() {
		super.onCreate();
		init();
		PreferenceManager.setDefaultValues(this, R.xml.preferences, true);
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		SharedPreferences.Editor editor = prefs.edit();
		if (isGoogleTV(this) || isAndroidTV(this)) {
			editor.putBoolean("serenity_tv_mode", true);
			editor.apply();
		}
	}

	/**
	 * @param deviceModel
	 */
	protected void sendStartedApplicationEvent() {
		String deviceModel = android.os.Build.MODEL;
		if (enableTracking) {
			Tracker tracker = EasyTracker.getTracker();
			if (tracker != null) {
				tracker.sendEvent("Devices", "Started Application",
						deviceModel, (long) 0);

			}
		}
	}
}
