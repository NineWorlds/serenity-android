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

package com.github.kingargyle.plexappclient.ui.browser.movie;

import java.util.ArrayList;
import java.util.List;

import com.github.kingargyle.plexapp.PlexappFactory;
import com.github.kingargyle.plexapp.model.impl.Directory;
import com.github.kingargyle.plexapp.model.impl.MediaContainer;
import com.github.kingargyle.plexappclient.R;
import com.github.kingargyle.plexappclient.SerenityApplication;
import com.github.kingargyle.plexappclient.core.model.CategoryInfo;
import com.github.kingargyle.plexappclient.core.model.SecondaryCategoryInfo;

import android.app.Activity;
import android.util.Log;
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
public class CategorySpinnerOnItemSelectedListener implements OnItemSelectedListener{
	
	private String selected;
	private String key;	
	private boolean firstSelection = true;
	private ArrayList<SecondaryCategoryInfo> secondaryCategories;

	
	
	public CategorySpinnerOnItemSelectedListener(String defaultSelection, String key) {
		selected = defaultSelection;
		this.key = key;
	}

	public void onItemSelected(AdapterView<?> viewAdapter, View view, int position, long id) {
		
		// This is so we skip it during the onCreate event from the Activity;
		if (firstSelection) {
			firstSelection = false;
			return;
		}
		
		CategoryInfo item = (CategoryInfo) viewAdapter.getItemAtPosition(position);
		if (selected.equalsIgnoreCase(item.getCategory())) {
			return;
		}

		selected = item.getCategory();		
		Activity c = (Activity)view.getContext();
		Spinner secondarySpinner = (Spinner) c.findViewById(R.id.movieCategoryFilter2);
		
		if (item.getLevel() == 0) {
			secondarySpinner.setVisibility(View.INVISIBLE);
			View bgLayout = c.findViewById(R.id.movieBrowserBackgroundLayout);
			Gallery posterGallery = (Gallery) c.findViewById(R.id.moviePosterGallery);
			posterGallery.setAdapter(new MoviePosterImageGalleryAdapter(c, key, item.getCategory()));
			posterGallery.setOnItemSelectedListener(new MoviePosterOnItemSelectedListener(bgLayout, c));
			posterGallery.setOnItemClickListener(new MoviePosterOnItemClickListener());			
		} else {
			populateSecondaryCategories(item.getCategory());
			
			if (secondaryCategories.isEmpty()) {
				Toast.makeText(c, "No Entries available for category: " + item.getCategoryDetail(), Toast.LENGTH_LONG).show();
				return;
			}
			secondarySpinner.setVisibility(View.VISIBLE);

			ArrayAdapter<SecondaryCategoryInfo> spinnerSecArrayAdapter = new ArrayAdapter<SecondaryCategoryInfo>(c, R.layout.serenity_spinner_textview, secondaryCategories);
			spinnerSecArrayAdapter.setDropDownViewResource(R.layout.serenity_spinner_textview_dropdown);
			secondarySpinner.setAdapter(spinnerSecArrayAdapter);
			secondarySpinner.setOnItemSelectedListener(new SecondaryCategorySpinnerOnItemSelectedListener(item.getCategory(), key));
		}
		
	}
	
	protected void populateSecondaryCategories(String categoryKey) {
		try {
			PlexappFactory factory = SerenityApplication.getPlexFactory();
			MediaContainer mediaContainer = factory.retrieveSections(key, categoryKey);
			List<Directory> dirs = mediaContainer.getDirectories();
			secondaryCategories = new ArrayList<SecondaryCategoryInfo>();
			for (Directory dir : dirs) {
				SecondaryCategoryInfo category = new SecondaryCategoryInfo();
				category.setCategory(dir.getKey());
				category.setCategoryDetail(dir.getTitle());
				category.setParentCategory(categoryKey);
				secondaryCategories.add(category);
			}
		} catch (Exception e) {
			Log.e("CategorySpinnerOnItemSelected", e.getMessage(), e);
		}
	}


	public void onNothingSelected(AdapterView<?> va) {
		
	}


}
