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

package us.nineworlds.serenity.ui.browser.tv;

import com.jess.ui.TwoWayAdapterView;
import com.jess.ui.TwoWayAdapterView.OnItemClickListener;

import us.nineworlds.serenity.core.model.SeriesContentInfo;
import us.nineworlds.serenity.ui.browser.tv.seasons.TVShowSeasonBrowserActivity;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;

/**
 * @author dcarver
 * 
 */
public class TVShowGridOnItemClickListener implements
		OnItemClickListener {

	private Activity context;

	/**
	 * 
	 */
	public TVShowGridOnItemClickListener(Context c) {
		context = (Activity) c;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.widget.AdapterView.OnItemClickListener#onItemClick(android.widget
	 * .AdapterView, android.view.View, int, long)
	 */
	@Override
	public void onItemClick(TwoWayAdapterView<?> av, View view, int position,
			long arg3) {
		
		SeriesContentInfo videoInfo = (SeriesContentInfo) av.getItemAtPosition(position);
	
		Intent i = new Intent(context, TVShowSeasonBrowserActivity.class);
		i.putExtra("key", videoInfo.getKey());
		context.startActivityForResult(i, 0);
	}

}
