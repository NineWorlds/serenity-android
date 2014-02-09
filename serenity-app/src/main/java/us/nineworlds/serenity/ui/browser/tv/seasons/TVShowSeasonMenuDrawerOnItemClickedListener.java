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

import net.simonvt.menudrawer.MenuDrawer;
import us.nineworlds.serenity.R;
import us.nineworlds.serenity.ui.util.VideoPlayerIntentUtils;

import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * @author dcarver
 *
 */
public class TVShowSeasonMenuDrawerOnItemClickedListener implements OnItemClickListener {
	private static final int PLAY_ALL_QUEUE = 0;
	private MenuDrawer menuDrawer;
	
	/**
	 * 
	 */
	public TVShowSeasonMenuDrawerOnItemClickedListener(MenuDrawer drawer) {
		menuDrawer = drawer;
	}



	/* (non-Javadoc)
	 * @see android.widget.AdapterView.OnItemClickListener#onItemClick(android.widget.AdapterView, android.view.View, int, long)
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Activity activity = (Activity) view.getContext();
		
		switch (position) {
		case PLAY_ALL_QUEUE:
			menuDrawer.toggleMenu();
			if (!activity.getPackageManager().hasSystemFeature("android.hardware.touchscreen")) {
				parent.setVisibility(View.INVISIBLE);
			}
			activity.findViewById(R.id.tvShowSeasonImageGallery).requestFocus();
			VideoPlayerIntentUtils.playAllFromQueue(activity);
			return;
		}
		menuDrawer.toggleMenu();
		activity.recreate();
	}
}
