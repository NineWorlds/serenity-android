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

import com.jess.ui.TwoWayGridView;

import us.nineworlds.serenity.core.model.CategoryInfo;
import us.nineworlds.serenity.core.model.SecondaryCategoryInfo;
import us.nineworlds.serenity.ui.activity.SerenityMultiViewVideoActivity;

import us.nineworlds.serenity.R;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Gallery;

/**
 * Populate the tv show banners based on the information from the Secondary
 * categories.
 * 
 * @author dcarver
 * 
 */
public class SecondaryCategorySpinnerOnItemSelectedListener implements
		OnItemSelectedListener {

	private String selected;
	private String key;
	private boolean firstTimesw = true;

	public SecondaryCategorySpinnerOnItemSelectedListener(
			String defaultSelection, String key) {
		selected = defaultSelection;
		this.key = key;
	}

	@Override
	public void onItemSelected(AdapterView<?> viewAdapter, View view,
			int position, long id) {
		
		SerenityMultiViewVideoActivity context = (SerenityMultiViewVideoActivity) view.getContext();

		SecondaryCategoryInfo item = (SecondaryCategoryInfo) viewAdapter
				.getItemAtPosition(position);
		
		if (firstTimesw) {
			if (context.retrieveSavedSelectedGenreCategory() != null) {
				int savedInstancePosition = getSavedInstancePosition(viewAdapter, context.retrieveSavedSelectedGenreCategory());
				item = (SecondaryCategoryInfo) viewAdapter.getItemAtPosition(savedInstancePosition);
				viewAdapter.setSelection(savedInstancePosition);
			}
			firstTimesw = false;
		}
		
		
		if (selected.equalsIgnoreCase(item.getCategory())) {
			return;
		}

		selected = item.getCategory();
		context.setSavedSelectedGenreCategory(item.getCategory());
		
		SerenityMultiViewVideoActivity c = (SerenityMultiViewVideoActivity) view.getContext();

		View bgLayout = c.findViewById(R.id.tvshowBrowserLayout);
		if (c.isGridViewActive()) {
			TwoWayGridView gridView = (TwoWayGridView) c.findViewById(R.id.tvShowGridView);
			gridView.setAdapter(new TVShowPosterImageGalleryAdapter(c, key, item.getParentCategory() + "/"
					+ item.getCategory()));
			gridView.setOnItemSelectedListener(new TVShowGridOnItemSelectedListener(bgLayout, c));
			gridView.setOnItemClickListener(new TVShowGridOnItemClickListener(c));
		} else {
			Gallery posterGallery = (Gallery) c
					.findViewById(R.id.tvShowBannerGallery);

			if (c.isPosterLayoutActive()) {
				posterGallery.setAdapter(new TVShowBannerImageGalleryAdapter(c,
						key, item.getParentCategory() + "/"
								+ item.getCategory()));
			} else {
				posterGallery.setAdapter(new TVShowPosterImageGalleryAdapter(c,
						key, item.getParentCategory() + "/"
								+ item.getCategory()));
			}

			posterGallery
					.setOnItemSelectedListener(new TVShowGalleryOnItemSelectedListener(
							bgLayout, c));
			posterGallery
					.setOnItemClickListener(new TVShowBrowserGalleryOnItemClickListener(
							c));
			posterGallery
					.setOnItemLongClickListener(new ShowOnItemLongClickListener());
			posterGallery.setCallbackDuringFling(false);
		}
	}
	
	private int getSavedInstancePosition(AdapterView<?> viewAdapter, String category) {
		int count = viewAdapter.getCount();
		for (int i = 0; i < count; i++) {
			CategoryInfo citem = (CategoryInfo) viewAdapter
					.getItemAtPosition(i);
			if (citem.getCategory().equals(category)) {
				return i;
			}
		}
		return 0;
	}
	

	@Override
	public void onNothingSelected(AdapterView<?> va) {

	}

}
