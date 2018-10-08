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

package us.nineworlds.serenity.ui.browser.tv.episodes;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;
import us.nineworlds.serenity.R;
import us.nineworlds.serenity.core.model.VideoContentInfo;
import us.nineworlds.serenity.ui.adapters.AbstractPosterImageGalleryAdapter;
import us.nineworlds.serenity.ui.adapters.RecyclerViewDiffUtil;

/**
 * Implementation of the Poster Image Gallery class for TV Shows.
 *
 * @author dcarver
 */
public class EpisodePosterImageGalleryAdapter extends AbstractPosterImageGalleryAdapter {

  @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view =
        LayoutInflater.from(parent.getContext()).inflate(R.layout.poster_indicator_view, null);
    return new EpisodeViewHolder(view);
  }

  @Override public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
    VideoContentInfo pi = posterList.get(position);

    EpisodeViewHolder viewHolder = (EpisodeViewHolder) holder;

    viewHolder.reset();
    viewHolder.createImage(pi, 300, 187);
    viewHolder.toggleWatchedIndicator(pi);
  }

  public void updateEpisodes(List<VideoContentInfo> episodes) {
    List<VideoContentInfo> oldList = new ArrayList<>(posterList);
    posterList.clear();
    posterList.addAll(episodes);

    new RecyclerViewDiffUtil(oldList, posterList).dispatchUpdatesTo(this);
  }

  @Override public void onItemViewClick(View view, int i) {
    onItemClickListener.onItemClick(view, i);
  }

  @Override public void onItemViewFocusChanged(boolean hasFocus, View view, int i) {
    if (hasFocus) {
      onItemSelectedListener.onItemSelected(view, i);
    }
  }
}
