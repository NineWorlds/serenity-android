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

package us.nineworlds.serenity;

import net.simonvt.menudrawer.MenuDrawer;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * @author dcarver
 * 
 */
public class MainMenuDrawerOnItemClickedListener implements OnItemClickListener {
	private static final int ABOUT = 0;
	private static final int CLEAR_CACHE = 1;
	private static final int CLEAR_QUEUE = 2;
	private final MenuDrawer menuDrawer;

	/**
	 * 
	 */
	public MainMenuDrawerOnItemClickedListener(MenuDrawer drawer,
			ViewGroup focus) {
		menuDrawer = drawer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.widget.AdapterView.OnItemClickListener#onItemClick(android.widget
	 * .AdapterView, android.view.View, int, long)
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Activity activity = (Activity) view.getContext();
		ListView lv = (ListView) menuDrawer.getMenuView().findViewById(
				R.id.menu_list_options);
		lv.setVisibility(View.GONE);
		menuDrawer.toggleMenu(true);

		switch (position) {
		case CLEAR_CACHE:
			createClearCacheDialog(view.getContext());
			break;
		case ABOUT:
			AboutDialog about = new AboutDialog(view.getContext());
			about.setTitle(R.string.about_title_serenity_for_google_tv);
			about.show();
			break;
		case CLEAR_QUEUE:
			SerenityApplication.getVideoPlaybackQueue().clear();
			Toast.makeText(
					activity,
					activity.getResources().getString(
							R.string.queue_has_been_cleared_),
					Toast.LENGTH_LONG).show();
			break;
		}
	}

	protected void createClearCacheDialog(Context context) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				context, android.R.style.Theme_Holo_Dialog);

		alertDialogBuilder.setTitle(R.string.options_main_clear_image_cache);
		alertDialogBuilder
				.setMessage(R.string.option_clear_the_image_cache_)
				.setCancelable(true)
				.setPositiveButton(R.string.clear,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {

								ImageLoader imageLoader = SerenityApplication
										.getImageLoader();
								imageLoader.clearDiscCache();
								imageLoader.clearMemoryCache();
							}
						})
				.setNegativeButton(R.string.cancel,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {

							}
						});

		alertDialogBuilder.create();
		alertDialogBuilder.show();

	}

}
