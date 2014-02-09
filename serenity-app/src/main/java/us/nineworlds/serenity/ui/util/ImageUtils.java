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
import us.nineworlds.serenity.core.SerenityConstants;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader.TileMode;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ProgressBar;

/**
 * @author dcarver
 * 
 */
public class ImageUtils {

	/**
	 * This code is courtesy of Neil Davies at http://www.inter-fuser.com
	 * 
	 * @param context
	 *            the current context
	 * @param originalImage
	 *            The original Bitmap image used to create the reflection
	 * @return the bitmap with a reflection
	 */
	public static Bitmap createReflectedImage(Context context,
			Bitmap originalImage) {
		// The gap we want between the reflection and the original image
		final int reflectionGap = 1;

		int width = originalImage.getWidth();
		int height = originalImage.getHeight();

		// This will not scale but will flip on the Y axis
		Matrix matrix = new Matrix();
		matrix.preScale(1, -1);

		// Create a Bitmap with the flip matrix applied to it.
		// We only want the bottom half of the image

		Bitmap reflectionImage = Bitmap.createBitmap(originalImage, 0,
				(int) (height * .3), width, (int) (height * .3), matrix, false);

		// Create a new bitmap with same width but taller to fit reflection
		Bitmap bitmapWithReflection = Bitmap.createBitmap(width,
				(height + (int) (height * .3)), Config.ARGB_8888);

		// Create a new Canvas with the bitmap that's big enough for
		// the image plus gap plus reflection
		Canvas canvas = new Canvas(bitmapWithReflection);
		// Draw in the original image
		canvas.drawBitmap(originalImage, 0, 0, null);
		// Draw in the gap
		Paint defaultPaint = new Paint();
		canvas.drawRect(0, height, width, height + reflectionGap, defaultPaint);
		// Draw in the reflection
		canvas.drawBitmap(reflectionImage, 0, height + reflectionGap, null);

		// Create a shader that is a linear gradient that covers the reflection
		Paint paint = new Paint();
		LinearGradient shader = new LinearGradient(0,
				originalImage.getHeight(), 0, bitmapWithReflection.getHeight()
						+ reflectionGap, 0x70ffffff, 0x00ffffff, TileMode.CLAMP);
		// Set the paint to use this shader (linear gradient)
		paint.setShader(shader);
		// Set the Transfer mode to be porter duff and destination in
		paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
		// Draw a rectangle using the paint with our linear gradient
		canvas.drawRect(0, height, width, bitmapWithReflection.getHeight()
				+ reflectionGap, paint);

		return bitmapWithReflection;
	}
	
	/**
	 * Takes a value in pixels and converts it to a dpi value.  It adjusts
	 * the dpi size based on the screen density that is returned by 
	 * android.
	 * 
	 * @param originalHeight
	 * @param context The activity context
	 * @return
	 */
	public static int getDPI(int pixelsize, Activity context) {
		DisplayMetrics metrics = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay().getMetrics(metrics);
		int heightPixels = metrics.heightPixels;
		float sizeMultiplier = 1;
		switch (metrics.densityDpi) {
		case DisplayMetrics.DENSITY_LOW: {
			break;
		}
		case DisplayMetrics.DENSITY_MEDIUM: {
			break;
		}
		case DisplayMetrics.DENSITY_TV:
		case DisplayMetrics.DENSITY_HIGH: {
			sizeMultiplier = 1.5f;
			break;
		}
		case DisplayMetrics.DENSITY_XHIGH:
			if (heightPixels < 1100) {
				sizeMultiplier = 3f;
			} else {
				sizeMultiplier = 4f;
			}
			break;
		case DisplayMetrics.DENSITY_XXHIGH: {
			sizeMultiplier = 4f;
			break;
		}
		default: {
			sizeMultiplier = 3;
			break;
		}
		}
		
		int dpi = Math.round((pixelsize * sizeMultiplier) / metrics.density);
		return dpi;
	}
	
	
	/**
	 * @param galleryCellView
	 * @param pi
	 * @param watchedView
	 */
	public static void toggleProgressIndicator(View galleryCellView, int dividend,
			int divisor) {
		ProgressBar view = (ProgressBar) galleryCellView
				.findViewById(R.id.posterInprogressIndicator);

		final float percentWatched = Float.valueOf(dividend)
				/ Float.valueOf(divisor);
		
		if (percentWatched < SerenityConstants.WATCHED_PERCENT) {
			int progress = Float.valueOf(percentWatched * 100).intValue();
			if (progress < 10) {
				progress = 10;
			}
			view.setProgress(progress);
			view.setClickable(false);
			view.setVisibility(View.VISIBLE);
		} else {
			view.setVisibility(View.GONE);
		}
	}
		
}
