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

package com.github.kingargyle.plexappclient.core.services;

import java.util.ArrayList;
import java.util.List;

import com.github.kingargyle.plexapp.model.impl.Directory;
import com.github.kingargyle.plexapp.model.impl.MediaContainer;
import com.github.kingargyle.plexappclient.core.model.SecondaryCategoryInfo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

/**
 * Retrieves the available secondary categories for filtering and returns them to the
 * calling service.
 * 
 * @author dcarver
 *
 */
public class MovieSecondaryCategoryRetrievalIntentService extends
		AbstractPlexRESTIntentService {
	
	private ArrayList<SecondaryCategoryInfo> secondaryCategories;
	private String key;
	
	
	public MovieSecondaryCategoryRetrievalIntentService() {
		super("MovieSecondaryCategoryRetrievalIntentService");
	}

	/* (non-Javadoc)
	 * @see com.github.kingargyle.plexappclient.core.services.AbstractPlexRESTIntentService#sendMessageResults(android.content.Intent)
	 */
	@Override
	public void sendMessageResults(Intent intent) {
		Bundle extras = intent.getExtras();
		if (extras != null) {
			Messenger messenger = (Messenger) extras.get("MESSENGER");
			Message msg = Message.obtain();
			msg.obj = secondaryCategories;
			try {
				messenger.send(msg);
			} catch (RemoteException ex) {
				Log.e(getClass().getName(), "Unable to send message", ex);
			}
		}
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		key = intent.getExtras().getString("key");
		String category = intent.getExtras().getString("category");
		populateSecondaryCategories(category);
		sendMessageResults(intent);
	}
	
	protected void populateSecondaryCategories(String categoryKey) {
		try {
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
			Log.e(getClass().getName(), e.getMessage(), e);
		}
	}
}
