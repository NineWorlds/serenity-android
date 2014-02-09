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

package us.nineworlds.serenity.ui.browser.tv.seasons;



import java.util.ArrayList;
import java.util.List;

import com.jess.ui.TwoWayGridView;

import us.nineworlds.serenity.R;
import us.nineworlds.serenity.SerenityApplication;
import us.nineworlds.serenity.core.model.SeriesContentInfo;
import us.nineworlds.serenity.core.model.VideoContentInfo;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

/**
 * A listener that handles long press for video content in Poster Gallery classes.
 * 
 * @author dcarver
 * 
 */
public class SeasonOnItemLongClickListener implements
		OnItemLongClickListener {

	protected Dialog dialog;
	protected Activity context;
	protected ImageView vciv;
	protected SeriesContentInfo info;
	
	/**
	 * 
	 */
	public SeasonOnItemLongClickListener(Activity context) {
		this.context = context;
	}
	

	@Override
	public boolean onItemLongClick(AdapterView<?> av, View v,
			int position, long arg3) {

		// Google TV is sending back different results than Nexus 7
		// So we try to handle the different results.
		
		info = (SeriesContentInfo) av.getItemAtPosition(position);

		if (v == null) {
			Gallery g = (Gallery) av;
			vciv = (ImageView) g.getSelectedView().findViewById(R.id.posterImageView);
		} else {
			if (v instanceof ImageView) {
				vciv = (ImageView) v;
			} else {
				vciv = (ImageView) av.getSelectedView().findViewById(R.id.posterImageView);;
			}
		}

		return onItemLongClick();
		
	}
	
	/**
	 * @return
	 */
	protected boolean onItemLongClick() {
		dialog = new Dialog(context);
		AlertDialog.Builder builder = new AlertDialog.Builder(
				new ContextThemeWrapper(context,
						android.R.style.Theme_Holo));
		builder.setTitle(context.getString(R.string.season_options));

		ListView modeList = new ListView(context);
		modeList.setSelector(R.drawable.menu_item_selector);
		ArrayList<String> options = new ArrayList<String>();
		options.add(context.getString(R.string.add_season_to_queue));

		ArrayAdapter<String> modeAdapter = new ArrayAdapter<String>(context,
				R.layout.simple_list_item, R.id.list_item_text,
				options);

		modeList.setAdapter(modeAdapter);
		modeList.setOnItemClickListener(new DialogOnItemSelected());

		builder.setView(modeList);
		dialog = builder.create();
		dialog.show();

		return true;
	}
	
	protected void performAddToQueue() {
		TwoWayGridView gridView = (TwoWayGridView) context.findViewById(R.id.episodeGridView);
		if (gridView != null) {
			SeasonsEpisodePosterImageGalleryAdapter adapter = (SeasonsEpisodePosterImageGalleryAdapter) gridView.getAdapter();
			List<VideoContentInfo> episodes = adapter.getItems();
			SerenityApplication.getVideoPlaybackQueue().addAll(episodes);
			Toast.makeText(context, adapter.getCount() + " videos have been added to the queue.", Toast.LENGTH_LONG).show();
			View v = context.findViewById(R.id.tvShowSeasonImageGallery);
			if (v != null) {
				v.requestFocusFromTouch();
			}
		}
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
		public void onItemClick(android.widget.AdapterView<?> arg0, View v,
				int position, long arg3) {

			switch (position) {
			case 0:
				performAddToQueue();
				break;
			default:
				
			}
			dialog.dismiss();
		}

	}
	
	
}
