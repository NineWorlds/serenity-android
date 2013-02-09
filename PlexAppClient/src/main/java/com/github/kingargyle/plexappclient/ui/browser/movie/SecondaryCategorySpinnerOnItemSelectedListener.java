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


import com.github.kingargyle.plexappclient.R;
import com.github.kingargyle.plexappclient.core.model.SecondaryCategoryInfo;

import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Gallery;

/**
 * @author dcarver
 *
 */
public class SecondaryCategorySpinnerOnItemSelectedListener implements OnItemSelectedListener{
	
	private String selected;
	private String key;	

	
	
	public SecondaryCategorySpinnerOnItemSelectedListener(String defaultSelection, String key) {
		selected = defaultSelection;
		this.key = key;
	}

	public void onItemSelected(AdapterView<?> viewAdapter, View view, int position, long id) {
				
		SecondaryCategoryInfo item = (SecondaryCategoryInfo) viewAdapter.getItemAtPosition(position);
		if (selected.equalsIgnoreCase(item.getCategory())) {
			return;
		}

		selected = item.getCategory();		
		Activity c = (Activity)view.getContext();
		
		View bgLayout = c.findViewById(R.id.movieBrowserBackgroundLayout);
		Gallery posterGallery = (Gallery) c.findViewById(R.id.moviePosterGallery);
		
		posterGallery.setAdapter(new MoviePosterImageGalleryAdapter(c, key, item.getParentCategory() + "/" + item.getCategory()));
		posterGallery.setOnItemSelectedListener(new MoviePosterOnItemSelectedListener(bgLayout, c));
		posterGallery.setOnItemClickListener(new MoviePosterOnItemClickListener());			
		
	}



	public void onNothingSelected(AdapterView<?> va) {
		
	}


}
