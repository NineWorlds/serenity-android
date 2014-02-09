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

import com.jess.ui.TwoWayGridView;

import us.nineworlds.serenity.core.model.CategoryInfo;
import us.nineworlds.serenity.core.model.SecondaryCategoryInfo;
import us.nineworlds.serenity.ui.activity.SerenityMultiViewVideoActivity;
import us.nineworlds.serenity.ui.adapters.AbstractPosterImageGalleryAdapter;
import us.nineworlds.serenity.ui.listeners.GridVideoOnItemClickListener;
import us.nineworlds.serenity.ui.listeners.GridVideoOnItemLongClickListener;
import us.nineworlds.serenity.ui.listeners.GalleryVideoOnItemClickListener;
import us.nineworlds.serenity.ui.listeners.GalleryVideoOnItemLongClickListener;
import us.nineworlds.serenity.widgets.SerenityGallery;

import us.nineworlds.serenity.R;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;

/**
 * Populate the movie posters based on the information from the Secondary
 * categories.
 * 
 * @author dcarver
 * 
 */
public class SecondaryCategorySpinnerOnItemSelectedListener implements
		OnItemSelectedListener {

	private String selected;
	private String key;
	private SharedPreferences prefs;
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
		if (prefs == null) {
			prefs = PreferenceManager.getDefaultSharedPreferences(c);
		}

		View bgLayout = c.findViewById(R.id.movieBrowserBackgroundLayout);
		SerenityGallery posterGallery = (SerenityGallery) c
				.findViewById(R.id.moviePosterGallery);
		AbstractPosterImageGalleryAdapter adapter = new MoviePosterImageAdapter(c, key,
				item.getParentCategory() + "/" + item.getCategory());

		if (!c.isGridViewActive()) {
			boolean scrollingAnimation = prefs.getBoolean("animation_gallery_scrolling", true);
			posterGallery.setAdapter(adapter);
			posterGallery
					.setOnItemSelectedListener(new MoviePosterOnItemSelectedListener(c));
			posterGallery
					.setOnItemClickListener(new GalleryVideoOnItemClickListener());
			posterGallery
					.setOnItemLongClickListener(new GalleryVideoOnItemLongClickListener());
			if (scrollingAnimation){
				posterGallery.setAnimationDuration(220);
			} else {
				posterGallery.setAnimationDuration(1);
			}
			posterGallery.setSpacing(25);
			posterGallery.setAnimationCacheEnabled(true);
			posterGallery.setCallbackDuringFling(false);
			posterGallery.setHorizontalFadingEdgeEnabled(true);
			posterGallery.setFocusableInTouchMode(false);
			posterGallery.setDrawingCacheEnabled(true);
			posterGallery.setUnselectedAlpha(0.75f);

		} else {
			TwoWayGridView gridView = (TwoWayGridView) c
					.findViewById(R.id.movieGridView);
			gridView.setAdapter(adapter);
			gridView.setOnItemClickListener(new GridVideoOnItemClickListener());
			gridView.setOnItemSelectedListener(new MovieGridPosterOnItemSelectedListener(
					bgLayout, c));
			gridView.setOnItemLongClickListener(new GridVideoOnItemLongClickListener());
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
