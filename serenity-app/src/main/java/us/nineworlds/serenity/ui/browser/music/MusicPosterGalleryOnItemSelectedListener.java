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

package us.nineworlds.serenity.ui.browser.music;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;

import us.nineworlds.serenity.SerenityApplication;
import us.nineworlds.serenity.core.imageloader.SerenityBackgroundLoaderListener;
import us.nineworlds.serenity.core.model.impl.MusicArtistContentInfo;
import us.nineworlds.serenity.ui.views.SerenityMusicImageView;

import us.nineworlds.serenity.R;

import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TextView;

/**
 * Display selected TV Show Information.
 * 
 * @author dcarver
 * 
 */
public class MusicPosterGalleryOnItemSelectedListener implements
		OnItemSelectedListener {

	private static final String SEPERATOR = "/";
	private View bgLayout;
	private Activity context;
	private ImageLoader imageLoader;
	private View previous;
	private ImageSize bgImageSize = new ImageSize(1280, 720);

	/**
	 * 
	 */
	public MusicPosterGalleryOnItemSelectedListener(Activity activity) {
		bgLayout = activity.findViewById(R.id.musicArtistBrowserLayout);
		context = activity;

		imageLoader = SerenityApplication.getImageLoader();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.widget.AdapterView.OnItemSelectedListener#onItemSelected(android
	 * .widget.AdapterView, android.view.View, int, long)
	 */
	@Override
	public void onItemSelected(AdapterView<?> av, View v, int position, long id) {

		if (previous != null) {
			previous.setPadding(0, 0, 0, 0);
		}

		previous = v;

		v.setPadding(5, 5, 5, 5);

		createArtistDetail((SerenityMusicImageView) v);
		changeBackgroundImage(v);

	}

	private void createArtistDetail(SerenityMusicImageView v) {

		TextView summary = (TextView) context
				.findViewById(R.id.musicArtistSummary);
		String plotSummary = v.getPosterInfo().getSummary();
		if (plotSummary == null) {
			summary.setText("");
		} else {
			summary.setText(plotSummary);
		}

		TextView title = (TextView) context.findViewById(R.id.musicTitle);
		title.setText(v.getPosterInfo().getTitle());
	}
	

	/**
	 * Change the background image of the activity.
	 * 
	 * Should be a background activity
	 * 
	 * @param v
	 */
	private void changeBackgroundImage(View v) {

		SerenityMusicImageView mpiv = (SerenityMusicImageView) v;
		MusicArtistContentInfo mi = mpiv.getPosterInfo();

		imageLoader.loadImage(mi.getBackgroundURL(), bgImageSize,
				new SerenityBackgroundLoaderListener(bgLayout,
						R.drawable.music));
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {

	}

}
