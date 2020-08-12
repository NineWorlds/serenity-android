package us.nineworlds.serenity.ui.video.player

import android.app.Activity
import android.content.SharedPreferences
import android.util.Log
import android.view.KeyEvent
import com.google.android.exoplayer2.SimpleExoPlayer
import org.greenrobot.eventbus.EventBus
import toothpick.Toothpick
import us.nineworlds.serenity.common.annotations.InjectionConstants
import us.nineworlds.serenity.common.annotations.OpenForTesting
import us.nineworlds.serenity.events.video.OnScreenDisplayEvent
import us.nineworlds.serenity.events.video.StartPlaybackEvent
import us.nineworlds.serenity.events.video.StopPlaybackEvent
import javax.inject.Inject

@OpenForTesting
class VideoKeyCodeHandlerDelegate(
  val player: SimpleExoPlayer, val activity: Activity,
  val presenter: ExoplayerContract.ExoplayerPresenter
) {

  @Inject
  lateinit var preferences: SharedPreferences
  val eventBus = EventBus.getDefault()

  init {
    Toothpick.inject(this, Toothpick.openScope(InjectionConstants.APPLICATION_SCOPE))
  }

  fun isPlaying(): Boolean = player.playWhenReady

  fun pause() {
    player.playWhenReady = false
  }

  fun play() {
    player.playWhenReady = true
  }

  fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
    val nextPrevBehavior = preferences.getString("next_prev_behavior", "queue")

    if (isKeyCodeInfo(keyCode)) {
      if (isHudShowing()) {
        eventBus.post(OnScreenDisplayEvent(true))
      } else {
        eventBus.post(OnScreenDisplayEvent(false))
      }
      return true
    }

    if (isKeyCodePlay(keyCode)) {
      if (isPlaying()) {
        if (isHudShowing()) {
          eventBus.post(OnScreenDisplayEvent(false))
        } else {
          eventBus.post(OnScreenDisplayEvent(true))
        }
      } else {
        play()
        eventBus.post(StartPlaybackEvent())
      }
      return true
    }

    if (isKeyCodePauseResume(keyCode)) {
      if (player.playWhenReady) {
        pause()
        eventBus.post(StopPlaybackEvent())
      } else {
        play()
        eventBus.post(StartPlaybackEvent())
      }
      return true
    }

    if (keyCode == KeyEvent.KEYCODE_MEDIA_NEXT) {
      if (nextPrevBehavior == "queue") {
        if (isPlaying()) {
          player.stop()
        }
        activity.finish()
        return true
      }
      val skipTo: Long
      val currentPosition = player.currentPosition
      val duration = player.duration

      if (nextPrevBehavior!!.endsWith("%")) {
        val percent = Integer.valueOf(nextPrevBehavior.substring(0, nextPrevBehavior.length - 1))
        skipTo = currentPosition + duration * percent!! / 100
      } else {
        skipTo = currentPosition + Integer.valueOf(nextPrevBehavior)!!
      }
      skipToOffset(skipTo)
      return true
    }

    try {
      val currentPosition = player.currentPosition
      val duration = player.duration
      if (keyCode == KeyEvent.KEYCODE_MEDIA_PREVIOUS && nextPrevBehavior != "queue") {
        val skipTo: Long
        if (nextPrevBehavior!!.endsWith("%")) {
          val percent = Integer.valueOf(nextPrevBehavior.substring(0, nextPrevBehavior.length - 1))
          skipTo = currentPosition - duration * percent!! / 100
        } else {
          skipTo = currentPosition - Integer.valueOf(nextPrevBehavior)!!
        }
        skipToOffset(skipTo)
        return true
      }

      if (isKeyCodeSkipForward(keyCode)) {
        skipToOffset(
          currentPosition + Integer.valueOf(preferences.getString("skip_forward_time", "30000")!!)
        )
        return true
      }

      if (isKeyCodeSkipBack(keyCode)) {
        skipToOffset(
          currentPosition - Integer.valueOf(
            preferences.getString("skip_backward_time", "10000")!!
          )
        )
        return true
      }

      if (isKeyCodeStop(keyCode)) {
        if (isPlaying()) {
          pause()
          eventBus.post(StopPlaybackEvent())
        }
        return true
      }

      if (isSkipByPercentage(keyCode)) {
        return true
      }
    } catch (e: IllegalStateException) {
      Log.e(this.javaClass.name, "Media Player is in an illegalstate.", e)
    }

    return false
  }

  fun isHudShowing(): Boolean = presenter.isHudShowing()

  internal fun isKeyCodeStop(keyCode: Int): Boolean =
    keyCode == KeyEvent.KEYCODE_MEDIA_STOP || keyCode == KeyEvent.KEYCODE_S

  internal fun isKeyCodeSkipBack(keyCode: Int): Boolean {
    return keyCode == KeyEvent.KEYCODE_MEDIA_REWIND
      || keyCode == KeyEvent.KEYCODE_R
      || keyCode == KeyEvent.KEYCODE_BUTTON_L1
      || keyCode == KeyEvent.KEYCODE_BUTTON_L2
  }

  internal fun isKeyCodeSkipForward(keyCode: Int): Boolean {
    return keyCode == KeyEvent.KEYCODE_MEDIA_FAST_FORWARD
      || keyCode == KeyEvent.KEYCODE_F
      || keyCode == KeyEvent.KEYCODE_BUTTON_R1
      || keyCode == KeyEvent.KEYCODE_BUTTON_R2
  }

  internal fun isKeyCodePauseResume(keyCode: Int): Boolean {
    return keyCode == KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE
      || keyCode == KeyEvent.KEYCODE_MEDIA_PAUSE
      || keyCode == KeyEvent.KEYCODE_P
      || keyCode == KeyEvent.KEYCODE_SPACE
      || keyCode == KeyEvent.KEYCODE_BUTTON_A
      || keyCode == KeyEvent.KEYCODE_MEDIA_PLAY
  }

  internal fun isKeyCodeInfo(keyCode: Int): Boolean {
    return keyCode == KeyEvent.KEYCODE_INFO
      || keyCode == KeyEvent.KEYCODE_I
      || keyCode == KeyEvent.KEYCODE_MENU
      || keyCode == KeyEvent.KEYCODE_BUTTON_Y
      || keyCode == KeyEvent.KEYCODE_DPAD_CENTER
  }

  internal fun isKeyCodePlay(keyCode: Int): Boolean = keyCode == KeyEvent.KEYCODE_MEDIA_PLAY

  internal fun isSkipByPercentage(keyCode: Int): Boolean {
    val duration = player.duration
    if (keyCode == KeyEvent.KEYCODE_1) {
      skipByPercentage(duration, 0.10f)
      return true
    }
    if (keyCode == KeyEvent.KEYCODE_2) {
      skipByPercentage(duration, 0.20f)
      return true
    }
    if (keyCode == KeyEvent.KEYCODE_3) {
      skipByPercentage(duration, 0.30f)
      return true
    }
    if (keyCode == KeyEvent.KEYCODE_4) {
      skipByPercentage(duration, 0.40f)
      return true
    }
    if (keyCode == KeyEvent.KEYCODE_5) {
      skipByPercentage(duration, 0.50f)
      return true
    }
    if (keyCode == KeyEvent.KEYCODE_6) {
      skipByPercentage(duration, 0.60f)
      return true
    }

    if (keyCode == KeyEvent.KEYCODE_7) {
      skipByPercentage(duration, 0.70f)
      return true
    }
    if (keyCode == KeyEvent.KEYCODE_8) {
      skipByPercentage(duration, 0.80f)
      return true
    }
    if (keyCode == KeyEvent.KEYCODE_9) {
      skipByPercentage(duration, 0.90f)
      return true
    }
    if (keyCode == KeyEvent.KEYCODE_0) {
      skipToPercentage(0)
      return true
    }
    return false
  }

  internal fun skipByPercentage(duration: Long, percentage: Float) {
    val newPos: Long = Math.round(duration * percentage).toLong()
    skipToPercentage(newPos)
  }

  internal fun skipToPercentage(newPos: Long) {
    player.seekTo(newPos)
  }

  internal fun skipToOffset(skipOffset: Long) {
    var skipOffset = skipOffset
    val duration = player.duration
    if (skipOffset > duration) {
      skipOffset = duration - 1
    } else if (skipOffset < 0) {
      skipOffset = 0
    }
    player.seekTo(skipOffset)
  }
}