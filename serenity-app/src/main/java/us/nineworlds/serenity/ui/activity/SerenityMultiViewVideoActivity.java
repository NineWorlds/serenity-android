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

import android.support.v7.widget.RecyclerView;
import net.ganin.darv.DpadAwareRecyclerView;
import us.nineworlds.serenity.core.SerenityConstants;
import us.nineworlds.serenity.core.model.VideoContentInfo;
import us.nineworlds.serenity.core.services.OnDeckRecommendationAsyncTask;
import us.nineworlds.serenity.ui.adapters.AbstractPosterImageGalleryAdapter;
import us.nineworlds.serenity.ui.util.ExternalPlayerResultHandler;
import us.nineworlds.serenity.ui.util.PlayerResultHandler;
import us.nineworlds.serenity.ui.video.player.SerenitySurfaceViewVideoActivity;
import us.nineworlds.serenity.widgets.SerenityGallery;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.BaseAdapter;

import com.jess.ui.TwoWayGridView;

/**
 * A activity that handles the indicator of whether toggling between Grid and
 * Detail view should occur. Views that need to support Detail and Grid view
 * should extend this view.
 *
 * @author dcarver
 *
 */
public abstract class SerenityMultiViewVideoActivity extends SerenityVideoActivity {

	protected boolean gridViewActive = false;

	@Override
	public void finish() {
		super.finish();
	}

	protected boolean posterLayoutActive = false;

	public boolean isGridViewActive() {
		return gridViewActive;
	}

	/**
	 * Used to indicate whether posters or banners are shown.
	 *
	 * @return
	 */
	public boolean isPosterLayoutActive() {
		return posterLayoutActive;
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);

		// savedCategory = savedInstanceState.getString("savedCategory");
		// savedSelectedGenreCategory = savedInstanceState
		// .getString("savedSelectedGenreCategory");
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		// if (savedCategory != null) {
		// outState.putString("savedCategory", savedCategory);
		// }
		// if (savedSelectedGenreCategory != null) {
		// outState.putString("savedSelectedGenreCategory",
		// savedSelectedGenreCategory);
		//
		// }
	}

	public void setGridViewEnabled(boolean sw) {
		gridViewActive = sw;
	}

	public void setPosterLayoutActive(boolean sw) {
		posterLayoutActive = sw;
	}

	public abstract AbstractPosterImageGalleryAdapter getAdapter();

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		boolean externalPlayer = prefs.getBoolean("external_player", false);
		DpadAwareRecyclerView gallery = findGalleryView();
		View selectedView;
		VideoContentInfo video;
		AbstractPosterImageGalleryAdapter adapter = getAdapter();

		RecyclerView.LayoutManager layoutManager = gallery.getLayoutManager();
		video = (VideoContentInfo) adapter.getItem(gallery.getSelectedItemPosition());
		selectedView = layoutManager.findViewByPosition(gallery.getSelectedItemPosition());

		if (data != null) {
			if (externalPlayer) {
				ExternalPlayerResultHandler externalPlayerHandler = new ExternalPlayerResultHandler(
						resultCode, data, this, adapter);
				externalPlayerHandler.updatePlaybackPosition(video,
						selectedView);
			} else {
				PlayerResultHandler playerResultHandler =

						new PlayerResultHandler(
						data, adapter);
				playerResultHandler.updateVideoPlaybackPosition(video,
						selectedView);
			}
		}

		gallery.requestFocus();

		if (requestCode == SerenityConstants.EXIT_PLAYBACK_IMMEDIATELY) {

			if (resultCode == SerenityConstants.EXIT_PLAYBACK_IMMEDIATELY) {
				if (!videoQueue.isEmpty()) {
					showQueueNotEmptyMessage();
				}
				return;
			}



			if (!externalPlayer) {
				if (!videoQueue.isEmpty()) {
					Intent vpIntent = new Intent(this,
							SerenitySurfaceViewVideoActivity.class);
					startActivityForResult(vpIntent,
							SerenityConstants.EXIT_PLAYBACK_IMMEDIATELY);
					return;
				}
			}
		}

		OnDeckRecommendationAsyncTask onDeckRecomendations = new OnDeckRecommendationAsyncTask(
				getApplicationContext());
		onDeckRecomendations.execute();
	}

}
