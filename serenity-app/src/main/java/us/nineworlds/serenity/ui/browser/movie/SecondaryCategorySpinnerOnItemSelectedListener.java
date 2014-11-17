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

import javax.inject.Inject;

import us.nineworlds.serenity.R;
import us.nineworlds.serenity.core.model.CategoryInfo;
import us.nineworlds.serenity.core.model.SecondaryCategoryInfo;
import us.nineworlds.serenity.injection.BaseInjector;
import us.nineworlds.serenity.ui.activity.SerenityMultiViewVideoActivity;
import us.nineworlds.serenity.ui.adapters.AbstractPosterImageGalleryAdapter;
import us.nineworlds.serenity.ui.listeners.GalleryVideoOnItemClickListener;
import us.nineworlds.serenity.ui.listeners.GalleryVideoOnItemLongClickListener;
import us.nineworlds.serenity.ui.listeners.GridVideoOnItemClickListener;
import us.nineworlds.serenity.ui.listeners.GridVideoOnItemLongClickListener;
import us.nineworlds.serenity.widgets.SerenityGallery;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;

import com.jess.ui.TwoWayGridView;

/**
 * Populate the movie posters based on the information from the Secondary
 * categories.
 *
 * @author dcarver
 *
 */
public class SecondaryCategorySpinnerOnItemSelectedListener extends
		BaseInjector implements OnItemSelectedListener {

	@Inject
	protected SharedPreferences prefs;

	@Inject
	protected GalleryVideoOnItemClickListener galleryOnItemClickListener;

	@Inject
	protected GridVideoOnItemClickListener gridVideoOnItemClickListener;

	@Inject
	protected GalleryVideoOnItemLongClickListener galleryOnItemLongClickListener;

	@Inject
	protected GridVideoOnItemLongClickListener gridVideoOnItemLongClickListener;

	@Inject
	protected MovieSelectedCategoryState categoryState;

	protected View bgLayout;

	protected SerenityGallery posterGallery;

	private SerenityMultiViewVideoActivity multiViewVideoActivity;

	protected TwoWayGridView gridView;

	private String selected;
	private final String key;
	private boolean firstTimesw = true;

	public SecondaryCategorySpinnerOnItemSelectedListener(
			String defaultSelection, String key) {
		super();
		selected = defaultSelection;
		this.key = key;
	}

	@Override
	public void onItemSelected(AdapterView<?> viewAdapter, View view,
			int position, long id) {

		setMultiViewVideoActivity((SerenityMultiViewVideoActivity) view
				.getContext());

		findViews();

		SecondaryCategoryInfo item = (SecondaryCategoryInfo) viewAdapter
				.getItemAtPosition(position);

		if (isFirstTimesw()) {
			if (categoryState.getGenreCategory() != null) {
				int savedInstancePosition = getSavedInstancePosition(viewAdapter);
				item = (SecondaryCategoryInfo) viewAdapter
						.getItemAtPosition(savedInstancePosition);
				viewAdapter.setSelection(savedInstancePosition);
			}
			setFirstTimesw(false);
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

	/**
	 * @param item
	 * @return
	 */
	protected AbstractPosterImageGalleryAdapter getPosterImageAdapter(
			SecondaryCategoryInfo item) {
		AbstractPosterImageGalleryAdapter adapter = new MoviePosterImageAdapter(
				getMultiViewVideoActivity(), key, item.getParentCategory()
				+ "/" + item.getCategory());
		return adapter;
	}

	protected void findViews() {
		bgLayout = getMultiViewVideoActivity().findViewById(
				R.id.movieBrowserBackgroundLayout);

		gridView = (TwoWayGridView) getMultiViewVideoActivity().findViewById(
				R.id.movieGridView);
		posterGallery = (SerenityGallery) getMultiViewVideoActivity()
				.findViewById(R.id.moviePosterGallery);
	}

	protected void refreshGallery(AbstractPosterImageGalleryAdapter adapter) {
		boolean scrollingAnimation = prefs.getBoolean(
				"animation_gallery_scrolling", true);
		posterGallery.setAdapter(adapter);
		posterGallery
				.setOnItemSelectedListener(new MoviePosterOnItemSelectedListener(
						getMultiViewVideoActivity()));

		posterGallery.setOnItemClickListener(galleryOnItemClickListener);
		posterGallery
				.setOnItemLongClickListener(new GalleryVideoOnItemLongClickListener());
		if (scrollingAnimation) {
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
	}

	protected void refreshGridView(AbstractPosterImageGalleryAdapter adapter) {
		gridView.setAdapter(adapter);
		gridView.setOnItemClickListener(gridVideoOnItemClickListener);
		gridView.setOnItemSelectedListener(new MovieGridPosterOnItemSelectedListener(
				bgLayout));
		gridView.setOnItemLongClickListener(gridVideoOnItemLongClickListener);
	}

	protected int getSavedInstancePosition(AdapterView<?> viewAdapter) {
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

	protected boolean isFirstTimesw() {
		return firstTimesw;
	}

	protected void setFirstTimesw(boolean firstTimesw) {
		this.firstTimesw = firstTimesw;
	}

	protected SerenityMultiViewVideoActivity getMultiViewVideoActivity() {
		return multiViewVideoActivity;
	}

	protected void setMultiViewVideoActivity(
			SerenityMultiViewVideoActivity multiViewVideoActivity) {
		this.multiViewVideoActivity = multiViewVideoActivity;
	}

}
