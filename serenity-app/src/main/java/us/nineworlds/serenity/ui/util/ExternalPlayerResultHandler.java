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

package us.nineworlds.serenity.ui.util;

import us.nineworlds.serenity.R;
import us.nineworlds.serenity.SerenityApplication;
import us.nineworlds.serenity.core.externalplayer.ExternalPlayer;
import us.nineworlds.serenity.core.externalplayer.ExternalPlayerFactory;
import us.nineworlds.serenity.core.externalplayer.MXPlayer;
import us.nineworlds.serenity.core.model.VideoContentInfo;
import us.nineworlds.serenity.core.services.UpdateProgressRequest;
import us.nineworlds.serenity.core.services.WatchedVideoAsyncTask;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Toast;

public class ExternalPlayerResultHandler {

	Intent data;
	Activity activity;
	String externalPlayerValue;
	boolean extPlayerVideoQueueEnabled;
	BaseAdapter adapter;
	int resultCode;
	ExternalPlayer externalPlayer;

	public ExternalPlayerResultHandler(int resultCode, Intent data,
			Activity activity, BaseAdapter adapter) {
		this.data = data;
		this.activity = activity;
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(activity);
		externalPlayerValue = preferences.getString(
				"serenity_external_player_filter", "default");
		extPlayerVideoQueueEnabled = preferences.getBoolean(
				"external_player_continuous_playback", false);
		this.adapter = adapter;
		this.resultCode = resultCode;
	}

	public void updatePlaybackPosition(VideoContentInfo video, View selectedView) {
		ExternalPlayerFactory externalPlayerFactory = new ExternalPlayerFactory(
				video, activity);
		externalPlayer = externalPlayerFactory
				.createExternalPlayer(externalPlayerValue);

		boolean mxPlayerCompleted = updateMXPlayerCompletedNormally(video);
		if (shouldUpdatePlaybackPosition(mxPlayerCompleted)) {
			updateVideoPlaybackPosition(video, selectedView);
		}

		if (!extPlayerVideoQueueEnabled) {
			return;
		}

		playNextQueueEntry();
		return;
	}

	private void playNextQueueEntry() {
		if (SerenityApplication.getVideoPlaybackQueue().isEmpty()) {
			return;
		}

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
	}

	/**
	 * @param video
	 * @param selectedView
	 */
	private void updateVideoPlaybackPosition(VideoContentInfo video,
			View selectedView) {
		if (selectedView == null || video == null) {
			return;
		}

		View watchedView = selectedView
				.findViewById(R.id.posterWatchedIndicator);

		updateProgress(data, video);
		if (video.isWatched()) {
			watchedView.setVisibility(View.VISIBLE);
			toggleWatched(video);
		} else if (video.isPartiallyWatched()) {
			if (watchedView.isShown()) {
				watchedView.setVisibility(View.INVISIBLE);
			}
			ImageUtils.toggleProgressIndicator(selectedView,
					video.getResumeOffset(), video.getDuration());
		}
		adapter.notifyDataSetChanged();
	}

	/**
	 * @param mxPlayerCompleted
	 * @return
	 */
	private boolean shouldUpdatePlaybackPosition(boolean mxPlayerCompleted) {
		return !mxPlayerCompleted && externalPlayer.supportsPlaybackPosition()
				&& data.hasExtra("position");
	}

	private boolean updateMXPlayerCompletedNormally(VideoContentInfo video) {
		if (externalPlayer instanceof MXPlayer
				&& hasMXPlayerCompletedNormally(data)) {
			if (video != null) {
				video.setResumeOffset(video.getDuration());
				toggleWatched(video);
				adapter.notifyDataSetChanged();
			}
			return true;
		}
		return false;
	}

	/**
	 * @param data
	 * @param mxplayerString
	 * @return
	 */
	protected boolean hasMXPlayerCompletedNormally(Intent data) {
		String mxplayerString = data.getStringExtra("end_by");
		return "playback_completion".equals(mxplayerString);
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
		Toast.makeText(activity,
				R.string.there_are_still_videos_int_the_queue_,
				Toast.LENGTH_LONG).show();
	}

	/**
	 * @param mxPlayerResume
	 */
	protected void externalPlayerPlayNext() {
		VideoContentInfo videoContentInfo = SerenityApplication
				.getVideoPlaybackQueue().poll();
		VideoPlayerIntentUtils.launchExternalPlayer(videoContentInfo, activity,
				false);
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

}
