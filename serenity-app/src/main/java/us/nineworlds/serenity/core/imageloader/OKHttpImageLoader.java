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

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;

import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.squareup.okhttp.OkHttpClient;

/**
 * @author dcarver
 *
 */
public class OKHttpImageLoader extends BaseImageDownloader {

	private OkHttpClient client;
	/**
	 * @param context
	 */
	public OKHttpImageLoader(Context context, OkHttpClient client) {
		super(context);
		this.client = client;
	}
	
	/* (non-Javadoc)
	 * @see com.nostra13.universalimageloader.core.download.BaseImageDownloader#getStreamFromNetwork(java.lang.String, java.lang.Object)
	 */
	@Override
	protected InputStream getStreamFromNetwork(String imageUri, Object extra)
			throws IOException {
		HttpURLConnection connection = client.open(new URL(imageUri));
		
		InputStream in = null;
		in = connection.getInputStream();
		return in;
	}


}
