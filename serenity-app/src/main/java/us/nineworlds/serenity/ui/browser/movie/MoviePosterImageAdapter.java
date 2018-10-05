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

package us.nineworlds.serenity.ui.browser.movie;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;
import us.nineworlds.serenity.R;
import us.nineworlds.serenity.core.model.VideoContentInfo;
import us.nineworlds.serenity.ui.adapters.AbstractPosterImageGalleryAdapter;
import us.nineworlds.serenity.ui.listeners.AbstractVideoOnItemClickListener;
import us.nineworlds.serenity.ui.listeners.AbstractVideoOnItemSelectedListener;
import us.nineworlds.serenity.ui.listeners.GalleryVideoOnItemClickListener;
import us.nineworlds.serenity.ui.views.statusoverlayview.StatusOverlayFrameLayout;

public class MoviePosterImageAdapter extends AbstractPosterImageGalleryAdapter {

  @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    StatusOverlayFrameLayout view = (StatusOverlayFrameLayout) LayoutInflater.from(parent.getContext())
        .inflate(R.layout.movie_status_overlay, parent, false);
    return new MoviePosterViewHolder(view);
  }

  @Override public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
    StatusOverlayFrameLayout overlayView = (StatusOverlayFrameLayout) holder.itemView;

    VideoContentInfo pi = posterList.get(position);
    overlayView.setTag(pi);
    overlayView.reset();
    overlayView.createImage(pi, 130, 200);
    overlayView.toggleWatchedIndicator(pi);
  }

  protected void populatePosters(List<VideoContentInfo> videos) {
    posterList = videos;
    notifyDataSetChanged();
  }

  @Override public void onItemViewClick(View view, int i) {
    getOnItemClickListener().onItemClick(view, i);
  }

  @Override public void onItemViewFocusChanged(boolean hasFocus, View view, int i) {
    if (hasFocus && view != null) {
      getOnItemSelectedListener().onItemSelected(view, i);
    }
  }
}
