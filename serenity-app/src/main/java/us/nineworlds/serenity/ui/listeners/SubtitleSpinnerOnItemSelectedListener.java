/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2012-2015 David Carver
 *
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

package us.nineworlds.serenity.ui.listeners;

import javax.inject.Inject;

import us.nineworlds.serenity.core.model.VideoContentInfo;
import us.nineworlds.serenity.core.model.impl.Subtitle;
import us.nineworlds.serenity.injection.BaseInjector;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;

public class SubtitleSpinnerOnItemSelectedListener extends BaseInjector
implements OnItemSelectedListener {

	private final VideoContentInfo video;
	private boolean firstSelection = true;

	protected void setFirstSelection(boolean firstSelection) {
		this.firstSelection = firstSelection;
	}

	@Inject
	protected SharedPreferences preferences;

	public SubtitleSpinnerOnItemSelectedListener(VideoContentInfo video) {
		super();
		this.video = video;
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		Subtitle subtitle = null;
		if (!firstSelection) {
			subtitle = (Subtitle) parent.getItemAtPosition(position);
			video.setSubtitle(subtitle);
			return;
		}

		boolean automaticallySelectSubtitle = preferences.getBoolean(
				"automatic_subtitle_selection", false);
		if (automaticallySelectSubtitle) {
			String languageCode = preferences.getString(
					"preferred_subtitle_language", "");
			if (languageCode != "") {
				for (int i = 0; i < parent.getAdapter().getCount(); i++) {
					subtitle = (Subtitle) parent.getItemAtPosition(i);
					if (languageCode.equals(subtitle.getLanguageCode())) {
						parent.setSelection(i);
						continue;
					}
				}
			}
		}
		firstSelection = false;
		video.setSubtitle(subtitle);
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {

	}

}
