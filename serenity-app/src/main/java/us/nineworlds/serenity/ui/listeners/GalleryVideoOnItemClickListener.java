/**
 * The MIT License (MIT)
 * Copyright (c) 2013 David Carver
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

import us.nineworlds.serenity.R;
import us.nineworlds.serenity.core.model.VideoContentInfo;
import us.nineworlds.serenity.widgets.SerenityAdapterView;
import us.nineworlds.serenity.widgets.SerenityAdapterView.OnItemClickListener;

import android.view.View;

/**
 * @author dcarver
 * 
 */
public class GalleryVideoOnItemClickListener extends AbstractVideoOnItemClickListener implements OnItemClickListener {

	@Override
	public void onItemClick(SerenityAdapterView<?> av, View v, int arg2,
			long arg3) {
		videoInfo = (VideoContentInfo) av.getItemAtPosition(arg2);
		onItemClick(v.findViewById(R.id.posterImageView));
	}

}
