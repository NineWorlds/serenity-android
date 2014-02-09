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

package us.nineworlds.serenity.ui.listeners;



import java.util.ArrayList;
import java.util.List;

import com.jess.ui.TwoWayAdapterView;
import com.jess.ui.TwoWayAdapterView.OnItemLongClickListener;
import com.jess.ui.TwoWayGridView;
import com.nostra13.universalimageloader.core.ImageLoader;

import us.nineworlds.serenity.R;
import us.nineworlds.serenity.SerenityApplication;
import us.nineworlds.serenity.core.menus.DialogMenuItem;
import us.nineworlds.serenity.core.model.VideoContentInfo;
import us.nineworlds.serenity.core.model.impl.Subtitle;
import android.app.AlertDialog;
import android.app.Dialog;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView.ScaleType;

/**
 * A listener that handles long press for video content in Grid View classes.
 * 
 * @author dcarver
 * 
 */
public class GridVideoOnItemLongClickListener extends AbstractVideoOnItemLongClickListener implements
		OnItemLongClickListener {


	@Override
	public boolean onItemLongClick(TwoWayAdapterView<?> av, View v,
			int position, long arg3) {

		info = (VideoContentInfo) av.getItemAtPosition(position);
		// Google TV is sending back different results than Nexus 7
		// So we try to handle the different results.

		if (v == null) {
			TwoWayGridView g = (TwoWayGridView) av;
			vciv = (ImageView) g.getSelectedView();
		} else {
			if (v instanceof ImageView) {
				vciv = (ImageView) v;
			} else {
				vciv = (ImageView) v.findViewById(R.id.posterImageView);
			}
		}

		return onItemLongClick();
	}
	
	@Override
	protected ArrayList<DialogMenuItem> addMenuOptions() {
		ArrayList<DialogMenuItem> options = super.addMenuOptions();
		DialogMenuItem infoItem = createMenuItem("Details", 300);
		options.add(infoItem);
		if (info.getAvailableSubtitles() != null && !info.getAvailableSubtitles().isEmpty()) {
			DialogMenuItem menuItem = createMenuItem("Select Subtitle", 200);
			options.add(menuItem);
		}
		return options;
	}
	
	@Override
	protected OnItemClickListener getDialogSelectedListener() {
		return new GridDialogOnItemSelected();
	}
	
	protected class GridDialogOnItemSelected implements OnItemClickListener {

		@Override
		public void onItemClick(android.widget.AdapterView<?> arg0, View v,
				int position, long arg3) {

			DialogMenuItem menuItem = (DialogMenuItem) arg0
					.getItemAtPosition(position);

			switch (menuItem.getMenuDialogAction()) {
			case 0:
				performWatchedToggle();
				break;
			case 1:
				startDownload();
				break;
			case 2:
				performAddToQueue();
				break;
			case 3:
				performPlayTrailer();
				break;
			case 4:
				performGoogleTVSecondScreen();
				break;
			case 300:
				performInfoDialog();
				break;
			case 200:
				performSubtitleSelection();
				break;
			}
			v.requestFocusFromTouch();
			dialog.dismiss();
		}

	}
	
	Dialog subtitleDialog;
	protected void performSubtitleSelection() {
		dialog.dismiss();
		AlertDialog.Builder builder = new AlertDialog.Builder(
				new ContextThemeWrapper(context,
						android.R.style.Theme_Holo_Dialog));
		builder.setTitle("Subtitle Selection");
				
		ListView modeList = new ListView(context);
		modeList.setSelector(R.drawable.menu_item_selector);
		List<Subtitle> options = info.getAvailableSubtitles();
		
		ArrayAdapter<Subtitle> modeAdapter = new ArrayAdapter<Subtitle>(
				context, R.layout.simple_list_item,
				R.id.list_item_text, options);
		modeList.setAdapter(modeAdapter);
		modeList.setOnItemClickListener(new SubTitleSelection());
		
		builder.setView(modeList);
	    subtitleDialog = builder.create();
		subtitleDialog.show();
		
		
	}
	
	Dialog detailsDialog;
	protected void performInfoDialog() {
		dialog.dismiss();
		detailsDialog =  new Dialog(
				new ContextThemeWrapper(context,
						android.R.style.Theme_Holo_Dialog));
		detailsDialog.setContentView(R.layout.details_layout);
		detailsDialog.setTitle("Details");
		ImageLoader imageLoader = SerenityApplication.getImageLoader();
		ImageView posterImage = (ImageView) detailsDialog.findViewById(R.id.video_poster);
		posterImage.setVisibility(View.VISIBLE);
		posterImage.setScaleType(ScaleType.FIT_XY);
		SerenityApplication.displayImage(info.getImageURL(), posterImage);
		
		TextView summary = (TextView) detailsDialog.findViewById(R.id.movieSummary);
		summary.setText(info.getSummary());

		TextView title = (TextView) detailsDialog
				.findViewById(R.id.movieBrowserPosterTitle);
		title.setText(info.getTitle());
		detailsDialog.show();
	}
	
	private class SubTitleSelection implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			info.setSubtitle(info.getAvailableSubtitles().get(position));
			subtitleDialog.dismiss();
		}
		
	}
	
}
