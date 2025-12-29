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
 * <p>
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS
 * OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
 * OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF
 * OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package us.nineworlds.serenity.ui.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.KeyEvent;

import com.google.firebase.analytics.FirebaseAnalytics;

import us.nineworlds.serenity.injection.InjectingMvpActivity;

public abstract class SerenityActivity extends InjectingMvpActivity {

  protected FirebaseAnalytics analytics;
  protected Handler scrollingHandler = new Handler();

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    analytics = FirebaseAnalytics.getInstance(this);
    analytics.setCurrentScreen(this, screenName(), null);
  }
  
  protected abstract String screenName();

  protected boolean contextMenuRequested(int keyCode) {
    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
    boolean menuKeySlidingMenu = prefs.getBoolean("remote_control_menu", true);
    return keyCode == KeyEvent.KEYCODE_C
        || keyCode == KeyEvent.KEYCODE_BUTTON_Y
        || keyCode == KeyEvent.KEYCODE_BUTTON_R2
        || keyCode == KeyEvent.KEYCODE_PROG_RED
        || (keyCode == KeyEvent.KEYCODE_MENU && menuKeySlidingMenu == false);
  }

  protected boolean isKeyCodeSkipForward(int keyCode) {
    return keyCode == KeyEvent.KEYCODE_F
        || keyCode == KeyEvent.KEYCODE_PAGE_UP
        || keyCode == KeyEvent.KEYCODE_CHANNEL_UP
        || keyCode == KeyEvent.KEYCODE_MEDIA_NEXT
        || keyCode == KeyEvent.KEYCODE_MEDIA_FAST_FORWARD
        || keyCode == KeyEvent.KEYCODE_BUTTON_R1;
  }

  protected boolean isKeyCodeSkipBack(int keyCode) {
    return keyCode == KeyEvent.KEYCODE_R
        || keyCode == KeyEvent.KEYCODE_PAGE_DOWN
        || keyCode == KeyEvent.KEYCODE_CHANNEL_DOWN
        || keyCode == KeyEvent.KEYCODE_MEDIA_PREVIOUS
        || keyCode == KeyEvent.KEYCODE_MEDIA_REWIND
        || keyCode == KeyEvent.KEYCODE_BUTTON_L1;
  }
}
