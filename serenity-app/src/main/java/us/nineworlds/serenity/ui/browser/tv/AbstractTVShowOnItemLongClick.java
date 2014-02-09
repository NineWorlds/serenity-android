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

import java.util.ArrayList;

import us.nineworlds.serenity.R;
import us.nineworlds.serenity.core.model.SeriesContentInfo;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * @author dcarver
 *
 */
public abstract class AbstractTVShowOnItemLongClick {

	protected Dialog dialog;
	protected Activity context;
	protected SeriesContentInfo videoInfo;
	protected static View view;
	/**
	 * 
	 */
	public void init() {
		context = (Activity) view.getContext();
	}
	/**
	 * 
	 */
	public void createAndShowDialog() {
		dialog = new Dialog(context);
		AlertDialog.Builder builder = new AlertDialog.Builder(
				new ContextThemeWrapper(context,
						android.R.style.Theme_Holo_Dialog));
		builder.setTitle(context.getString(R.string.video_options));
	
		ListView modeList = new ListView(context);
		modeList.setSelector(R.drawable.menu_item_selector);
		ArrayList<String> options = new ArrayList<String>();
		options.add(context.getString(R.string.toggle_watched_status));
	
		ArrayAdapter<String> modeAdapter = new ArrayAdapter<String>(
				context, R.layout.simple_list_item,
				R.id.list_item_text, options);
		
		modeList.setAdapter(modeAdapter);
		modeList.setOnItemClickListener(new DialogOnItemSelected());
	
		builder.setView(modeList);
		dialog = builder.create();
		dialog.show();
	}
	
	protected class DialogOnItemSelected implements OnItemClickListener {

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * android.widget.AdapterView.OnItemClickListener#onItemClick(android
		 * .widget.AdapterView, android.view.View, int, long)
		 */
		@Override
		public void onItemClick(android.widget.AdapterView<?> av, View v,
				int position, long arg3) {
									
			switch (position) {
			case 0:
				View posterLayout = (View) view.getParent();
				ImageView watchedView = (ImageView) posterLayout.findViewById(R.id.posterWatchedIndicator);
				View progressView = posterLayout.findViewById(R.id.posterInprogressIndicator);
				progressView.setVisibility(View.INVISIBLE);
				if (videoInfo.isWatched()) {
					watchedView.setVisibility(View.INVISIBLE);
				} else {
					watchedView.setImageResource(R.drawable.overlaywatched);
					watchedView.setVisibility(View.VISIBLE);
				}
				videoInfo.toggleWatchedStatus();
				
				Activity a = (Activity) v.getContext();
				TextView tv = (TextView) a.findViewById(R.id.tvShowWatchedUnwatched);
				if (tv != null) {
					String wu = "";
					wu = context.getString(R.string.watched_) + " " + videoInfo.getShowsWatched();
					wu = wu + " " + context.getString(R.string.unwatched_) + " " + videoInfo.getShowsUnwatched();
					tv.setText(wu);
					tv.refreshDrawableState();
				}
				break;
			}
			dialog.dismiss();
		}
	}
	

}
