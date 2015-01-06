/**
 * The MIT License (MIT)
 * Copyright (c) 2012-2015 David Carver
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

import java.util.LinkedList;

import javax.inject.Inject;
import javax.inject.Singleton;

import us.nineworlds.serenity.R;
import us.nineworlds.serenity.core.model.VideoContentInfo;
import us.nineworlds.serenity.injection.ApplicationContext;
import us.nineworlds.serenity.injection.BaseInjector;
import us.nineworlds.serenity.injection.ForVideoQueue;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

@Singleton
public class VideoQueueHelper extends BaseInjector {

	@Inject
	@ApplicationContext
	protected Context context;

	@Inject
	protected SharedPreferences sharedPreferences;

	@Inject
	@ForVideoQueue
	protected LinkedList<VideoContentInfo> videoQueue;

	public void performAddToQueue(VideoContentInfo videoInfo) {
		boolean extplayer = sharedPreferences.getBoolean("external_player",
				false);
		boolean extplayerVideoQueue = sharedPreferences.getBoolean(
				"external_player_continuous_playback", false);
		if (extplayer && !extplayerVideoQueue) {
			Toast.makeText(
					context,
					R.string.external_player_video_queue_support_has_not_been_enabled_,
					Toast.LENGTH_LONG).show();
			return;
		}

		videoQueue.add(videoInfo);
	}

}
