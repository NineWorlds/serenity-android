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
import us.nineworlds.serenity.ui.util.VideoPlayerIntentUtils;
import us.nineworlds.serenity.ui.video.player.SerenitySurfaceViewVideoActivity;
import us.nineworlds.serenity.widgets.SerenityGallery;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
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
		
		if (requestCode == SerenityConstants.EXIT_PLAYBACK_IMMEDIATELY) {

			if (resultCode == SerenityConstants.EXIT_PLAYBACK_IMMEDIATELY) {
				if (!SerenityApplication.getVideoPlaybackQueue().isEmpty()) {
					showQueueNotEmptyMessage();
				}
				return;
			}

			if (!externalPlayer) {

				if (!SerenityApplication.getVideoPlaybackQueue().isEmpty()) {
					Intent vpIntent = new Intent(this,
							SerenitySurfaceViewVideoActivity.class);
					startActivityForResult(vpIntent,
							SerenityConstants.EXIT_PLAYBACK_IMMEDIATELY);
					return;
				}

			}
		}

		if (data != null) {
			if (data.hasExtra("position")) {
				SerenityGallery gallery = (SerenityGallery) findViewById(R.id.moviePosterGallery);
				if (gallery != null) {
					VideoContentInfo video = (VideoContentInfo) gallery
							.getSelectedItem();

					if (video != null) {
						updateProgress(data, video);
					}
				} else {
					TwoWayGridView gridView = (TwoWayGridView) findViewById(R.id.movieGridView);
					if (gridView != null) {
						VideoContentInfo video = (VideoContentInfo) gridView
								.getSelectedItem();
						if (video == null) {
							video = (VideoContentInfo) gridView
									.getItemAtPosition(SerenityConstants.CLICKED_GRID_VIEW_ITEM);
						}
						if (video != null) {
							updateProgress(data, video);
						}
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
		VideoPlayerIntentUtils.launchExternalPlayer(videoContentInfo, this);
	}
}
