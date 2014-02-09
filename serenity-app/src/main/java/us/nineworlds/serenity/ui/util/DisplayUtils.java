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

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;


/**
 * @author dcarver
 *
 */
public class DisplayUtils {
	
	public static int screenWidthDP(Activity context) {
		Display display = context.getWindowManager().getDefaultDisplay();
	    DisplayMetrics outMetrics = new DisplayMetrics ();
	    display.getMetrics(outMetrics);

	    float density  = context.getResources().getDisplayMetrics().density;
	    int dpWidth  = Float.valueOf(outMetrics.widthPixels / density).intValue();
	    return dpWidth;
	}
	
	public static int screenHeightDP(Activity context) {
		Display display = context.getWindowManager().getDefaultDisplay();
	    DisplayMetrics outMetrics = new DisplayMetrics ();
	    display.getMetrics(outMetrics);

	    float density  = context.getResources().getDisplayMetrics().density;
	    int dpWidth  = Float.valueOf(outMetrics.heightPixels / density).intValue();
	    return dpWidth;
	}

	public static void overscanCompensation(Context context, View... views) {
		final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

		if (prefs.getBoolean("overscan_compensation", false)) {
			for (View view : views) {
				ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams)  view.getLayoutParams();
				params.setMargins(
						prefs.getInt("overscan_left", 50),
						prefs.getInt("overscan_top", 50),
						prefs.getInt("overscan_right", 50),
						prefs.getInt("overscan_bottom", 50));
			}
		}
	}
	
}
