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

package org.robolectric.shadows;

import static org.robolectric.Robolectric.directlyOn;
import static org.robolectric.Robolectric.shadowOf;

import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;
import org.robolectric.internal.HiddenApi;
import org.robolectric.res.ResName;

import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;

/**
 * @author dcarver
 * 
 */
@Implements(Resources.class)
public class SerenityShadowResources extends ShadowResources {

	/**
	 * 
	 */
	public SerenityShadowResources() {
	}

	@Override
	@HiddenApi
	@Implementation
	public Drawable loadDrawable(TypedValue value, int id) {
		ResName resName = tryResName(id);
		Drawable drawable = fetchResource(value, id);
		// todo: this kinda sucks, find some better way...
		if (drawable != null) {
			shadowOf(drawable).createdFromResId = id;
			if (drawable instanceof BitmapDrawable) {
				Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
				if (bitmap != null) {
					ShadowBitmap shadowBitmap = shadowOf(bitmap);
					if (shadowBitmap.getCreatedFromResId() == -1) {
						shadowBitmap.setCreatedFromResId(id, resName);
					}
				}
			}
		}
		return drawable;
	}

	private ResName tryResName(int id) {
		return getResourceLoader().getResourceIndex().getResName(id);
	}

	/**
	 * @param value
	 * @param id
	 * @return
	 */
	protected Drawable fetchResource(TypedValue value, int id) {
		Drawable drawable = null;
		try {
			drawable = (Drawable) directlyOn(realResources, Resources.class,
					"loadDrawable", TypedValue.class, int.class).invoke(value,
					id);
		} catch (NotFoundException ex) {
			try {
				drawable = new ColorDrawable(value.data);
			} catch (NumberFormatException e) {

			}

		}
		return drawable;
	}

}
