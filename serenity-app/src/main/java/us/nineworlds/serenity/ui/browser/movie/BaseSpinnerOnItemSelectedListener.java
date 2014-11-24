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
import us.nineworlds.serenity.injection.BaseInjector;
import us.nineworlds.serenity.ui.activity.SerenityMultiViewVideoActivity;
import us.nineworlds.serenity.ui.adapters.AbstractPosterImageGalleryAdapter;
import us.nineworlds.serenity.widgets.SerenityGallery;
import android.content.SharedPreferences;
import android.widget.AdapterView;

import com.jess.ui.TwoWayGridView;

public abstract class BaseSpinnerOnItemSelectedListener extends BaseInjector {

	@Inject
	protected MovieSelectedCategoryState categoryState;

	@Inject
	protected SharedPreferences prefs;

	protected SerenityGallery posterGallery;
	protected TwoWayGridView gridView;

	protected SerenityMultiViewVideoActivity multiViewVideoActivity;

	protected String selected;
	protected static String key;
	protected boolean firstSelection = true;

	public BaseSpinnerOnItemSelectedListener(String defaultSelection, String key) {
		selected = defaultSelection;
		this.key = key;
	}

	protected void findViews() {
		gridView = (TwoWayGridView) getMultiViewVideoActivity().findViewById(
				R.id.movieGridView);
		posterGallery = (SerenityGallery) getMultiViewVideoActivity()
				.findViewById(R.id.moviePosterGallery);
	}

	protected int getSavedInstancePosition(AdapterView<?> viewAdapter) {
		int count = viewAdapter.getCount();
		for (int i = 0; i < count; i++) {
			CategoryInfo citem = (CategoryInfo) viewAdapter
					.getItemAtPosition(i);
			if (citem.getCategory().equals(getSavedCategory())) {
				return i;
			}
		}
		return 0;
	}

	protected abstract String getSavedCategory();

	public void onNothingSelected(AdapterView<?> va) {

	}

	protected void refreshGallery(AbstractPosterImageGalleryAdapter adapter) {
		posterGallery.setAdapter(adapter);
	}

	protected void refreshGridView(AbstractPosterImageGalleryAdapter adapter) {
		gridView.setAdapter(adapter);
	}

	protected SerenityMultiViewVideoActivity getMultiViewVideoActivity() {
		return multiViewVideoActivity;
	}

	protected void setMultiViewVideoActivity(
			SerenityMultiViewVideoActivity multiViewVideoActivity) {
		this.multiViewVideoActivity = multiViewVideoActivity;
	}

	public boolean isFirstSelection() {
		return firstSelection;
	}

	public void setFirstSelection(boolean firstSelection) {
		this.firstSelection = firstSelection;
	}

}
