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

package us.nineworlds.serenity.volley;

import java.util.List;

import javax.inject.Inject;

import us.nineworlds.plex.rest.model.impl.MediaContainer;
import us.nineworlds.serenity.R;
import us.nineworlds.serenity.core.model.CategoryInfo;
import us.nineworlds.serenity.core.model.impl.TVCategoryMediaContainer;
import us.nineworlds.serenity.injection.BaseInjector;
import us.nineworlds.serenity.ui.browser.tv.TVCategorySpinnerOnItemSelectedListener;
import us.nineworlds.serenity.ui.browser.tv.TVCategoryState;
import android.app.Activity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.android.volley.Response;

public class TVCategoryResponseListener extends BaseInjector implements
		Response.Listener<MediaContainer> {

	@Inject
	TVCategoryState categoryState;

	private List<CategoryInfo> categories;
	private final Activity context;
	private final String key;

	public TVCategoryResponseListener(Activity context, String key) {
		super();
		this.context = context;
		this.key = key;
	}

	@Override
	public void onResponse(MediaContainer mediaContainer) {
		TVCategoryMediaContainer categoryMediaContainer = new TVCategoryMediaContainer(
				mediaContainer);
		categories = categoryMediaContainer.createCategories();
		setupShows();
	}

	protected void setupShows() {
		ArrayAdapter<CategoryInfo> spinnerArrayAdapter = new ArrayAdapter<CategoryInfo>(
				context, R.layout.serenity_spinner_textview, categories);

		spinnerArrayAdapter
				.setDropDownViewResource(R.layout.serenity_spinner_textview_dropdown);

		Spinner categorySpinner = (Spinner) context
				.findViewById(R.id.categoryFilter);
		categorySpinner.setVisibility(View.VISIBLE);
		categorySpinner.setAdapter(spinnerArrayAdapter);

		if (categoryState.getCategory() == null) {
			categorySpinner
					.setOnItemSelectedListener(new TVCategorySpinnerOnItemSelectedListener(
							"all", key));
		} else {
			categorySpinner
					.setOnItemSelectedListener(new TVCategorySpinnerOnItemSelectedListener(
							categoryState.getCategory(), key, false));

		}
		categorySpinner.requestFocus();
	}

}
