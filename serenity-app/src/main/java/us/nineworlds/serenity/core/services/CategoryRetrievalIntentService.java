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

package us.nineworlds.serenity.core.services;

import java.util.ArrayList;
import java.util.List;

import us.nineworlds.plex.rest.model.impl.Directory;
import us.nineworlds.plex.rest.model.impl.MediaContainer;
import us.nineworlds.serenity.core.model.CategoryInfo;

import android.content.Intent;
import android.util.Log;

/**
 * Retrieves the available categories for filtering and returns them to the
 * calling service.
 * 
 * @author dcarver
 * 
 */
public class CategoryRetrievalIntentService extends
		AbstractCategoryService {

	private String key;
	private boolean filterAlbums;

	public CategoryRetrievalIntentService() {
		super("CategoryRetrievalIntentService");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.IntentService#onHandleIntent(android.content.Intent)
	 */
	@Override
	protected void onHandleIntent(Intent intent) {
		key = intent.getExtras().getString("key");
		filterAlbums = intent.getExtras().getBoolean("filterAlbums", false);
		populateCategories();
		sendMessageResults(intent);
	}

	@Override
	protected void populateCategories() {
		try {
			MediaContainer mediaContainer = factory.retrieveSections(key);
			List<Directory> dirs = mediaContainer.getDirectories();
			categories = new ArrayList<CategoryInfo>();
			for (Directory dir : dirs) {
				if (resultsNotFiltered(dir)) {
					CategoryInfo category = new CategoryInfo();
					category.setCategory(dir.getKey());
					category.setCategoryDetail(dir.getTitle());
					if (dir.getSecondary() > 0) {
						category.setLevel(dir.getSecondary());
					}
					categories.add(category);
				}
			}
		} catch (Exception e) {
			Log.e(getClass().getName(), e.getMessage(), e);
		}
	}

	/**
	 * @param dir
	 * @return
	 */
	protected boolean resultsNotFiltered(Directory dir) {
		if (filterAlbums) {
			if (dir.getKey().equals("year") ||
				dir.getKey().equals("decade"))  {
				return false;
			}
		}
		return !"folder".equals(dir.getKey())
				&& !"Search...".equals(dir.getTitle()) &&
				!"Search Artists...".equals(dir.getTitle()) &&
				!"Search Albums...".equals(dir.getTitle()) &&
				!"Search Tracks...".equals(dir.getTitle());
	}

}
