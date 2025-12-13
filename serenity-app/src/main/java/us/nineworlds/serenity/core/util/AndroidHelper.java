/**
 * The MIT License (MIT)
 * Copyright (c) 2014 David Carver
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

package us.nineworlds.serenity.core.util;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.audio.AudioCapabilities;

public class AndroidHelper {

  private static final String AMAZON_FEATURE_FIRE_TV = "amazon.hardware.fire_tv";
  private static final String ANDROID_SOFTWARE_LEANBACK = "android.software.leanback";
  private static final String AMAZON_TV_MODELS = "AFT";
  private static final String MANUFACTURER_AMAZON = "Amazon";
  private static final String ANDROID_HARDWARE_TYPE_TELEVISION = "android.hardware.type.television";
  private static final String ANDROID_SHIELD_MODEL = "SHIELD";
  private static final String ANDROID_BRAVIA_MODEL = "Bravia";
  private static final String ANDROID_TCL_BEYONDTV5 = "BeyondTV";

  Context context;

  public AndroidHelper(Context context) {
    this.context = context;
  }

  public boolean isAndroidTV() {
    final PackageManager pm = context.getPackageManager();
    return isAmazonFireTV() || pm.hasSystemFeature(ANDROID_HARDWARE_TYPE_TELEVISION);
  }

  public boolean isAmazonFireTV() {
    return context.getPackageManager().hasSystemFeature(AMAZON_FEATURE_FIRE_TV)
        || Build.MODEL.startsWith(AMAZON_TV_MODELS) && Build.MANUFACTURER.equals(MANUFACTURER_AMAZON);
  }

  public boolean isNvidiaShield() {
    return Build.MODEL.contains(ANDROID_SHIELD_MODEL);
  }

  public boolean isBravia() {
    return Build.MODEL.toLowerCase().contains(ANDROID_BRAVIA_MODEL.toLowerCase());
  }

  public boolean enableTunneling() {
    return false;
  }

  public boolean isBeyondTV() {
    return Build.MODEL.toLowerCase().contains(ANDROID_TCL_BEYONDTV5.toLowerCase()) || Build.MODEL.toLowerCase().contains("smart tv");
  }

  public boolean isLeanbackSupported() {
    final PackageManager pm = context.getPackageManager();
    return pm.hasSystemFeature(ANDROID_SOFTWARE_LEANBACK);
  }

  public boolean isAudioPassthroughSupported(String codec) {
    AudioCapabilities audioCapabilities = AudioCapabilities.getCapabilities(context);
    int encoding = 0;
    switch (codec.toLowerCase()) {
      case "ac3":
        encoding = C.ENCODING_AC3;
        break;
      case "eac3":
        encoding = C.ENCODING_E_AC3;
        break;
      case "dts":
        encoding = C.ENCODING_DTS;
        break;
      case "truehd":
        encoding = C.ENCODING_DOLBY_TRUEHD;
        break;
      default:
        return false;
    }

    return audioCapabilities.supportsEncoding(encoding);
  }

  public int buildNumber() {
    return Build.VERSION.SDK_INT;
  }
}
