/**
 * The MIT License (MIT)
 * Copyright (c) 2013 David Carver
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

import us.nineworlds.serenity.R;
import us.nineworlds.serenity.SerenityApplication;
import us.nineworlds.serenity.core.SerenityConstants;
import us.nineworlds.serenity.core.model.VideoContentInfo;
import us.nineworlds.serenity.core.services.WatchedVideoAsyncTask;
import us.nineworlds.serenity.ui.util.ImageUtils;
import us.nineworlds.serenity.ui.util.VideoPlayerIntentUtils;
import us.nineworlds.serenity.ui.video.player.SerenitySurfaceViewVideoActivity;
import us.nineworlds.serenity.widgets.SerenityGallery;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.jess.ui.TwoWayGridView;

/**
 * @author dcarver
 * 
 */
public abstract class SerenityVideoActivity extends SerenityActivity {

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		boolean externalPlayer = prefs.getBoolean("external_player", false);
		boolean extPlayerVideoQueueEnabled = prefs.getBoolean(
				"external_player_continuous_playback", false);

		if (data != null && (data.hasExtra("position") || data.hasExtra("end_by"))) {
			SerenityGallery gallery = findGalleryView();
			View selectedView = null;
			VideoContentInfo video = null;
			BaseAdapter adapter = null;
			
			if (gallery != null) {
				video = (VideoContentInfo) gallery.getSelectedItem();
				adapter = (BaseAdapter) gallery.getAdapter();
				selectedView = gallery.getSelectedView();
			} else {
				TwoWayGridView gridView = findGridView();
				if (gridView != null) {
					video = (VideoContentInfo) gridView.getSelectedItem();
					adapter = (BaseAdapter) gridView.getAdapter();
					selectedView = gridView.getSelectedView();
					if (video == null) {
						video = (VideoContentInfo) gridView
								.getItemAtPosition(SerenityConstants.CLICKED_GRID_VIEW_ITEM);
						gridView.setSelectionInTouch(SerenityConstants.CLICKED_GRID_VIEW_ITEM);
						selectedView = gridView.getSelectedView();
					}
				}
			}
			String mxplayerString = data.getStringExtra("end_by");
			if (hasMXPlayerCompletedNormally(data, mxplayerString)) {
				if (video != null) {
					video.setResumeOffset(video.getDuration());
					toggleWatched(video);
					adapter.notifyDataSetChanged();
				}
			} else if (data.hasExtra("position")) {
				
				if (selectedView != null) {
					if (video != null) {
						View watchedView = selectedView.findViewById(R.id.posterWatchedIndicator);
						updateProgress(data, video);
						if (video.isWatched()) {
							watchedView.setVisibility(View.VISIBLE);
							toggleWatched(video);
						} else if (video.isPartiallyWatched()) {
							if (watchedView.isShown()) {
								watchedView.setVisibility(View.INVISIBLE);
							}
							ImageUtils.toggleProgressIndicator(selectedView,
									video.getResumeOffset(),
									video.getDuration());
						}
						adapter.notifyDataSetChanged();
					}
				}				
			}
			

			if (externalPlayer && extPlayerVideoQueueEnabled) {
				if (!SerenityApplication.getVideoPlaybackQueue().isEmpty()) {

					if (isVimuUserCancelResult(resultCode)) {
						showQueueNotEmptyMessage();
						return;
					}

					if (notMXPlayer(data)) {
						externalPlayerPlayNext();
						return;
					}

					String mxplayerEndedBy = data.getStringExtra("end_by");
					if ("user".equals(mxplayerEndedBy)) {
						showQueueNotEmptyMessage();
						return;
					}

					externalPlayerPlayNext();
					return;
				}
			}
		}

		if (requestCode == SerenityConstants.EXIT_PLAYBACK_IMMEDIATELY) {

			if (resultCode == SerenityConstants.EXIT_PLAYBACK_IMMEDIATELY) {
				if (!SerenityApplication.getVideoPlaybackQueue().isEmpty()) {
					showQueueNotEmptyMessage();
				}
			} else if (!externalPlayer) {
				if (!SerenityApplication.getVideoPlaybackQueue().isEmpty()) {
					Intent vpIntent = new Intent(this,
							SerenitySurfaceViewVideoActivity.class);
					startActivityForResult(vpIntent,
							SerenityConstants.EXIT_PLAYBACK_IMMEDIATELY);
					return;
				}
			}
		}

	}

	/**
	 * @param data
	 * @param mxplayerString
	 * @return
	 */
	protected boolean hasMXPlayerCompletedNormally(Intent data,
			String mxplayerString) {
		return data.hasExtra("end_by") && "playback_completion".equals(mxplayerString);
	}

	/**
	 * @return
	 */
	protected SerenityGallery findGalleryView() {
		return (SerenityGallery) findViewById(R.id.moviePosterGallery);
	}

	/**
	 * @return
	 */
	protected TwoWayGridView findGridView() {
		return (TwoWayGridView) findViewById(R.id.movieGridView);
	}

	/**
	 * @param selectedView
	 * @param video
	 */
	protected void toggleWatched(VideoContentInfo video) {
		if (video.isWatched()) {
			new WatchedVideoAsyncTask().execute(video.id());
			video.setResumeOffset(0);
			video.setViewCount(video.getViewCount() + 1);
		}
	}

	/**
	 * @param data
	 * @return
	 */
	protected boolean notMXPlayer(Intent data) {
		return !data.hasExtra("end_by");
	}

	/**
	 * @param resultCode
	 * @return
	 */
	protected boolean isVimuUserCancelResult(int resultCode) {
		return resultCode == 0 || resultCode == 4;
	}

	/**
	 * 
	 */
	protected void showQueueNotEmptyMessage() {
		Toast.makeText(this, R.string.there_are_still_videos_int_the_queue_,
				Toast.LENGTH_LONG).show();
	}

	/**
	 * @param mxPlayerResume
	 */
	protected void externalPlayerPlayNext() {
		VideoContentInfo videoContentInfo = SerenityApplication
				.getVideoPlaybackQueue().poll();
		VideoPlayerIntentUtils.launchExternalPlayer(videoContentInfo, this, false);
	}
}
