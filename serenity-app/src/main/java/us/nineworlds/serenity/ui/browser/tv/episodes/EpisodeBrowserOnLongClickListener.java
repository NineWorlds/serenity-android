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
import us.nineworlds.serenity.core.menus.DialogMenuItem;
import us.nineworlds.serenity.ui.listeners.GalleryVideoOnItemLongClickListener;

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
		context = (Activity) vciv.getContext();

		dialog = new Dialog(context);
		AlertDialog.Builder builder = new AlertDialog.Builder(
				new ContextThemeWrapper(context,
						android.R.style.Theme_Holo));
		builder.setTitle(context.getString(R.string.video_options));

		ListView modeList = new ListView(context);
		modeList.setSelector(R.drawable.menu_item_selector);
		ArrayList<DialogMenuItem> options = addMenuOptions();

		ArrayAdapter<DialogMenuItem> modeAdapter = new ArrayAdapter<DialogMenuItem>(context,
				R.layout.simple_list_item, R.id.list_item_text,
				options);

		modeList.setAdapter(modeAdapter);
		modeList.setOnItemClickListener(new EpisodeDialogOnItemSelected());

		builder.setView(modeList);
		dialog = builder.create();
		dialog.show();

		return true;
	}
	
	/* (non-Javadoc)
	 * @see us.nineworlds.serenity.ui.listeners.AbstractVideoOnItemLongClickListener#addMenuOptions()
	 */
	@Override
	protected ArrayList<DialogMenuItem> addMenuOptions() {
		ArrayList<DialogMenuItem> options = super.addMenuOptions();
		options.add(createMenuItemViewAllEpisodes());
		return options;
	}
	
	protected DialogMenuItem createMenuItemViewAllEpisodes() {
		return createMenuItem(context.getString(R.string.view_all_episodes), 2);
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
		@Override
		public void onItemClick(android.widget.AdapterView<?> arg0, View v,
				int position, long arg3) {
			
			DialogMenuItem menuItem = (DialogMenuItem) arg0.getItemAtPosition(position);

			switch (menuItem.getMenuDialogAction()) {
			case 0:
				performWatchedToggle();
				break;
			case 1:
				startDownload();
				break;
			case 2:
				performListAllEpisodesForSeason();
				
			case 3:
				performPlayTrailer();
				break;
				
			default:
				performGoogleTVSecondScreen();
			}
			dialog.dismiss();
		}

	}	

}
