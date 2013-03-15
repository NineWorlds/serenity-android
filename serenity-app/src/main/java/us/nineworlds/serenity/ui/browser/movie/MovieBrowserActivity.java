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

import java.util.ArrayList;

import us.nineworlds.serenity.core.model.CategoryInfo;
import us.nineworlds.serenity.core.services.MovieCategoryRetrievalIntentService;
import us.nineworlds.serenity.ui.activity.SerenityActivity;

import us.nineworlds.serenity.R;
import com.google.analytics.tracking.android.EasyTracker;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class MovieBrowserActivity extends SerenityActivity {
	
	private static String key;
	private static Spinner categorySpinner;
	private boolean restarted_state = false;
	private Handler categoryHandler;
	
	private static Activity context;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		key = getIntent().getExtras().getString("key");
		setContentView(R.layout.activity_movie_browser);		
	}
	
	
	@Override
	protected void onStart() {
		super.onStart();
		EasyTracker.getInstance().activityStart(this);
		if (restarted_state == false) {
			categoryHandler = new CategoryHandler();
			Messenger messenger = new Messenger(categoryHandler);
			Intent categoriesIntent = new Intent(this, MovieCategoryRetrievalIntentService.class);
			categoriesIntent.putExtra("key", key);
			categoriesIntent.putExtra("MESSENGER", messenger);
			startService(categoriesIntent);
			context = this;
		}
		restarted_state = false;
	}


	/* (non-Javadoc)
	 * @see android.app.Activity#onStop()
	 */
	@Override
	protected void onStop() {
		super.onStop();
		EasyTracker.getInstance().activityStop(this);
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_movie_browser, menu);
		return true;
	}
		
	/* (non-Javadoc)
	 * @see android.app.Activity#onRestart()
	 */
	@Override
	protected void onRestart() {
		super.onRestart();
		restarted_state = true;
	}
	
	private static class CategoryHandler extends Handler {
		
		private ArrayList<CategoryInfo> categories;
		
		/* (non-Javadoc)
		 * @see android.os.Handler#handleMessage(android.os.Message)
		 */
		@Override
		public void handleMessage(Message msg) {
			if (msg.obj != null) {
				categories = (ArrayList<CategoryInfo>)msg.obj;
				setupMovieBrowser();
			}
		}
		
		/**
		 * Setup the Gallery and Category spinners 
		 */
		protected void setupMovieBrowser() {
			ArrayAdapter<CategoryInfo> spinnerArrayAdapter = new ArrayAdapter<CategoryInfo>(context, R.layout.serenity_spinner_textview, categories);
			spinnerArrayAdapter.setDropDownViewResource(R.layout.serenity_spinner_textview_dropdown);
			
			categorySpinner =(Spinner) context.findViewById(R.id.movieCategoryFilter);
			categorySpinner.setVisibility(View.VISIBLE);
			categorySpinner.setAdapter(spinnerArrayAdapter);
			categorySpinner.setOnItemSelectedListener(new CategorySpinnerOnItemSelectedListener("all", key));
			categorySpinner.requestFocus();
		}
		
	}	
}
