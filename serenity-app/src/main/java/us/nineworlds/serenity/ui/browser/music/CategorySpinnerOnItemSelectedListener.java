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

package us.nineworlds.serenity.ui.browser.music;

import java.util.List;

import com.jess.ui.TwoWayGridView;

import us.nineworlds.serenity.core.model.CategoryInfo;
import us.nineworlds.serenity.core.model.SecondaryCategoryInfo;
import us.nineworlds.serenity.core.services.SecondaryCategoryRetrievalIntentService;
import us.nineworlds.serenity.ui.browser.music.albums.MusicAlbumsActivity;
import us.nineworlds.serenity.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Gallery;
import android.widget.Spinner;
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

	@Override
	public void onItemSelected(AdapterView<?> viewAdapter, View view,
			int position, long id) {
		context = (Activity) view.getContext();

		CategoryInfo item = (CategoryInfo) viewAdapter
				.getItemAtPosition(position);

		if (firstSelection) {
			int count = viewAdapter.getCount();
			for (int i = 0; i < count; i++) {
				CategoryInfo citem = (CategoryInfo) viewAdapter
						.getItemAtPosition(i);
				if (citem.getCategory().equals("all")) {
					item = citem;
					selected = citem.getCategory();
					viewAdapter.setSelection(i);
					continue;
				}
			}
			
			createGallery(item);

			firstSelection = false;
			return;
		}

		if (selected.equalsIgnoreCase(item.getCategory())) {
			return;
		}

		selected = item.getCategory();
		category = item.getCategory();

		Spinner secondarySpinner = (Spinner) context
				.findViewById(R.id.musicCategoryFilter2);

		if (item.getLevel() == 0) {
			secondarySpinner.setVisibility(View.INVISIBLE);
			if (item.getCategory().equals("albums") ||
				item.getCategory().equals("recentlyAdded")) {
				Intent i = new Intent(context, MusicAlbumsActivity.class);
				String albumKey = "/library/sections/" + key + "/" + item.getCategory(); 
				i.putExtra("key", albumKey);
				context.startActivityForResult(i, 0);
			} else {
				createGallery(item);
			}
		} else {
			Messenger messenger = new Messenger(secondaryCategoryHandler);
			Intent categoriesIntent = new Intent(context,
					SecondaryCategoryRetrievalIntentService.class);
			categoriesIntent.putExtra("key", key);
			categoriesIntent.putExtra("category", category);
			categoriesIntent.putExtra("MESSENGER", messenger);
			context.startService(categoriesIntent);
		}

	}

	/**
	 * @param item
	 * @param bgLayout
	 */
	protected void createGallery(CategoryInfo item) {
		if (!MusicActivity.MUSIC_GRIDVIEW) {
			MusicPosterGalleryAdapter adapter = new MusicPosterGalleryAdapter(
					context, key, item.getCategory());
			Gallery posterGallery = (Gallery) context
					.findViewById(R.id.musicArtistGallery);
			posterGallery.setAdapter(adapter);
			posterGallery
					.setOnItemSelectedListener(new MusicPosterGalleryOnItemSelectedListener(
							context));
			posterGallery.setOnItemClickListener(new MusicPosterGalleryOnItemClickListener());
			posterGallery.setSpacing(25);
			posterGallery.setAnimationDuration(1);
			posterGallery.setCallbackDuringFling(false);
		} else {
			MusicPosterGridViewAdapter adapter = new MusicPosterGridViewAdapter(context, key, item.getCategory());
			TwoWayGridView gridView = (TwoWayGridView) context
					.findViewById(R.id.musicGridView);
			gridView.setAdapter(adapter);
			gridView.setOnItemClickListener(new MusicGridOnItemClickListener());
			gridView.setOnItemSelectedListener(new MusicGridOnItemSelectedListener(
					context));
		}
	}

	@Override
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
					.findViewById(R.id.musicCategoryFilter2);
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
