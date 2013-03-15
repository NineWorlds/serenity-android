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

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import org.teleal.cling.model.meta.Device;

import us.nineworlds.plex.rest.PlexappFactory;
import us.nineworlds.plex.rest.config.IConfiguration;
import us.nineworlds.serenity.core.ServerConfig;

import com.google.analytics.tracking.android.EasyTracker;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

/**
 * Global manager for the Serenity application
 * 
 * @author dcarver
 * 
 */
public class SerenityApplication extends Application {

	private static final String HTTPCACHE = "httpcache";

	private static PlexappFactory plexFactory;

	private static ConcurrentHashMap<String, Device> plexmediaServers = new ConcurrentHashMap<String, Device>();
	private static ImageLoader imageLoader;

	@Override
	public void onCreate() {
		super.onCreate();
		
		installHttpCache();
		
		
		EasyTracker.getInstance().setContext(this);
		
		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
        .cacheInMemory()
        .cacheOnDisc()
        .bitmapConfig(Bitmap.Config.RGB_565)
        .showImageOnFail(R.drawable.default_error)
        .showStubImage(R.drawable.default_video_cover)
        .build();
		
		 ImageLoaderConfiguration imageLoaderconfig = new ImageLoaderConfiguration.Builder(this)
		 .memoryCacheExtraOptions(1280,720)
		 .taskExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
		 .taskExecutorForCachedImages(AsyncTask.THREAD_POOL_EXECUTOR)
		 .threadPoolSize(5)
     	 .tasksProcessingOrder(QueueProcessingType.FIFO)
		 .denyCacheImageMultipleSizesInMemory()
		 .defaultDisplayImageOptions(defaultOptions)
		 .memoryCache(new WeakMemoryCache())
		 .build();		

		 imageLoader = ImageLoader.getInstance();
		 imageLoader.init(imageLoaderconfig);

		// Temporarily clean the cache
		// imageManager.getFileManager().clean();

		IConfiguration config = ServerConfig.getInstance(this);
		plexFactory = PlexappFactory.getInstance(config);
		String deviceModel = android.os.Build.MODEL;

		EasyTracker.getTracker().sendEvent("Devices", "Started Application",
				deviceModel, (long) 0);

	}
	
	/**
	 * Install an HTTPResponseCache. This is using an open source library so
	 * that caching occurs across all platforms not just 4.x.
	 * 
	 */
	protected void installHttpCache() {
		final long cacheMaxSize = 10 * 1024 * 1024;
		final File httpCacheDir = new File(getCacheDir(), HTTPCACHE);
		try {
			com.integralblue.httpresponsecache.HttpResponseCache.install(
					httpCacheDir, cacheMaxSize);
		} catch (IOException ex) {
			Log.e(getClass().getName(), "Unable to install cache", ex);
		}
	}
	

	
	

	public static PlexappFactory getPlexFactory() {
		return plexFactory;
	}

	public static boolean isGoogleTV(Context context) {
		final PackageManager pm = context.getPackageManager();
		return pm.hasSystemFeature("com.google.android.tv");
	}

	public static ConcurrentHashMap<String, Device> getPlexMediaServers() {
		return plexmediaServers;
	}
	
	public static ImageLoader getImageLoader() {
		return imageLoader;
	}

}
