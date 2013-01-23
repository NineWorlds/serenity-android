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

package com.github.kingargyle.plexappclient;

import java.io.IOException;

import com.github.kingargyle.plexapp.PlexappFactory;
import com.github.kingargyle.plexapp.config.IConfiguration;
import com.github.kingargyle.plexappclient.core.ConcurrentLoader;
import com.github.kingargyle.plexappclient.core.LoaderSettings;
import com.github.kingargyle.plexappclient.core.ServerConfig;
import com.novoda.imageloader.core.ImageManager;

import android.app.Application;
import android.os.StrictMode;

/**
 * Global manager for the Serenity application
 * 
 * @author dcarver
 *
 */
public class SerenityApplication extends Application {
	
	private static ImageManager imageManager;
	private static PlexappFactory plexFactory;
	private static LoaderSettings settings;

	
	@Override
	public void onCreate() {
		super.onCreate();
		
		// Yes I know this is bad, really need to make network activity happen
		// in AsyncTask.
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);
		
		settings = new LoaderSettings.SettingsBuilder()
	      .withDisconnectOnEveryCall(true).withUpsampling(true).build(this);
		settings.setLoader(new ConcurrentLoader(settings));
		
	    imageManager = new ImageManager(this, settings);
	    
	    // Temporarily clean the cache
	    //imageManager.getFileManager().clean();
	    
	    IConfiguration config = ServerConfig.getInstance(this);
		plexFactory = PlexappFactory.getInstance(config);
		
	}
	
	public static ImageManager getImageManager() {
	    return imageManager;
	}
	
	public static PlexappFactory getPlexFactory() {
		return plexFactory;
	}
	
	public static LoaderSettings getLoaderSettings() {
		return settings;
	}
	

}
