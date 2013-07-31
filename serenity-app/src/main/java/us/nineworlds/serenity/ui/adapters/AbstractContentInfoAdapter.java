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

package us.nineworlds.serenity.ui.adapters;

import us.nineworlds.plex.rest.PlexappFactory;
import us.nineworlds.serenity.SerenityApplication;

import com.nostra13.universalimageloader.core.ImageLoader;

import android.app.Activity;
import android.os.Handler;
import android.widget.BaseAdapter;

/**
 * @author dcarver
 *
 */
public abstract class AbstractContentInfoAdapter extends BaseAdapter {

	protected static Activity context;
	protected ImageLoader imageLoader;
	protected PlexappFactory factory;
	protected Handler handler;
	protected String key;
	protected String category;
	
	
	/**
	 * Base constructor for a Content Info Adapter
	 */
	public AbstractContentInfoAdapter(Activity c, String key, String category) {
		context = (Activity) c;
		this.key = key;
		this.category = category;
		imageLoader = SerenityApplication.getImageLoader();
	}
	
	protected abstract void fetchDataFromService();
	
	@Override
	public long getItemId(int position) {
		return position;
	}

}
