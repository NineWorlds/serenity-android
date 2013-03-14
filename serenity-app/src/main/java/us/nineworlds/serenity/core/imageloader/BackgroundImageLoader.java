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

package us.nineworlds.serenity.core.imageloader;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import us.nineworlds.serenity.SerenityApplication;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.DiscCacheUtil;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.assist.MemoryCacheUtil;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.core.download.HttpClientImageDownloader;
import com.nostra13.universalimageloader.utils.IoUtils;

public class BackgroundImageLoader implements Runnable {

	ImageLoader imageLoader;
	View bgLayout;
	int defaultBGDrawableImageId;
	String imageURL;
	BaseImageDownloader httpLoader;

	/**
	 * 
	 */
	public BackgroundImageLoader(String url, View bgLayout,
			int defaultBGDrawalbleId) {
		imageURL = url;
		imageLoader = SerenityApplication.getImageLoader();
		httpLoader = new BaseImageDownloader(bgLayout.getContext());
		this.bgLayout = bgLayout;
		defaultBGDrawableImageId = defaultBGDrawalbleId;
	}

	/**
	 * Call and fetch an image directly.
	 */
	public void run() {

		if (imageURL != null) {
			Bitmap bmp;
			ImageSize size = new ImageSize(1280, 720);
			String memoryCacheKey = MemoryCacheUtil.generateKey(imageURL, size);

			bmp = imageLoader.getMemoryCache().get(memoryCacheKey);
			if (bmp != null && !bmp.isRecycled()) {
				BitmapDisplayer displayer = new BitmapDisplayer(bmp);
				Activity activity = (Activity) bgLayout.getContext();
				activity.runOnUiThread(displayer);
			} else {
				File image = imageLoader.getDiscCache().get(imageURL);
				if (image.exists()) {
					InputStream is;
					try {
						is = new FileInputStream(image);
						bmp = BitmapFactory.decodeStream(is);
					} catch (FileNotFoundException e) {

					}
				} else {
					InputStream is;
					try {
						is = httpLoader.getStream(imageURL, null);
						try {
							OutputStream os = new BufferedOutputStream(
									new FileOutputStream(image));
							try {
								IoUtils.copyStream(is, os);
							} finally {
								IoUtils.closeSilently(os);
							}
							imageLoader.getDiscCache().put(imageURL, image);
							File fetchLoadedImage = imageLoader.getDiscCache().get(imageURL);
							InputStream fis = new FileInputStream(fetchLoadedImage);
							bmp = BitmapFactory.decodeStream(new FileInputStream(fetchLoadedImage));
							IoUtils.closeSilently(fis);
						} finally {
							IoUtils.closeSilently(is);
						}
					} catch (IOException e) {
					}
				}
			}
			BitmapDisplayer bd = new BitmapDisplayer(bmp);
			Activity activity = (Activity) bgLayout.getContext();
			activity.runOnUiThread(bd);

		}

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
