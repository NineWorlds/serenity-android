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

package us.nineworlds.serenity.core;

import javax.inject.Inject;

import us.nineworlds.plex.rest.PlexappFactory;
import us.nineworlds.serenity.core.util.AndroidHelper;
import us.nineworlds.serenity.injection.BaseInjector;
import us.nineworlds.serenity.volley.DefaultLoggingVolleyErrorListener;
import us.nineworlds.serenity.volley.LibraryResponseListener;
import us.nineworlds.serenity.volley.VolleyUtils;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

public class OnDeckRecommendations extends BaseInjector {

	@Inject
	PlexappFactory factory;

	@Inject
	AndroidHelper androidHelper;

	@Inject
	SharedPreferences preferences;

	@Inject
	VolleyUtils volley;

	private final Context context;

	public OnDeckRecommendations(Context context) {
		super();
		this.context = context;
	}

	public boolean recommended() {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
			return false;
		}

		if (!androidHelper.isLeanbackSupported()) {
			return false;
		}

		boolean onDeckRecommendations = preferences.getBoolean(
				SerenityConstants.PREFERENCE_ONDECK_RECOMMENDATIONS, false);
		boolean isAndroidTV = preferences.getBoolean(
				SerenityConstants.PREFERENCE_TV_MODE, false);

		if (onDeckRecommendations == false || isAndroidTV == false) {
			return false;
		}

		String sectionsURL = factory.getSectionsURL();
		volley.volleyXmlGetRequest(sectionsURL, new LibraryResponseListener(
				context), new DefaultLoggingVolleyErrorListener());
		return true;
	}
}
