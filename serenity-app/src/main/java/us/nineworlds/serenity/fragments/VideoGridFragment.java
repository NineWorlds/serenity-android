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

import us.nineworlds.plex.rest.PlexappFactory;
import us.nineworlds.serenity.R;
import us.nineworlds.serenity.injection.InjectingFragment;
import us.nineworlds.serenity.ui.browser.movie.MovieBrowserActivity;
import us.nineworlds.serenity.ui.browser.movie.MovieGridPosterOnItemSelectedListener;
import us.nineworlds.serenity.ui.browser.movie.MovieSelectedCategoryState;
import us.nineworlds.serenity.ui.listeners.GridVideoOnItemClickListener;
import us.nineworlds.serenity.ui.listeners.GridVideoOnItemLongClickListener;
import us.nineworlds.serenity.volley.DefaultLoggingVolleyErrorListener;
import us.nineworlds.serenity.volley.MovieCategoryResponseListener;
import us.nineworlds.serenity.volley.VolleyUtils;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jess.ui.TwoWayGridView;

public class VideoGridFragment extends InjectingFragment {

	@Inject
	protected GridVideoOnItemClickListener onItemClickListener;

	@Inject
	protected GridVideoOnItemLongClickListener onItemLongClickListener;

	@Inject
	protected MovieSelectedCategoryState categoryState;

	@Inject
	protected PlexappFactory factory;

	@Inject
	protected VolleyUtils volleyUtils;

	protected MovieGridPosterOnItemSelectedListener onItemSelectedListener;

	private TwoWayGridView gridView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		onItemSelectedListener = new MovieGridPosterOnItemSelectedListener();
		View view = inflater.inflate(R.layout.video_grid_fragment, container);
		setupGrid(view);
		return view;
	}

	protected void setupGrid(View view) {
		gridView = (TwoWayGridView) view.findViewById(R.id.movieGridView);

		gridView.setOnItemClickListener(onItemClickListener);
		gridView.setOnItemSelectedListener(new MovieGridPosterOnItemSelectedListener());
		gridView.setOnItemLongClickListener(onItemLongClickListener);

		MovieBrowserActivity activity = (MovieBrowserActivity) view
				.getContext();
		String key = MovieBrowserActivity.getKey();

		MovieCategoryResponseListener response = new MovieCategoryResponseListener(
				getActivity(), key);
		String url = factory.getSectionsUrl(key);
		volleyUtils.volleyXmlGetRequest(url, response,
				new DefaultLoggingVolleyErrorListener());

		// Handler categoryHandler = new CategoryHandler(getActivity(),
		// categoryState.getCategory(), key);
		// Messenger messenger = new Messenger(categoryHandler);
		// Intent categoriesIntent = new Intent(getActivity(),
		// CategoryRetrievalIntentService.class);
		// categoriesIntent.putExtra("key", key);
		// categoriesIntent.putExtra("MESSENGER", messenger);
		// activity.startService(categoriesIntent);
	}

}
