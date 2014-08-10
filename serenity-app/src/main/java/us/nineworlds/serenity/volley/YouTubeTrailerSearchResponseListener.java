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

package us.nineworlds.serenity.volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import us.nineworlds.serenity.R;
import us.nineworlds.serenity.core.model.DBMetaData;
import us.nineworlds.serenity.core.model.VideoContentInfo;
import us.nineworlds.serenity.core.util.DBMetaDataSource;
import us.nineworlds.serenity.ui.util.ImageUtils;
import android.app.Activity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.android.volley.Response;

/**
 * @author davidcarver
 *
 */
public class YouTubeTrailerSearchResponseListener implements
Response.Listener<JSONObject> {

	View posterView;
	VideoContentInfo video;

	public YouTubeTrailerSearchResponseListener(View posterView,
			VideoContentInfo video) {
		this.posterView = posterView;
		this.video = video;
	}

	@Override
	public void onResponse(JSONObject response) {
		try {
			JSONArray items = response.getJSONArray("items");
			View trailerIndicator = posterView
					.findViewById(R.id.trailerIndicator);
			View infoGraphicMeta = posterView
					.findViewById(R.id.infoGraphicMeta);

			JSONObject item = items.getJSONObject(0);
			JSONObject youtubeId = item.getJSONObject("id");

			String id = youtubeId.getString("videoId");

			createMetaData(id);
			if (trailerIndicator != null) {
				trailerIndicator.setVisibility(View.VISIBLE);
				infoGraphicMeta.setVisibility(View.VISIBLE);
			} else {
				Activity context = (Activity) posterView.getContext();
				LinearLayout infographicsView = (LinearLayout) context
						.findViewById(R.id.movieInfoGraphicLayout);
				ImageView ytImage = new ImageView(context);
				ytImage.setImageResource(R.drawable.yt_social_icon_red_128px);
				ytImage.setScaleType(ScaleType.FIT_XY);
				int w = ImageUtils.getDPI(45, context);
				int h = ImageUtils.getDPI(24, context);
				ytImage.setLayoutParams(new LinearLayout.LayoutParams(w, h));
				LinearLayout.LayoutParams p = (LinearLayout.LayoutParams) ytImage
						.getLayoutParams();
				p.leftMargin = 5;
				p.gravity = Gravity.CENTER_VERTICAL;
				infographicsView.addView(ytImage);
			}

			video.setTrailer(true);
			video.setTrailerId(id);
		} catch (JSONException e) {
			Log.d(getClass().getName(), e.getMessage(), e);
		}
	}

	/**
	 * @param yt
	 */
	protected void createMetaData(String id) {
		DBMetaDataSource datasource = new DBMetaDataSource(
				posterView.getContext());
		datasource.open();
		DBMetaData metaData = datasource.findMetaDataByPlexId(video.id());
		if (metaData == null) {
			datasource.createMetaData(id, video.id());
		}
		datasource.close();
	}

}
