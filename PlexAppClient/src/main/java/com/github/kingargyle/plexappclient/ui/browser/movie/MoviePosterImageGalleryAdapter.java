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

package com.github.kingargyle.plexappclient.ui.browser.movie;

import java.util.ArrayList;
import java.util.List;

import com.github.kingargyle.plexappclient.R;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.RadioGroup.LayoutParams;

/**
 * @author dcarver
 *
 */
public class MoviePosterImageGalleryAdapter extends BaseAdapter {
	
	private List<MoviePostersInfo> posterList = null;
	private Context context;
	
	
	public MoviePosterImageGalleryAdapter(Context c) {
		context = c;
		
		posterList = new ArrayList<MoviePostersInfo>();
		
		createTestingPosters();
		
	}
	
	private void createTestingPosters() {
		
		int[] postersId = {R.drawable.poster1, R.drawable.poster2};
		int[] backgroundId = {R.drawable.poster1_background, R.drawable.poster2_background};
		String[] plotSummary = {
				"After total humiliation at her thirteenth birthday party, Jenna Rink wants to just hide until she's thirty. Thanks to some wishing dust, Jenna's prayer has been answered. With a knockout body, a dream apartment, a fabulous wardrobe, an athlete boyfriend, a dream job, and superstar friends, this can't be a better life. Unfortunetly, Jenna realizes that this is not what she wanted. The only one that she needs is her childhood best friend, Matt, a boy that she thought destroyed her party. But when she finds him, he's a grown up, and not the same person that she knew.",
				"2 Fast 2 Furious is a 2003 American street racing action film directed by John Singleton. It is the second film in The Fast and the Furious film series following The Fast and the Furious (2001). It stars Paul Walker, Tyrese Gibson, Eva Mendes, Devon Aoki, and Chris Bridges; and was directed by John Singleton. The soundtrack was composed by David Arnold. It is the only film in the series to not feature Vin Diesel in the cast, though his character Dominic Toretto is referenced in the film. Paul Walker returns as ex-cop Brian O'Conner who teams up with his ex-con pal Roman Pearce (Gibson). The duo transport a shipment of dirty money for shady Miami-based import-export dealer Carter Verone (Cole Hauser), while working with undercover agent Monica Fuentes (Mendes) to bring Verone down."
		};
		String[] castInfo = {
				"Director: Gary Winick\r\nCast: Jennifer Garner, Mark Ruffalo, Judy Greer",
				"Director: John Singleton\r\nCast: James Remar, Cole Hauser, Amaury Nolasco"
		};
		
		for (int index=0; index < postersId.length; index++) {
			MoviePostersInfo mpi = new MoviePostersInfo();
			mpi.setBackgroundId(backgroundId[index]);
			mpi.setPosterId(postersId[index]);
			mpi.setPlotSummary(plotSummary[index]);
			mpi.setCastInfo(castInfo[index]);
			posterList.add(mpi);
		}
		
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getCount()
	 */
	public int getCount() {
		
		return posterList.size();
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItem(int)
	 */
	public Object getItem(int position) {
		
		return posterList.get(position);
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItemId(int)
	 */
	public long getItemId(int position) {
		return position;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	public View getView(int position, View convertView, ViewGroup parent) {
		
		MoviePostersInfo pi = posterList.get(position);
		MoviePosterImageView mpiv = new MoviePosterImageView(context, pi);
		mpiv.setImageResource(pi.getPosterId());
		mpiv.setScaleType(ImageView.ScaleType.FIT_XY);
		mpiv.setLayoutParams(new Gallery.LayoutParams(150, LayoutParams.WRAP_CONTENT));
		return mpiv;
	}

}
