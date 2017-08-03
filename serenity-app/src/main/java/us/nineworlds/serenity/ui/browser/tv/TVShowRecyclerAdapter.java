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

package us.nineworlds.serenity.ui.browser.tv;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;
import us.nineworlds.serenity.R;
import us.nineworlds.serenity.core.model.SeriesContentInfo;
import us.nineworlds.serenity.ui.adapters.AbstractPosterImageGalleryAdapter;

public class TVShowRecyclerAdapter extends AbstractPosterImageGalleryAdapter {

  private static final int BANNER_PIXEL_HEIGHT = 140;
  private static final int BANNER_PIXEL_WIDTH = 758;

  protected List<SeriesContentInfo> tvShowList = new ArrayList<>();

  public int getItemCount() {
    return tvShowList.size();
  }

  @Override public Object getItem(int position) {
    if (position < 0) {
      position = 0;
    }
    return tvShowList.get(position);
  }

  @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.poster_tvshow_indicator_view, parent, false);
    return new TVShowViewHolder(view);
  }

  @Override public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
    SeriesContentInfo pi = tvShowList.get(position);
    TVShowViewHolder tvShowViewHolder = (TVShowViewHolder) holder;

    tvShowViewHolder.reset();
    tvShowViewHolder.createImage(pi, BANNER_PIXEL_WIDTH, BANNER_PIXEL_HEIGHT);
    tvShowViewHolder.toggleWatchedIndicator(pi);
  }

  @Override public long getItemId(int position) {
    return position;
  }

  public void updateSeries(List<SeriesContentInfo> series) {
    tvShowList = series;
    notifyDataSetChanged();
  }
}
