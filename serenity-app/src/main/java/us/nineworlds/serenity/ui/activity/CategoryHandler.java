/**
 * The MIT License (MIT)
 * Copyright (c) 2013 David Carver
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

package us.nineworlds.serenity.ui.activity;

import java.util.List;

import us.nineworlds.serenity.R;
import us.nineworlds.serenity.core.model.CategoryInfo;
import us.nineworlds.serenity.ui.browser.movie.CategorySpinnerOnItemSelectedListener;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class CategoryHandler extends Handler {

	private List<CategoryInfo> categories;
	private Activity _context;
	private String _savedCategory;
	private String _key;
	
	/**
	 * 
	 */
	public CategoryHandler(Activity context, String savedCategory, String key) {
		_context = context;
		_savedCategory = savedCategory;
		_key = key;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.Handler#handleMessage(android.os.Message)
	 */
	@Override
	public void handleMessage(Message msg) {
		if (msg.obj != null) {
			categories = (List<CategoryInfo>) msg.obj;
			setupMovieBrowser();
		}
	}

	/**
	 * Setup the Gallery and Category spinners
	 */
	protected void setupMovieBrowser() {
		ArrayAdapter<CategoryInfo> spinnerArrayAdapter = new ArrayAdapter<CategoryInfo>(
				_context, R.layout.serenity_spinner_textview, categories);
		spinnerArrayAdapter
				.setDropDownViewResource(R.layout.serenity_spinner_textview_dropdown);

		Spinner categorySpinner = (Spinner) _context
				.findViewById(R.id.categoryFilter);
		categorySpinner.setVisibility(View.VISIBLE);
		categorySpinner.setAdapter(spinnerArrayAdapter);
		if (_savedCategory == null) {
			categorySpinner
					.setOnItemSelectedListener(new CategorySpinnerOnItemSelectedListener(
							"all", _key));
		} else {
			categorySpinner
			.setOnItemSelectedListener(new CategorySpinnerOnItemSelectedListener(
					_savedCategory, _key, false));
		}
		categorySpinner.requestFocus();
	}
}
