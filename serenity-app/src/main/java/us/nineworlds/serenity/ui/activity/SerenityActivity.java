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

import net.simonvt.menudrawer.MenuDrawer;
import us.nineworlds.serenity.R;
import us.nineworlds.serenity.core.model.VideoContentInfo;
import us.nineworlds.serenity.core.services.UpdateProgressRequest;
import us.nineworlds.serenity.ui.adapters.AbstractPosterImageGalleryAdapter;
import us.nineworlds.serenity.ui.util.ImageUtils;
import us.nineworlds.serenity.widgets.SerenityGallery;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ListView;

import com.jess.ui.TwoWayGridView;

/**
 * @author dcarver
 * 
 */
public abstract class SerenityActivity extends Activity {

	protected ListView menuOptions;
	protected MenuDrawer menuDrawer;

	protected abstract void createSideMenu();

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		SerenityGallery gallery = (SerenityGallery) findViewById(R.id.moviePosterGallery);
		TwoWayGridView gridView = (TwoWayGridView) findViewById(R.id.movieGridView);
		if (gridView == null) {
			gridView = (TwoWayGridView) findViewById(R.id.tvShowGridView);
		}

		if (gallery == null && gridView == null) {
			return super.onKeyDown(keyCode, event);
		}

		AbstractPosterImageGalleryAdapter adapter = null;
		if (gallery != null) {
			adapter = (AbstractPosterImageGalleryAdapter) gallery.getAdapter();
		} else {
			adapter = (AbstractPosterImageGalleryAdapter) gridView.getAdapter();
		}

		if (adapter != null) {
			int itemsCount = adapter.getCount();

			if (contextMenuRequested(keyCode)) {
				View view = null;
				if (gallery != null) {
					view = gallery.getSelectedView();
				} else {
					view = gridView.getSelectedView();
				}
				view.performLongClick();
				return true;
			}

			if (gallery != null) {
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
					int newPosition = selectedItem + 10;
					if (newPosition > itemsCount) {
						newPosition = itemsCount - 1;
					}
					gallery.setSelection(newPosition);
					return true;
				}
			}
		}

		return super.onKeyDown(keyCode, event);
	}

	/**
	 * @param keyCode
	 * @return
	 */
	protected boolean contextMenuRequested(int keyCode) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		boolean menuKeySlidingMenu = prefs.getBoolean("remote_control_menu",
				true);
		return keyCode == KeyEvent.KEYCODE_C
				|| keyCode == KeyEvent.KEYCODE_BUTTON_Y
				|| keyCode == KeyEvent.KEYCODE_BUTTON_R2
				|| keyCode == KeyEvent.KEYCODE_PROG_RED
				|| (keyCode == KeyEvent.KEYCODE_MENU && menuKeySlidingMenu == false);
	}

	/**
	 * @param keyCode
	 * @return
	 */
	protected boolean isKeyCodeSkipForward(int keyCode) {
		return keyCode == KeyEvent.KEYCODE_F
				|| keyCode == KeyEvent.KEYCODE_PAGE_UP
				|| keyCode == KeyEvent.KEYCODE_CHANNEL_UP
				|| keyCode == KeyEvent.KEYCODE_MEDIA_NEXT
				|| keyCode == KeyEvent.KEYCODE_MEDIA_FAST_FORWARD
				|| keyCode == KeyEvent.KEYCODE_BUTTON_R1;
	}

	/**
	 * @param keyCode
	 * @return
	 */
	protected boolean isKeyCodeSkipBack(int keyCode) {
		return keyCode == KeyEvent.KEYCODE_R
				|| keyCode == KeyEvent.KEYCODE_PAGE_DOWN
				|| keyCode == KeyEvent.KEYCODE_CHANNEL_DOWN
				|| keyCode == KeyEvent.KEYCODE_MEDIA_PREVIOUS
				|| keyCode == KeyEvent.KEYCODE_MEDIA_REWIND
				|| keyCode == KeyEvent.KEYCODE_BUTTON_L1;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			if (data != null
					&& data.getAction().equals("com.mxtech.intent.result.VIEW")) {
				SerenityGallery gallery = (SerenityGallery) findViewById(R.id.moviePosterGallery);
				VideoContentInfo video = null;
				View selectedView = null;
				if (gallery != null) {
					video = (VideoContentInfo) gallery.getSelectedItem();
					selectedView = gallery.getSelectedView();
				} else {
					TwoWayGridView gridView = (TwoWayGridView) findViewById(R.id.movieGridView);
					if (gridView != null) {
						video = (VideoContentInfo) gridView.getSelectedItem();
						selectedView = gridView.getSelectedView();
					}
				}
				if (video != null) {
					updateProgress(data, video);
					ImageUtils.toggleProgressIndicator(selectedView,
							video.getResumeOffset(), video.getDuration());
				}
			}
		}
	}

	/**
	 * @param data
	 * @param video
	 */
	protected void updateProgress(Intent data, VideoContentInfo video) {
		long position = 0;
		position = data.getIntExtra("position", 0);

		video.setResumeOffset(Long.valueOf(position).intValue());

		if (video.isPartiallyWatched()) {
			UpdateProgressRequest request = new UpdateProgressRequest(position,
					video);
			video.setResumeOffset(Long.valueOf(position).intValue());
			request.execute();
			return;
		}

	}

	public void showMenuItems() {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		boolean tvMode = prefs.getBoolean("serenity_tv_mode", false);
		if (tvMode) {
			menuOptions.setVisibility(View.VISIBLE);
			menuOptions.requestFocusFromTouch();
		}
	}

	public void hideMenuItems() {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		boolean tvMode = prefs.getBoolean("serenity_tv_mode", false);
		if (tvMode) {
			menuOptions.setVisibility(View.GONE);
		}
	}

	public MenuDrawer getMenuDrawer() {
		return menuDrawer;
	}

	public void setMenuDrawer(MenuDrawer menuDrawer) {
		this.menuDrawer = menuDrawer;
	}

}
