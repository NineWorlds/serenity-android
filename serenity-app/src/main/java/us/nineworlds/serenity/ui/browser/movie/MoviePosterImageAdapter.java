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

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;
import us.nineworlds.serenity.R;
import us.nineworlds.serenity.core.model.VideoContentInfo;
import us.nineworlds.serenity.ui.adapters.AbstractPosterImageGalleryAdapter;
import us.nineworlds.serenity.ui.adapters.RecyclerViewDiffUtil;
import us.nineworlds.serenity.ui.listeners.GalleryVideoOnItemLongClickListener;
import us.nineworlds.serenity.ui.views.statusoverlayview.StatusOverlayFrameLayout;
import us.nineworlds.serenity.ui.views.viewholders.AbstractPosterImageViewHolder;

public class MoviePosterImageAdapter extends AbstractPosterImageGalleryAdapter {

  @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    StatusOverlayFrameLayout view = (StatusOverlayFrameLayout) LayoutInflater.from(parent.getContext())
        .inflate(R.layout.movie_status_overlay, parent, false);
    return new MoviePosterViewHolder(view);
  }

  @Override public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
    StatusOverlayFrameLayout overlayView = (StatusOverlayFrameLayout) holder.itemView;
    AbstractPosterImageViewHolder viewHolder = (AbstractPosterImageViewHolder) holder;
    viewHolder.setZoomedOut(false);

    VideoContentInfo pi = posterList.get(position);
    overlayView.setTag(pi);
    overlayView.reset();
    overlayView.createImage(pi, 130, 200);
    overlayView.toggleWatchedIndicator(pi);
    overlayView.setClickable(true);
    overlayView.setOnClickListener((view) -> onItemViewClick(view, position));
    overlayView.setOnLongClickListener((view -> onItemViewLongClick(view, position)));
    overlayView.setOnFocusChangeListener((view, hasFocus) -> onItemViewFocusChanged(hasFocus, view, position));
    overlayView.setOnKeyListener(this);

  }

  private boolean onItemViewLongClick(View view, int position) {
    GalleryVideoOnItemLongClickListener onItemLongClickListener = new GalleryVideoOnItemLongClickListener(this);
    onItemLongClickListener.setPosition(position);
    return onItemLongClickListener.onLongClick(view);
  }

  protected void populatePosters(List<VideoContentInfo> videos) {
    List<VideoContentInfo> oldList = new ArrayList<>(posterList);
    posterList.clear();
    posterList.addAll(videos);
    new RecyclerViewDiffUtil(oldList, posterList).dispatchUpdatesTo(this);
  }

  public void onItemViewClick(View view, int i) {
    getOnItemClickListener().onItemClick(view, i);
  }

  public void onItemViewFocusChanged(boolean hasFocus, View view, int i) {
    view.clearAnimation();
    view.setBackground(null);

    if (triggerFocusSelection) {
      if (hasFocus) {
        view.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.rounded_transparent_border));
        zoomOut(view);
        getOnItemSelectedListener().onItemSelected(view, i);
      }
    }
  }
}
