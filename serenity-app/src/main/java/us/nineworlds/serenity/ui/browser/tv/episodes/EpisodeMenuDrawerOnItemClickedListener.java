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

import android.content.Context;
import android.content.ContextWrapper;
import us.nineworlds.serenity.R;
import us.nineworlds.serenity.injection.BaseInjector;
import us.nineworlds.serenity.ui.activity.SerenityMultiViewVideoActivity;
import us.nineworlds.serenity.ui.util.VideoPlayerIntentUtils;
import us.nineworlds.serenity.widgets.DrawerLayout;
import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import javax.inject.Inject;

/**
 * @author dcarver
 *
 */
public class EpisodeMenuDrawerOnItemClickedListener extends BaseInjector
implements OnItemClickListener {
	private static final int PLAY_ALL_QUEUE = 0;
	private final DrawerLayout menuDrawer;

	@Inject
	protected VideoPlayerIntentUtils vpUtils;

	public EpisodeMenuDrawerOnItemClickedListener(DrawerLayout drawer) {
		super();
		menuDrawer = drawer;
	}

	protected Activity getActivity(Context contextWrapper) {
		Context context = contextWrapper;
		while (context instanceof ContextWrapper) {
			if (context instanceof Activity) {
				return (Activity) context;
			}
			context = ((ContextWrapper)context).getBaseContext();
		}
		return null;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Activity activity = getActivity(view.getContext());

		switch (position) {
		case PLAY_ALL_QUEUE:
			menuDrawer.closeDrawers();
			if (!activity.getPackageManager().hasSystemFeature(
					"android.hardware.touchscreen")) {
				parent.setVisibility(View.INVISIBLE);
			}
			activity.findViewById(R.id.moviePosterGallery).requestFocus();
			vpUtils.playAllFromQueue(activity);
			break;
		}
	}
}
