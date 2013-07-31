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

package us.nineworlds.serenity.ui.listeners;



import us.nineworlds.serenity.ui.views.SerenityPosterImageView;
import us.nineworlds.serenity.widgets.SerenityAdapterView;
import us.nineworlds.serenity.widgets.SerenityAdapterView.OnItemLongClickListener;
import us.nineworlds.serenity.widgets.SerenityGallery;
import android.view.View;

/**
 * A listener that handles long press for video content in Poster Gallery classes.
 * 
 * @author dcarver
 * 
 */
public class GalleryVideoOnItemLongClickListener extends AbstractVideoOnItemLongClickListener implements
		OnItemLongClickListener {


	@Override
	public boolean onItemLongClick(SerenityAdapterView<?> av, View v,
			int position, long arg3) {

		// Google TV is sending back different results than Nexus 7
		// So we try to handle the different results.

		if (v == null) {
			SerenityGallery g = (SerenityGallery) av;
			vciv = (SerenityPosterImageView) g.getSelectedView();
		} else {
			if (v instanceof SerenityPosterImageView) {
				vciv = (SerenityPosterImageView) v;
			} else {
				SerenityGallery g = (SerenityGallery) v;
				vciv = (SerenityPosterImageView) g.getSelectedView();
			}
		}

		return onItemLongClick();
		
	}
}
