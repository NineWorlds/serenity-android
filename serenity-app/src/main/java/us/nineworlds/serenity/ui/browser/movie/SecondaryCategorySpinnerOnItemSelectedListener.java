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

import us.nineworlds.serenity.core.model.SecondaryCategoryInfo;
import us.nineworlds.serenity.ui.activity.SerenityMultiViewVideoActivity;
import us.nineworlds.serenity.ui.adapters.AbstractPosterImageGalleryAdapter;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;

/**
 * Populate the movie posters based on the information from the Secondary
 * categories.
 */
public class SecondaryCategorySpinnerOnItemSelectedListener extends
		BaseSpinnerOnItemSelectedListener implements OnItemSelectedListener {

	public SecondaryCategorySpinnerOnItemSelectedListener(
			String defaultSelection, String key) {
		super(defaultSelection, key);
	}

	@Override
	public void onItemSelected(AdapterView<?> viewAdapter, View view,
			int position, long id) {

		setMultiViewVideoActivity((SerenityMultiViewVideoActivity) view
				.getContext());

		findViews();

		SecondaryCategoryInfo item = (SecondaryCategoryInfo) viewAdapter
				.getItemAtPosition(position);

		if (isFirstSelection()) {
			if (categoryState.getGenreCategory() != null) {
				int savedInstancePosition = getSavedInstancePosition(viewAdapter);
				item = (SecondaryCategoryInfo) viewAdapter
						.getItemAtPosition(savedInstancePosition);
				viewAdapter.setSelection(savedInstancePosition);
			}
			setFirstSelection(false);
		}

		if (selected.equalsIgnoreCase(item.getCategory())) {
			return;
		}

		selected = item.getCategory();

		categoryState.setGenreCategory(item.getCategory());

		AbstractPosterImageGalleryAdapter adapter = getPosterImageAdapter(item);

		if (getMultiViewVideoActivity().isGridViewActive()) {
			refreshGridView(adapter);
			return;
		}

		refreshGallery(adapter);

	}

	protected AbstractPosterImageGalleryAdapter getPosterImageAdapter(
			SecondaryCategoryInfo item) {
		AbstractPosterImageGalleryAdapter adapter = new MoviePosterImageAdapter(
				getMultiViewVideoActivity(), key, item.getParentCategory()
						+ "/" + item.getCategory());
		return adapter;
	}

	@Override
	protected String getSavedCategory() {
		return categoryState.getGenreCategory();
	}

}
