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

package com.github.kingargyle.plexappclient.core.imageloader;

import java.io.File;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;

import com.github.kingargyle.plexappclient.SerenityApplication;
import com.novoda.imageloader.core.ImageManager;
import com.novoda.imageloader.core.LoaderSettings;
import com.novoda.imageloader.core.bitmap.BitmapUtil;
import com.novoda.imageloader.core.cache.CacheManager;
import com.novoda.imageloader.core.file.FileManager;

public class BackgroundImageLoader implements Runnable {

	ImageManager imageManager;
	View bgLayout;
	int defaultBGDrawableImageId;
	String imageURL;

	/**
	 * 
	 */
	public BackgroundImageLoader(String url, View bgLayout,
			int defaultBGDrawalbleId) {
		imageURL = url;
		imageManager = SerenityApplication.getImageManager();
		this.bgLayout = bgLayout;
		defaultBGDrawableImageId = defaultBGDrawalbleId;
	}

	/**
	 * Call and fetch an image directly.
	 */
	public void run() {

		CacheManager cm = imageManager.getCacheManager();
		Bitmap bm = cm.get(imageURL, 1280, 720);

		if (imageURL != null && bm == null) {
			FileManager fm = imageManager.getFileManager();
			File f = fm.getFile(imageURL);
			LoaderSettings settings = SerenityApplication.getLoaderSettings();
			if (!f.exists()) {
				settings.getNetworkManager().retrieveImage(
						imageURL, f);
			}

			BitmapUtil bmu = SerenityApplication.getLoaderSettings()
					.getBitmapUtil();
			bm = bmu.decodeFile(f, 1280, 720);
		}

		Activity activity = (Activity) bgLayout.getContext();
		activity.runOnUiThread(new BitmapDisplayer(bm));
	}

	protected class BitmapDisplayer implements Runnable {

		private Bitmap bm;

		/**
	 * 
	 */
		public BitmapDisplayer(Bitmap bm) {
			this.bm = bm;
		}

		public void run() {
			if (bm == null) {
				bgLayout.setBackgroundResource(defaultBGDrawableImageId);
				return;
			}

			BitmapDrawable bmd = new BitmapDrawable(bm);
			bgLayout.setBackgroundDrawable(bmd);
		}

	}

}
