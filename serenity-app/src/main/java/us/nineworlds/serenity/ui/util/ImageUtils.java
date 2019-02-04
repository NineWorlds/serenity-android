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

package us.nineworlds.serenity.ui.util;

import android.view.View;
import android.widget.ProgressBar;
import us.nineworlds.serenity.R;
import us.nineworlds.serenity.core.SerenityConstants;

public class ImageUtils {

    public static void toggleProgressIndicator(View galleryCellView, int dividend, int divisor) {
        ProgressBar view = galleryCellView.findViewById(R.id.posterInprogressIndicator);

        final float percentWatched = Float.valueOf(dividend) / Float.valueOf(divisor);

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
