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

package us.nineworlds.serenity.ui.search;

import java.net.URLEncoder;
import java.util.List;

import us.nineworlds.serenity.MainMenuTextViewAdapter;
import us.nineworlds.serenity.R;
import us.nineworlds.serenity.core.menus.MenuItem;
import us.nineworlds.serenity.core.model.VideoContentInfo;
import us.nineworlds.serenity.core.services.MovieSearchIntentService;
import us.nineworlds.serenity.ui.activity.SerenityVideoActivity;
import us.nineworlds.serenity.ui.browser.movie.MoviePosterOnItemSelectedListener;
import us.nineworlds.serenity.ui.listeners.GalleryVideoOnItemClickListener;
import us.nineworlds.serenity.ui.listeners.GalleryVideoOnItemLongClickListener;
import us.nineworlds.serenity.widgets.SerenityGallery;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.provider.SearchRecentSuggestions;
import android.view.View;
import android.widget.Toast;

/**
 * Implements basic search functionality for movies.
 * 
 * @author dcarver
 * 
 */
public class SearchableActivity extends SerenityVideoActivity {

	protected Handler searchHandler;
	protected static View bgLayout;
	protected static Activity context;

	@Override
	protected void createSideMenu() {

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;

		setContentView(R.layout.activity_movie_browser);
		View v = findViewById(R.id.metaDataRow);
		v.setVisibility(View.GONE);
		bgLayout = findViewById(R.id.movieBrowserBackgroundLayout);
		handleIntent(getIntent());
	}

	@Override
	protected void onNewIntent(Intent intent) {
		setIntent(intent);
		handleIntent(intent);
	}

	protected void handleIntent(Intent intent) {
		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			String query = intent.getStringExtra(SearchManager.QUERY);
			String key = null;

			SearchRecentSuggestions suggestions = new SearchRecentSuggestions(
					this, SerenitySuggestionProvider.AUTHORITY,
					SerenitySuggestionProvider.MODE);
			suggestions.saveRecentQuery(query, null);

			searchHandler = new MovieSearchHandler();
			Messenger messenger = new Messenger(searchHandler);

			List<MenuItem> menuItems = MainMenuTextViewAdapter.menuItems;

			if (menuItems != null) {

				for (MenuItem menuItem : menuItems) {
					if ("movie".equals(menuItem.getType())) {
						key = menuItem.getSection();
					}
				}
			}

			Intent searchIntent = new Intent(this,
					MovieSearchIntentService.class);

			searchIntent.putExtra("key", key);
			searchIntent.putExtra("query", URLEncoder.encode(query));
			searchIntent.putExtra("MESSENGER", messenger);
			startService(searchIntent);
		}

	}

	/**
	 * @param item
	 * @param bgLayout
	 */
	protected static void createGallery(List<VideoContentInfo> videos,
			View bgLayout) {
		SerenityGallery posterGallery = (SerenityGallery) context
				.findViewById(R.id.moviePosterGallery);
		posterGallery.setAdapter(new SearchAdapter(context, videos));
		posterGallery
				.setOnItemSelectedListener(new MoviePosterOnItemSelectedListener(
						context));
		posterGallery
				.setOnItemClickListener(new GalleryVideoOnItemClickListener());
		posterGallery
				.setOnItemLongClickListener(new GalleryVideoOnItemLongClickListener());
		posterGallery.setSpacing(25);
		posterGallery.setAnimationDuration(1);
	}

	protected static class MovieSearchHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			if (msg.obj != null) {
				List<VideoContentInfo> videos = (List<VideoContentInfo>) msg.obj;
				if (videos != null && videos.isEmpty()) {
					Toast.makeText(
							context,
							R.string.no_videos_found_that_match_the_search_criteria,
							Toast.LENGTH_LONG).show();
					context.finish();
				} else {
					createGallery(videos, bgLayout);
				}
			}
		}
	}
}
