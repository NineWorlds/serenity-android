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

package us.nineworlds.serenity.ui.browser.tv;

import com.jess.ui.TwoWayAdapterView;
import com.jess.ui.TwoWayGridView;
import com.jess.ui.TwoWayAdapterView.OnItemLongClickListener;


import us.nineworlds.serenity.R;
import us.nineworlds.serenity.core.model.SeriesContentInfo;

import android.view.View;
import android.widget.ImageView;

/**
 * A listener that handles long press for video content. Includes displaying a
 * dialog for toggling watched and unwatched status as well for possibly playing
 * on the TV.
 * 
 * @author dcarver
 * 
 */
public class TVShowGridOnItemLongClickListener extends AbstractTVShowOnItemLongClick implements OnItemLongClickListener {

	@Override
	public boolean onItemLongClick(TwoWayAdapterView<?> av, View v,
			int position, long arg3) {

		// Google TV is sending back different results than Nexus 7
		// So we try to handle the different results.
		
		videoInfo = (SeriesContentInfo) av.getItemAtPosition(position);

		if (v == null) {
			TwoWayGridView g = (TwoWayGridView) av;
			view = g.getSelectedView().findViewById(R.id.posterImageView);
		} else {
			if (v instanceof ImageView) {
				view = v;
			} else {
				view = v.findViewById(R.id.posterImageView);
			}
		}

		init();
		
		createAndShowDialog();

		return true;
	}

}
