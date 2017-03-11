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

import javax.inject.Inject;

import net.ganin.darv.DpadAwareRecyclerView;
import us.nineworlds.serenity.R;
import us.nineworlds.serenity.core.model.CategoryInfo;
import us.nineworlds.serenity.core.model.SecondaryCategoryInfo;
import us.nineworlds.serenity.injection.BaseInjector;
import us.nineworlds.serenity.ui.activity.SerenityMultiViewVideoActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;

/**
 * Populate the tv show banners based on the information from the Secondary
 * categories.
 *
 * @author dcarver
 *
 */
public class TVSecondaryCategorySpinnerOnItemSelectedListener extends
		BaseInjector implements OnItemSelectedListener {

	private String selected;
	private final String key;
	private boolean firstTimesw = true;
	private SerenityMultiViewVideoActivity activity;

	@Inject
	protected TVCategoryState categoryState;

	public TVSecondaryCategorySpinnerOnItemSelectedListener(
			String defaultSelection, String key, SerenityMultiViewVideoActivity serenityMultiViewVideoActivity) {
		super();
		selected = defaultSelection;
		this.key = key;
		activity = serenityMultiViewVideoActivity;
	}

	@Override
	public void onItemSelected(AdapterView<?> viewAdapter, View view,
			int position, long id) {

		SecondaryCategoryInfo item = (SecondaryCategoryInfo) viewAdapter
				.getItemAtPosition(position);

		if (firstTimesw) {
			if (categoryState.getGenreCategory() != null) {
				int savedInstancePosition = getSavedInstancePosition(viewAdapter);
				item = (SecondaryCategoryInfo) viewAdapter
						.getItemAtPosition(savedInstancePosition);
				viewAdapter.setSelection(savedInstancePosition);
			}
			firstTimesw = false;
		}

		if (selected.equalsIgnoreCase(item.getCategory())) {
			return;
		}

		selected = item.getCategory();
		categoryState.setGenreCategory(item.getCategory());


		View bgLayout = activity.findViewById(R.id.tvshowBrowserLayout);
		DpadAwareRecyclerView dpadAwareRecyclerView;
		if (activity.isGridViewActive()) {
			dpadAwareRecyclerView = (DpadAwareRecyclerView) activity.findViewById(R.id.tvShowGridView);
			dpadAwareRecyclerView.setAdapter(new TVShowPosterImageGalleryAdapter(activity, key,
					item.getParentCategory() + "/" + item.getCategory()));
			dpadAwareRecyclerView.setOnItemSelectedListener(new TVShowGridOnItemSelectedListener(
					bgLayout, activity));
		} else {
			DpadAwareRecyclerView posterGallery = (DpadAwareRecyclerView) activity
					.findViewById(R.id.tvShowBannerGallery);

			if (activity.isPosterLayoutActive()) {
				posterGallery.setAdapter(new TVShowPosterImageGalleryAdapter(activity,
						key, item.getParentCategory() + "/"
								+ item.getCategory()));
			} else {
				posterGallery.setAdapter(new TVShowBannerImageGalleryAdapter(activity,
						key, item.getParentCategory() + "/"
								+ item.getCategory()));
			}

			posterGallery
					.setOnItemSelectedListener(new TVShowGalleryOnItemSelectedListener(activity));
//			posterGallery
//					.setOnItemLongClickListener(new ShowOnItemLongClickListener());
		}
	}

	private int getSavedInstancePosition(AdapterView<?> viewAdapter) {
		int count = viewAdapter.getCount();
		for (int i = 0; i < count; i++) {
			CategoryInfo citem = (CategoryInfo) viewAdapter
					.getItemAtPosition(i);
			if (citem.getCategory().equals(categoryState.getGenreCategory())) {
				return i;
			}
		}
		return 0;
	}

	@Override
	public void onNothingSelected(AdapterView<?> va) {

	}

}
