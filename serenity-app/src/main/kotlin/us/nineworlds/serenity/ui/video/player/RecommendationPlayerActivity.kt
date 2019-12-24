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
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS
 * OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
 * OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF
 * OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package us.nineworlds.serenity.ui.video.player

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import us.nineworlds.serenity.R
import us.nineworlds.serenity.core.model.VideoContentInfo
import us.nineworlds.serenity.core.services.OnDeckRecommendationAsyncTask
import us.nineworlds.serenity.ui.activity.SerenityActivity
import us.nineworlds.serenity.ui.util.ExternalPlayerResultHandler
import us.nineworlds.serenity.ui.util.PlayerResultHandler
import us.nineworlds.serenity.ui.util.VideoPlayerIntentUtils
import javax.inject.Inject

class RecommendationPlayerActivity : SerenityActivity() {

  @Inject
  internal lateinit var vpUtils: VideoPlayerIntentUtils

  private lateinit var video: VideoContentInfo

  override fun screenName(): String = "Recommendations Player"

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setContentView(R.layout.video_playback)

    val intent = intent
    if (intent != null) {
      val objVideo = intent.extras.getSerializable("serenity_video")
      if (objVideo != null) {
        video = objVideo as VideoContentInfo
        vpUtils.playVideo(this, video, true)
      }
    }
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    val prefs = PreferenceManager.getDefaultSharedPreferences(this)
    val externalPlayer = prefs.getBoolean("external_player", false)

    if (data != null) {
      if (externalPlayer) {
        val externalPlayerHandler = ExternalPlayerResultHandler(resultCode, data, this, null)
        externalPlayerHandler.updateVideoPlaybackPosition(video)
      } else {
        val playerResultHandler = PlayerResultHandler(data, null)
        playerResultHandler.updateVideoPlaybackPosition(video)
      }
    }

    val onDeckRecomendations = OnDeckRecommendationAsyncTask(applicationContext)
    onDeckRecomendations.execute()

    finish()
  }
}
