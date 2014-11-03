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

import javax.inject.Inject;

import us.nineworlds.serenity.R;
import us.nineworlds.serenity.injection.BaseInjector;
import us.nineworlds.serenity.ui.activity.SerenityMultiViewVideoActivity;
import us.nineworlds.serenity.ui.util.VideoPlayerIntentUtils;
import us.nineworlds.serenity.widgets.DrawerLayout;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class MovieMenuDrawerOnItemClickedListener extends BaseInjector
		implements OnItemClickListener {
	private static final int GRID_VIEW = 0;
	private static final int DETAIL_VIEW = 1;
	private static final int PLAY_ALL_QUEUE = 2;
	private final DrawerLayout menuDrawer;

	@Inject
	VideoPlayerIntentUtils vpUtils;

	public MovieMenuDrawerOnItemClickedListener(DrawerLayout drawer) {
		super();
		menuDrawer = drawer;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		SerenityMultiViewVideoActivity activity = (SerenityMultiViewVideoActivity) view
				.getContext();

		switch (position) {
		case GRID_VIEW:
			activity.setContentView(R.layout.activity_movie_browser_gridview);

			toggleView(activity, true);
			break;
		case DETAIL_VIEW:
			activity.setContentView(R.layout.activity_movie_browser);
			toggleView(activity, false);
			break;
		case PLAY_ALL_QUEUE:
			playAllFromQueue(parent, activity);
			return;
		}
		menuDrawer.closeDrawers();
		activity.recreate();
	}

	private void playAllFromQueue(AdapterView<?> parent,
			SerenityMultiViewVideoActivity activity) {
		menuDrawer.closeDrawers();
		if (!activity.getPackageManager().hasSystemFeature(
				"android.hardware.touchscreen")) {
			parent.setVisibility(View.INVISIBLE);
		}
		View gallery = activity.findViewById(R.id.moviePosterGallery);
		if (gallery != null) {
			gallery.requestFocusFromTouch();
		}
		vpUtils.playAllFromQueue(activity);
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
