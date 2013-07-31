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

package us.nineworlds.serenity.ui.adapters;

import java.util.ArrayList;
import java.util.List;

import us.nineworlds.serenity.R;
import us.nineworlds.serenity.core.model.impl.TVShowSeriesInfo;
import us.nineworlds.serenity.core.services.ShowRetrievalIntentService;
import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Gallery;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author dcarver
 *
 */
public abstract class AbstractSeriesContentInfoAdapter extends
		AbstractContentInfoAdapter {

	protected static List<TVShowSeriesInfo> tvShowList = new ArrayList<TVShowSeriesInfo>();

	/**
	 * @param c
	 * @param key
	 * @param category
	 */
	public AbstractSeriesContentInfoAdapter(Activity c, String key,
			String category) {
		super(c, key, category);
		fetchDataFromService();
	}


	@Override
	public int getCount() {
		return tvShowList.size();
	}


	@Override
	public Object getItem(int position) {
		return tvShowList.get(position);
	}
	
	protected ShowRetrievalHandler getHandlerInstance(){
		return new ShowRetrievalHandler();
	}
	
	protected void fetchDataFromService() {
		handler = getHandlerInstance();
		Messenger messenger = new Messenger(handler);
		Intent intent = new Intent(context, ShowRetrievalIntentService.class);
		intent.putExtra("MESSENGER", messenger);
		intent.putExtra("key", key);
		intent.putExtra("category", category);
		context.startService(intent);
	}

	protected class ShowRetrievalHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {

			tvShowList = (List<TVShowSeriesInfo>) msg.obj;
			Gallery posterGallery = (Gallery) context
					.findViewById(R.id.tvShowBannerGallery);
			if (tvShowList != null) {
				TextView tv = (TextView) context
						.findViewById(R.id.tvShowItemCount);
				if (tvShowList.isEmpty()) {
					Toast.makeText(context, context.getString(R.string.no_shows_found_for_the_category_) + category, Toast.LENGTH_LONG).show();
				}
				tv.setText(Integer.toString(tvShowList.size()) + context.getString(R.string._item_s_));
			}
			notifyDataSetChanged();
			posterGallery.requestFocus();
		}
	}
	

}
