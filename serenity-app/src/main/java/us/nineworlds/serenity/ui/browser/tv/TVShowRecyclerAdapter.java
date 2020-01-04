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

import android.content.Context;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;
import us.nineworlds.serenity.R;
import us.nineworlds.serenity.core.model.SeriesContentInfo;
import us.nineworlds.serenity.ui.adapters.AbstractPosterImageGalleryAdapter;
import us.nineworlds.serenity.ui.adapters.RecyclerViewDiffUtil;

@Deprecated
public class TVShowRecyclerAdapter extends AbstractPosterImageGalleryAdapter {

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

    Context context = tvShowViewHolder.getContext();
    int width = context.getResources().getDimensionPixelSize(R.dimen.banner_image_width);
    int height = context.getResources().getDimensionPixelSize(R.dimen.banner_image_height);

    tvShowViewHolder.reset();
    tvShowViewHolder.createImage(pi, width, height, false);
    tvShowViewHolder.toggleWatchedIndicator(pi);
    tvShowViewHolder.getItemView().setOnClickListener((view -> onItemViewClick(view, position)));
    tvShowViewHolder.getItemView().setOnFocusChangeListener((view, focus) -> onItemViewFocusChanged(focus, view, position));
    tvShowViewHolder.getItemView().setOnLongClickListener((view) -> onItemViewLongClick(view, position));
    tvShowViewHolder.getItemView().setOnKeyListener(this);
  }

  protected boolean onItemViewLongClick(View view, int position) {
    ShowOnItemLongClickListener onItemLongClickListener = new ShowOnItemLongClickListener(this);
    onItemLongClickListener.setPosition(position);
    return onItemLongClickListener.onLongClick(view);
  }

  @Override public long getItemId(int position) {
    return position;
  }

  public void updateSeries(List<SeriesContentInfo> series) {
    List<SeriesContentInfo> oldList = new ArrayList(tvShowList);
    tvShowList.clear();
    tvShowList.addAll(series);
    new RecyclerViewDiffUtil(oldList, tvShowList).dispatchUpdatesTo(this);
  }

  public void onItemViewClick(View view, int i) {
    onItemClickListener.onItemClick(view, i);
  }

  public void onItemViewFocusChanged(boolean hasFocus, View view, int i) {
    view.clearAnimation();
    view.setBackground(null);

    if (triggerFocusSelection) {
      if (hasFocus) {
        view.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.rounded_transparent_border));
        getOnItemSelectedListener().onItemSelected(view, i);
        return;
      }
    }
  }
}
