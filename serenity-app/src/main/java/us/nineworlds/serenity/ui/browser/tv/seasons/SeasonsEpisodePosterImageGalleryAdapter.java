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

package us.nineworlds.serenity.ui.browser.tv.seasons;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import us.nineworlds.serenity.R;
import us.nineworlds.serenity.core.model.VideoContentInfo;
import us.nineworlds.serenity.ui.browser.tv.episodes.EpisodePosterImageGalleryAdapter;
import us.nineworlds.serenity.ui.browser.tv.episodes.EpisodeViewHolder;

public class SeasonsEpisodePosterImageGalleryAdapter extends EpisodePosterImageGalleryAdapter {

  @Override public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
    EpisodeViewHolder episodeViewHolder = (EpisodeViewHolder) holder;

    VideoContentInfo pi = posterList.get(position);

    episodeViewHolder.reset();
    episodeViewHolder.createImage(pi, 270, 147);
    episodeViewHolder.toggleWatchedIndicator(pi);
    episodeViewHolder.updateSeasonsTitle(pi);
    episodeViewHolder.itemView.setOnClickListener((view -> onItemViewClick(view, position)));
    episodeViewHolder.itemView.setOnFocusChangeListener((view, focus) -> onItemViewFocusChanged(focus, view, position));
  }

  @Override public void onItemViewClick(View view, int i) {
    if (onItemClickListener == null) {
      onItemClickListener = new EpisodePosterOnItemClickListener(this);
    }
    onItemClickListener.onItemClick(view, i);
  }

  @Override public void onItemViewFocusChanged(boolean hasFocus, View view, int i) {
    view.clearAnimation();
    view.setBackground(null);

    if (hasFocus && view != null) {
      view.clearAnimation();
      view.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.rounded_transparent_border));
      zoomOut(view);
      return;
    }
  }
}
