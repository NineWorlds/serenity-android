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

package us.nineworlds.serenity.ui.activity;

import com.jess.ui.TwoWayGridView;

import us.nineworlds.serenity.R;
import us.nineworlds.serenity.core.model.VideoContentInfo;
import us.nineworlds.serenity.core.services.UpdateProgressRequest;
import us.nineworlds.serenity.ui.adapters.AbstractPosterImageGalleryAdapter;
import us.nineworlds.serenity.widgets.SerenityGallery;
import android.app.Activity;
import android.content.Intent;
import android.view.KeyEvent;
import android.widget.ImageView;

/**
 * @author dcarver
 * 
 */
public abstract class SerenityActivity extends Activity {
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
			SerenityGallery gallery = (SerenityGallery) findViewById(R.id.moviePosterGallery);
			if (gallery == null) {
				return super.onKeyDown(keyCode, event);				
			}
			
			AbstractPosterImageGalleryAdapter adapter = (AbstractPosterImageGalleryAdapter) gallery.getAdapter();
			int itemsCount =  adapter.getCount();
			
			if (keyCode == KeyEvent.KEYCODE_C) {
				ImageView view = (ImageView) gallery.getSelectedView();
				view.performLongClick();
				return true;
			}
			if (isKeyCodeSkipBack(keyCode)) {
				int selectedItem = gallery.getSelectedItemPosition();
				int newPosition = selectedItem - 10;
				if (newPosition < 0) {
					newPosition = 0;
				}
				gallery.setSelection(newPosition);
				return true;
			}
			if (isKeyCodeSkipForward(keyCode)) {
				int selectedItem = gallery.getSelectedItemPosition();
				int newPosition = selectedItem  + 10;
				if (newPosition > itemsCount) {
					newPosition = itemsCount - 1;
				}
				gallery.setSelection(newPosition);
				return true;
			}
			
			return super.onKeyDown(keyCode, event);
		}

	/**
	 * @param keyCode
	 * @return
	 */
	protected boolean isKeyCodeSkipForward(int keyCode) {
		return keyCode == KeyEvent.KEYCODE_F ||
			   keyCode == KeyEvent.KEYCODE_PAGE_UP ||
			   keyCode == KeyEvent.KEYCODE_CHANNEL_UP ||
			   keyCode == KeyEvent.KEYCODE_MEDIA_NEXT ||
			   keyCode == KeyEvent.KEYCODE_MEDIA_FAST_FORWARD;
	}

	/**
	 * @param keyCode
	 * @return
	 */
	protected boolean isKeyCodeSkipBack(int keyCode) {
		return keyCode == KeyEvent.KEYCODE_R ||
			   keyCode == KeyEvent.KEYCODE_PAGE_DOWN ||
			   keyCode == KeyEvent.KEYCODE_CHANNEL_DOWN ||
			   keyCode == KeyEvent.KEYCODE_MEDIA_PREVIOUS ||
			   keyCode == KeyEvent.KEYCODE_MEDIA_REWIND;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			if (data != null && data.getAction().equals("com.mxtech.intent.result.VIEW")) {
				SerenityGallery gallery = (SerenityGallery) findViewById(R.id.moviePosterGallery);
				if (gallery != null) {
					VideoContentInfo video = (VideoContentInfo) gallery.getSelectedItem();
					if (video != null) {
						updateProgress(data, video);
					}
				} else {
					TwoWayGridView gridView = (TwoWayGridView) findViewById(R.id.movieGridView);
					if (gridView != null) {
						VideoContentInfo video = (VideoContentInfo) gridView.getSelectedItem();
						if (video != null) {
							updateProgress(data, video);
						}
					}
				}
			}
		}
	}

	/**
	 * @param data
	 * @param video
	 */
	protected void updateProgress(Intent data, VideoContentInfo video) {
		long position = data.getIntExtra("position", 0);
		UpdateProgressRequest request = new UpdateProgressRequest(position, video.id());
		request.execute();
	}

}
