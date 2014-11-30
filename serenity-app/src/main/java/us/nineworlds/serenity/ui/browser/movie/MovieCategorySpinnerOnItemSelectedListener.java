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

import java.util.List;

import us.nineworlds.serenity.R;
import us.nineworlds.serenity.core.model.CategoryInfo;
import us.nineworlds.serenity.core.model.SecondaryCategoryInfo;
import us.nineworlds.serenity.core.services.SecondaryCategoryRetrievalIntentService;
import us.nineworlds.serenity.ui.activity.SerenityMultiViewVideoActivity;
import us.nineworlds.serenity.ui.adapters.AbstractPosterImageGalleryAdapter;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class MovieCategorySpinnerOnItemSelectedListener extends
BaseSpinnerOnItemSelectedListener implements OnItemSelectedListener {

	protected Spinner secondarySpinner;

	private static String category;
	private String savedInstanceCategory; // From a restarted activity
	private final Handler secondaryCategoryHandler;

	public MovieCategorySpinnerOnItemSelectedListener(String defaultSelection,
			String ckey) {
		super(defaultSelection, ckey);
		secondaryCategoryHandler = new SecondaryCategoryHandler();
	}

	public MovieCategorySpinnerOnItemSelectedListener(String defaultSelection,
			String ckey, boolean firstSelection) {
		this(defaultSelection, key);
		savedInstanceCategory = defaultSelection;
		this.setFirstSelection(firstSelection);
	}

	@Override
	public void onItemSelected(AdapterView<?> viewAdapter, View view,
			int position, long id) {
		if (view == null) {
			return;
		}

		setMultiViewVideoActivity((SerenityMultiViewVideoActivity) view
				.getContext());

		findViews();

		CategoryInfo item = (CategoryInfo) viewAdapter
				.getItemAtPosition(position);

		if (savedInstanceCategory != null) {
			int savedInstancePosition = getSavedInstancePosition(viewAdapter);
			item = (CategoryInfo) viewAdapter
					.getItemAtPosition(savedInstancePosition);
			viewAdapter.setSelection(savedInstancePosition);
			savedInstanceCategory = null;
			if (item.getLevel() == 0) {
				createGallery(item);
			} else {
				populateSecondaryCategory();
				return;
			}
		}

		if (isFirstSelection()) {

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

			createGallery(item);

			setFirstSelection(false);
			return;
		}

		if (selected.equalsIgnoreCase(item.getCategory())) {
			return;
		}

		selected = item.getCategory();
		category = item.getCategory();
		categoryState.setCategory(selected);

		if (item.getLevel() == 0) {
			secondarySpinner.setVisibility(View.INVISIBLE);
			createGallery(item);
		} else {
			populateSecondaryCategory();
		}
	}

	@Override
	protected void findViews() {
		super.findViews();
		secondarySpinner = (Spinner) getMultiViewVideoActivity().findViewById(
				R.id.categoryFilter2);
	}

	protected void populateSecondaryCategory() {
		Messenger messenger = new Messenger(secondaryCategoryHandler);
		Intent categoriesIntent = new Intent(getMultiViewVideoActivity(),
				SecondaryCategoryRetrievalIntentService.class);
		categoriesIntent.putExtra("key", key);
		categoriesIntent.putExtra("category", category);
		categoriesIntent.putExtra("MESSENGER", messenger);
		getMultiViewVideoActivity().startService(categoriesIntent);
	}

	protected void createGallery(CategoryInfo item) {
		AbstractPosterImageGalleryAdapter adapter = getPosterImageAdapter(item);
		if (getMultiViewVideoActivity().isGridViewActive()) {
			refreshGridView(adapter);
			return;
		}
		refreshGallery(adapter);
	}

	/**
	 * @param item
	 * @return
	 */
	protected AbstractPosterImageGalleryAdapter getPosterImageAdapter(
			CategoryInfo item) {
		AbstractPosterImageGalleryAdapter adapter = new MoviePosterImageAdapter(
				getMultiViewVideoActivity(), key, item.getCategory());
		return adapter;
	}

	private class SecondaryCategoryHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			List<SecondaryCategoryInfo> secondaryCategories = (List<SecondaryCategoryInfo>) msg.obj;

			if (secondaryCategories == null || secondaryCategories.isEmpty()) {
				Toast.makeText(getMultiViewVideoActivity(),
						R.string.no_entries_available_for_category_,
						Toast.LENGTH_LONG).show();
				return;
			}

			Spinner secondarySpinner = (Spinner) getMultiViewVideoActivity()
					.findViewById(R.id.categoryFilter2);
			secondarySpinner.setVisibility(View.VISIBLE);

			ArrayAdapter<SecondaryCategoryInfo> spinnerSecArrayAdapter = new ArrayAdapter<SecondaryCategoryInfo>(
					getMultiViewVideoActivity(),
					R.layout.serenity_spinner_textview, secondaryCategories);
			spinnerSecArrayAdapter
					.setDropDownViewResource(R.layout.serenity_spinner_textview_dropdown);
			secondarySpinner.setAdapter(spinnerSecArrayAdapter);
			secondarySpinner
					.setOnItemSelectedListener(new SecondaryCategorySpinnerOnItemSelectedListener(
							category, key));
		}

	}

	@Override
	protected String getSavedCategory() {
		return categoryState.getCategory();
	}

}
