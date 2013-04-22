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

package us.nineworlds.serenity.ui.browser.tv;

import java.util.List;

import us.nineworlds.serenity.core.model.CategoryInfo;
import us.nineworlds.serenity.core.model.SecondaryCategoryInfo;
import us.nineworlds.serenity.core.services.MovieSecondaryCategoryRetrievalIntentService;
import us.nineworlds.serenity.ui.browser.tv.episodes.EpisodeBrowserActivity;

import us.nineworlds.serenity.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Gallery;
import android.widget.Toast;

/**
 * @author dcarver
 * 
 */
public class CategorySpinnerOnItemSelectedListener implements
		OnItemSelectedListener {

	private String selected;
	private static String key;
	private boolean firstSelection = true;
	private static Activity context;
	private static String category;
	private Handler secondaryCategoryHandler;

	public CategorySpinnerOnItemSelectedListener(String defaultSelection,
			String ckey) {
		selected = defaultSelection;
		key = ckey;
		secondaryCategoryHandler = new SecondaryCategoryHandler();
	}

	public void onItemSelected(AdapterView<?> viewAdapter, View view,
			int position, long id) {
		context = (Activity) view.getContext();
		CategoryInfo item = (CategoryInfo) viewAdapter
				.getItemAtPosition(position);

		if (firstSelection) {
			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
			String filter = prefs.getString("serenity_category_filter", "all");
			
			int count = viewAdapter.getCount();
			for (int i = 0; i < count; i++) {
				CategoryInfo citem = (CategoryInfo) viewAdapter
						.getItemAtPosition(i);
				if (citem.getCategory().equals(filter)) {
					item = citem;
					selected = citem.getCategory();
					viewAdapter.setSelection(i);
					continue;
				}
			}
			
			if (item.getCategory().equals("newest")
					|| item.getCategory().equals("recentlyAdded")
					|| item.getCategory().equals("recentlyViewed")
					|| item.getCategory().equals("onDeck")) {
				Intent i = new Intent(context, EpisodeBrowserActivity.class);
				i.putExtra("key",
						"/library/sections/" + key + "/" + item.getCategory());
				context.startActivityForResult(i, 0);
			} else {
				setupImageGallery(item);
			}			
			setupImageGallery(item);
			firstSelection = false;
			return;
		}

		if (selected.equalsIgnoreCase(item.getCategory())) {
			return;
		}

		selected = item.getCategory();
		category = item.getCategory();

		Spinner secondarySpinner = (Spinner) context
				.findViewById(R.id.tvshow_movieCategoryFilter2);

		if (item.getLevel() == 0) {

			if (item.getCategory().equals("newest")
					|| item.getCategory().equals("recentlyAdded")
					|| item.getCategory().equals("recentlyViewed")
					|| item.getCategory().equals("onDeck")) {
				Intent i = new Intent(context, EpisodeBrowserActivity.class);
				i.putExtra("key",
						"/library/sections/" + key + "/" + item.getCategory());
				context.startActivityForResult(i, 0);
			} else {
				secondarySpinner.setVisibility(View.INVISIBLE);
				setupImageGallery(item);
			}
		} else {
			Messenger messenger = new Messenger(secondaryCategoryHandler);
			Intent categoriesIntent = new Intent(context,
					MovieSecondaryCategoryRetrievalIntentService.class);
			categoriesIntent.putExtra("key", key);
			categoriesIntent.putExtra("category", category);
			categoriesIntent.putExtra("MESSENGER", messenger);
			context.startService(categoriesIntent);
		}

	}

	/**
	 * @param item
	 */
	protected void setupImageGallery(CategoryInfo item) {
		View bgLayout = context.findViewById(R.id.tvshowBrowserLayout);
		Gallery posterGallery = (Gallery) context
				.findViewById(R.id.tvShowBannerGallery);

		if (!TVShowBrowserActivity.USE_POSTER_LAYOUT) {
			posterGallery.setAdapter(new TVShowBannerImageGalleryAdapter(
					context, key, item.getCategory()));
		} else {
			posterGallery.setAdapter(new TVShowPosterImageGalleryAdapter(
					context, key, item.getCategory()));
		}
		posterGallery
				.setOnItemSelectedListener(new TVShowGalleryOnItemSelectedListener(
						bgLayout, context));
		posterGallery
				.setOnItemClickListener(new TVShowBrowserGalleryOnItemClickListener(
						context));
		posterGallery
				.setOnItemLongClickListener(new ShowOnItemLongClickListener());

	}

	public void onNothingSelected(AdapterView<?> va) {

	}

	private static class SecondaryCategoryHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			List<SecondaryCategoryInfo> secondaryCategories = (List<SecondaryCategoryInfo>) msg.obj;

			if (secondaryCategories == null || secondaryCategories.isEmpty()) {
				Toast.makeText(context,
						R.string.no_entries_available_for_category_,
						Toast.LENGTH_LONG).show();
				return;
			}

			Spinner secondarySpinner = (Spinner) context
					.findViewById(R.id.tvshow_movieCategoryFilter2);
			secondarySpinner.setVisibility(View.VISIBLE);

			ArrayAdapter<SecondaryCategoryInfo> spinnerSecArrayAdapter = new ArrayAdapter<SecondaryCategoryInfo>(
					context, R.layout.serenity_spinner_textview,
					secondaryCategories);
			spinnerSecArrayAdapter
					.setDropDownViewResource(R.layout.serenity_spinner_textview_dropdown);
			secondarySpinner.setAdapter(spinnerSecArrayAdapter);
			secondarySpinner
					.setOnItemSelectedListener(new SecondaryCategorySpinnerOnItemSelectedListener(
							category, key));

		}

	}

}
