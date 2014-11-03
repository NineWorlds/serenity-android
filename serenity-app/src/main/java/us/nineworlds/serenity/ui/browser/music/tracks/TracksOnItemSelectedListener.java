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

package us.nineworlds.serenity.ui.browser.music.tracks;

import javax.inject.Inject;

import us.nineworlds.serenity.R;
import us.nineworlds.serenity.core.imageloader.SerenityImageLoader;
import us.nineworlds.serenity.core.model.impl.AudioTrackContentInfo;
import us.nineworlds.serenity.injection.BaseInjector;
import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * @author dcarver
 *
 */
public class TracksOnItemSelectedListener extends BaseInjector implements
OnItemSelectedListener {

	@Inject
	protected SerenityImageLoader serenityImageLoader;

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		Activity context = (Activity) view.getContext();
		view.setSelected(true);
		AudioTrackContentInfo track = (AudioTrackContentInfo) parent
				.getAdapter().getItem(position);
		ImageView album = (ImageView) context
				.findViewById(R.id.musicAlbumImage);
		ImageLoader imageLoader = serenityImageLoader.getImageLoader();
		if (track.getParentPosterURL() != null) {
			serenityImageLoader.displayImage(track.getParentPosterURL(), album);
		} else {
			album.setImageResource(R.drawable.default_music);
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {

	}

}
