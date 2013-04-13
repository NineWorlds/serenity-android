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
import us.nineworlds.serenity.core.services.UnWatchVideoAsyncTask;
import us.nineworlds.serenity.core.services.WatchedVideoAsyncTask;
import us.nineworlds.serenity.ui.views.TVShowSeasonImageView;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Gallery;
import android.widget.ListView;
import android.widget.TextView;

/**
 * A listener that handles long press for video content. Includes displaying a
 * dialog for toggling watched and unwatched status as well for possibly playing
 * on the TV.
 * 
 * @author dcarver
 * 
 */
public class ShowOnItemLongClickListener implements OnItemLongClickListener {

	private Dialog dialog;
	private Activity context;
	private SeriesContentInfo info;
	private TVShowBannerImageView tvsv;

	public boolean onItemLongClick(AdapterView<?> av, View v,
			int position, long arg3) {

		// Google TV is sending back different results than Nexus 7
		// So we try to handle the different results.

		if (v == null) {
			Gallery g = (Gallery) av;
			tvsv = (TVShowBannerImageView) g.getSelectedView();
		} else {
			if (v instanceof TVShowBannerImageView) {
				tvsv = (TVShowBannerImageView) v;
			} else {
				Gallery g = (Gallery) v;
				tvsv = (TVShowBannerImageView) g.getSelectedView();
			}
		}

		info = tvsv.getPosterInfo();
		context = (Activity) tvsv.getContext();

		dialog = new Dialog(context);
		AlertDialog.Builder builder = new AlertDialog.Builder(
				new ContextThemeWrapper(context,
						android.R.style.Theme_Holo_Dialog));
		builder.setTitle(context.getString(R.string.video_options));

		ListView modeList = new ListView(context);
		ArrayList<String> options = new ArrayList<String>();
		options.add(context.getString(R.string.toggle_watched_status));

		ArrayAdapter<String> modeAdapter = new ArrayAdapter<String>(context,
				android.R.layout.simple_list_item_1, android.R.id.text1,
				options);

		modeList.setAdapter(modeAdapter);
		modeList.setOnItemClickListener(new DialogOnItemSelected());

		builder.setView(modeList);
		dialog = builder.create();
		dialog.show();

		return true;
	}

	protected class DialogOnItemSelected implements OnItemClickListener {

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * android.widget.AdapterView.OnItemClickListener#onItemClick(android
		 * .widget.AdapterView, android.view.View, int, long)
		 */
		public void onItemClick(android.widget.AdapterView<?> arg0, View v,
				int position, long arg3) {
			
			int watched = 0;
			int unwatched = 0;

			if (info.getShowsWatched() != null) {
				watched = Integer.valueOf(info.getShowsWatched());
			}
			
			if (info.getShowsUnwatched() != null) {
				unwatched = Integer.valueOf(info.getShowsUnwatched());
			}
			
			int total = watched + unwatched;

			switch (position) {
			case 0:
				if (Integer.valueOf(info.getShowsWatched()) > 0) {
					new UnWatchVideoAsyncTask().execute(info.id());
					info.setShowsWatched("0");
					info.setShowsUnwatched(Integer.valueOf(total).toString());
				} else {
					new WatchedVideoAsyncTask().execute(info.id());
					info.setShowsWatched(Integer.valueOf(total).toString());
					info.setShowsUnwatched("0");
				}
				Activity a = (Activity) v.getContext();
				TextView tv = (TextView) a.findViewById(R.id.tvShowWatchedUnwatched);
				
				String wu = "";
				wu = context.getString(R.string.watched_) + " " + info.getShowsWatched();
				wu = wu + " " + context.getString(R.string.unwatched_) + " " + info.getShowsUnwatched();
				tv.setText(wu);
				tv.refreshDrawableState();
				break;
			}
			dialog.dismiss();
		}
	}
}
