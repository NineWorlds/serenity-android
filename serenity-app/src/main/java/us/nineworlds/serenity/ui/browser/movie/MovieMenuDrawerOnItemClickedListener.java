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

package us.nineworlds.serenity.ui.browser.movie;

import net.simonvt.menudrawer.MenuDrawer;
import us.nineworlds.serenity.R;
import us.nineworlds.serenity.ui.activity.SerenityMultiViewVideoActivity;
import us.nineworlds.serenity.ui.util.VideoPlayerIntentUtils;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * @author dcarver
 * 
 */
public class MovieMenuDrawerOnItemClickedListener implements
		OnItemClickListener {
	private static final int GRID_VIEW = 0;
	private static final int DETAIL_VIEW = 1;
	private static final int PLAY_ALL_QUEUE = 2;
	private final MenuDrawer menuDrawer;

	/**
	 * 
	 */
	public MovieMenuDrawerOnItemClickedListener(MenuDrawer drawer) {
		menuDrawer = drawer;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		SerenityMultiViewVideoActivity activity = (SerenityMultiViewVideoActivity) view
				.getContext();

		switch (position) {
		case GRID_VIEW:
			menuDrawer.setContentView(R.layout.activity_movie_browser_gridview);
			toggleView(activity, true);
			break;
		case DETAIL_VIEW:
			menuDrawer.setContentView(R.layout.activity_movie_browser);
			toggleView(activity, false);
			break;
		case PLAY_ALL_QUEUE:
			playAllFromQueue(parent, activity);
			return;
		}
		menuDrawer.toggleMenu();
		activity.recreate();
	}

	private void playAllFromQueue(AdapterView<?> parent,
			SerenityMultiViewVideoActivity activity) {
		menuDrawer.toggleMenu();
		if (!activity.getPackageManager().hasSystemFeature(
				"android.hardware.touchscreen")) {
			parent.setVisibility(View.INVISIBLE);
		}
		View gallery = activity.findViewById(R.id.moviePosterGallery);
		if (gallery != null) {
			gallery.requestFocusFromTouch();
		}
		VideoPlayerIntentUtils.playAllFromQueue(activity);
	}

	/**
	 * @param listView
	 */
	public void hideMenuItems() {
	}

	/**
	 * @param activity
	 */
	protected void toggleView(SerenityMultiViewVideoActivity activity,
			boolean enableGridView) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(activity);
		Editor e = prefs.edit();
		activity.setGridViewEnabled(enableGridView);
		e.putBoolean("movie_layout_grid", enableGridView);
		e.apply();
	}
}
