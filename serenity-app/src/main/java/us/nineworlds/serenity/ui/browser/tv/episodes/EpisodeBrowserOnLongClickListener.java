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

package us.nineworlds.serenity.ui.browser.tv.episodes;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import us.nineworlds.serenity.R;
import us.nineworlds.serenity.SerenityApplication;
import us.nineworlds.serenity.core.services.UnWatchVideoAsyncTask;
import us.nineworlds.serenity.core.services.WatchedVideoAsyncTask;
import us.nineworlds.serenity.ui.listeners.GalleryVideoOnItemLongClickListener;
import us.nineworlds.serenity.ui.util.ImageInfographicUtils;

/**
 * @author dcarver
 *
 */
public class EpisodeBrowserOnLongClickListener extends
		GalleryVideoOnItemLongClickListener {
	
	/* (non-Javadoc)
	 * @see us.nineworlds.serenity.ui.listeners.AbstractVideoOnItemLongClickListener#onItemLongClick()
	 */
	@Override
	protected boolean onItemLongClick() {
		info = vciv.getPosterInfo();
		context = (Activity) vciv.getContext();

		dialog = new Dialog(context);
		AlertDialog.Builder builder = new AlertDialog.Builder(
				new ContextThemeWrapper(context,
						android.R.style.Theme_Holo));
		builder.setTitle(context.getString(R.string.video_options));

		ListView modeList = new ListView(context);
		ArrayList<String> options = new ArrayList<String>();
		options.add(context.getString(R.string.toggle_watched_status));
		options.add(context.getString(R.string.download_video_to_device));
		options.add(context.getString(R.string.view_all_episodes));
		if (!SerenityApplication.isGoogleTV(context)) {
			options.add(context.getString(R.string.play_on_tv));
		}

		ArrayAdapter<String> modeAdapter = new ArrayAdapter<String>(context,
				android.R.layout.simple_list_item_1, android.R.id.text1,
				options);

		modeList.setAdapter(modeAdapter);
		modeList.setOnItemClickListener(new EpisodeDialogOnItemSelected());

		builder.setView(modeList);
		dialog = builder.create();
		dialog.show();

		return true;
	}
	
	protected void performListAllEpisodesForSeason() {
		Intent i = new Intent(context, EpisodeBrowserActivity.class);
		i.putExtra("key", info.getParentKey() + "/children");
		context.startActivityForResult(i, 0);		
	}
	
	protected class EpisodeDialogOnItemSelected implements OnItemClickListener {

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * android.widget.AdapterView.OnItemClickListener#onItemClick(android
		 * .widget.AdapterView, android.view.View, int, long)
		 */
		public void onItemClick(android.widget.AdapterView<?> arg0, View v,
				int position, long arg3) {

			switch (position) {
			case 0:
				performWatchedToggle();
				break;
			case 1:
				startDownload();
				break;
			case 2:
				performListAllEpisodesForSeason();
			default:
				performGoogleTVSecondScreen();
			}
			dialog.dismiss();
		}

	}	

}
