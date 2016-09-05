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

import net.ganin.darv.DpadAwareRecyclerView;
import us.nineworlds.serenity.core.menus.MenuItem;
import us.nineworlds.serenity.ui.browser.movie.MovieBrowserActivity;
import us.nineworlds.serenity.ui.browser.music.MusicActivity;
import us.nineworlds.serenity.ui.browser.tv.TVShowBrowserActivity;
import us.nineworlds.serenity.ui.preferences.SerenityPreferenceActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;

public class GalleryOnItemClickListener implements DpadAwareRecyclerView.OnItemClickListener {

	private static final String MENU_TYPE_SEARCH = "search";
	private static final String MENU_TYPE_SHOW = "show";
	private static final String MENU_TYPE_MOVIE = "movie";
	private static final String MENU_TYPE_MUSIC = "artist";
	private static final String MENU_TYPE_OPTIONS = "options";
	private Activity context;

	public GalleryOnItemClickListener(Context c) {
		context = (Activity) c;
	}

	@Override
	public void onItemClick(DpadAwareRecyclerView dpadAwareRecyclerView, View view, int i, long l) {
		MainMenuTextViewAdapter mainMenuTextViewAdapter = (MainMenuTextViewAdapter) dpadAwareRecyclerView.getAdapter();
		MenuItem menuItem = mainMenuTextViewAdapter.getItemAtPosition(i);
		String librarySection = menuItem.getSection();
		String activityType = menuItem.getType();

		if (MENU_TYPE_SEARCH.equalsIgnoreCase(activityType)) {
			context.onSearchRequested();
			return;
		}

		if (MENU_TYPE_OPTIONS.equalsIgnoreCase(activityType)) {
			context.openOptionsMenu();
			return;
		}

		Intent intent;


		if (MENU_TYPE_MOVIE.equalsIgnoreCase(activityType)) {
			intent = new Intent(context, MovieBrowserActivity.class);
			intent.putExtra("key", librarySection);
			context.startActivityForResult(intent, 0);
			return;
		}

		if (MENU_TYPE_SHOW.equalsIgnoreCase(activityType)) {
			intent = new Intent(context, TVShowBrowserActivity.class);
			intent.putExtra("key", librarySection);
			context.startActivityForResult(intent, 0);
			return;
		}

		if (MENU_TYPE_MUSIC.equalsIgnoreCase(activityType)) {
			intent = new Intent(context, MusicActivity.class);
			intent.putExtra("key", librarySection);
			context.startActivityForResult(intent, 0);
			return;
		}

		intent = new Intent(context, SerenityPreferenceActivity.class);
		intent.putExtra("key", librarySection);
		context.startActivityForResult(intent, 0);
	}
}
