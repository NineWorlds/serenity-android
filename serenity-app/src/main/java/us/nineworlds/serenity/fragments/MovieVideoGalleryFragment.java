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

package us.nineworlds.serenity.fragments;

import javax.inject.Inject;

import us.nineworlds.serenity.R;
import us.nineworlds.serenity.core.services.CategoryRetrievalIntentService;
import us.nineworlds.serenity.injection.InjectingFragment;
import us.nineworlds.serenity.ui.activity.CategoryHandler;
import us.nineworlds.serenity.ui.browser.movie.MovieBrowserActivity;
import us.nineworlds.serenity.ui.browser.movie.MoviePosterOnItemSelectedListener;
import us.nineworlds.serenity.ui.browser.movie.MovieSelectedCategoryState;
import us.nineworlds.serenity.ui.browser.tv.episodes.EpisodeBrowserActivity;
import us.nineworlds.serenity.ui.listeners.GalleryVideoOnItemClickListener;
import us.nineworlds.serenity.ui.listeners.GalleryVideoOnItemLongClickListener;
import us.nineworlds.serenity.widgets.SerenityGallery;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Messenger;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MovieVideoGalleryFragment extends InjectingFragment {

	@Inject
	SharedPreferences preferences;

	@Inject
	GalleryVideoOnItemClickListener onItemClickListener;

	@Inject
	GalleryVideoOnItemLongClickListener onItemLongClickListener;

	@Inject
	protected MovieSelectedCategoryState categoryState;

	MoviePosterOnItemSelectedListener onItemSelectedListener;

	private SerenityGallery videoGallery;

	public MovieVideoGalleryFragment() {
		super();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		onItemSelectedListener = new MoviePosterOnItemSelectedListener();
		return inflater.inflate(R.layout.video_gallery_fragment, container);
	}

	@Override
	public void onStart() {
		super.onStart();

		boolean scrollingAnimation = preferences.getBoolean(
				"animation_gallery_scrolling", true);

		videoGallery = (SerenityGallery) getActivity().findViewById(
				R.id.moviePosterGallery);

		videoGallery.setOnItemSelectedListener(onItemSelectedListener);

		videoGallery.setOnItemClickListener(onItemClickListener);

		videoGallery.setOnItemLongClickListener(onItemLongClickListener);
		if (scrollingAnimation) {
			videoGallery.setAnimationDuration(220);
		} else {
			videoGallery.setAnimationDuration(1);
		}
		videoGallery.setSpacing(25);
		videoGallery.setAnimationCacheEnabled(true);
		videoGallery.setCallbackDuringFling(false);
		videoGallery.setHorizontalFadingEdgeEnabled(true);
		videoGallery.setFocusableInTouchMode(false);
		videoGallery.setDrawingCacheEnabled(true);
		videoGallery.setUnselectedAlpha(0.75f);

		String key = null;
		if (getActivity() instanceof MovieBrowserActivity) {
			key = ((MovieBrowserActivity) getActivity()).getKey();
		} else {
			key = ((EpisodeBrowserActivity) getActivity()).getKey();
		}

		Activity activity = getActivity();

		Handler categoryHandler = new CategoryHandler(getActivity(),
				categoryState.getCategory(), key);
		Messenger messenger = new Messenger(categoryHandler);
		Intent categoriesIntent = new Intent(getActivity(),
				CategoryRetrievalIntentService.class);
		categoriesIntent.putExtra("key", key);
		categoriesIntent.putExtra("MESSENGER", messenger);
		activity.startService(categoriesIntent);

	}

	@Override
	public void onResume() {
		super.onResume();
	}

}
