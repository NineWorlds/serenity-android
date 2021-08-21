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

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
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

/**
 * Implementation of the Poster Image Gallery class for TV Shows.
 *
 * @author dcarver
 */
public class EpisodePosterImageGalleryAdapter extends AbstractPosterImageGalleryAdapter {

  private RecyclerView recyclerView;

  @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view =
        LayoutInflater.from(parent.getContext()).inflate(R.layout.poster_indicator_view, null);
    return new EpisodeViewHolder(view);
  }

  @Override
  public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
    super.onAttachedToRecyclerView(recyclerView);
    this.recyclerView = recyclerView;
  }

  @Override public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
    VideoContentInfo pi = posterList.get(position);

    EpisodeViewHolder viewHolder = (EpisodeViewHolder) holder;

    Context context = viewHolder.getContext();
    int width = context.getResources().getDimensionPixelSize(R.dimen.episode_image_width);
    int height = context.getResources().getDimensionPixelSize(R.dimen.episode_image_height);

    viewHolder.reset();
    viewHolder.createImage(pi, width, height, recyclerView);
    viewHolder.toggleWatchedIndicator(pi);
    viewHolder.getItemView().setOnClickListener((view)-> onItemViewClick(view, position));
    viewHolder.getItemView().setOnLongClickListener((view) -> onItemViewLongClick(view, position));
    viewHolder.getItemView().setOnFocusChangeListener((view, focus)-> onItemViewFocusChanged(focus, view, position));
    viewHolder.getItemView().setOnKeyListener(this);
  }

  public void updateEpisodes(List<VideoContentInfo> episodes) {
    List<VideoContentInfo> oldList = new ArrayList(posterList);
    posterList.clear();
    posterList.addAll(episodes);
   // new RecyclerViewDiffUtil(oldList, posterList).dispatchUpdatesTo(this);
    notifyDataSetChanged();
  }

  public void onItemViewClick(View view, int i) {
    onItemClickListener.onItemClick(view, i);
  }

  public void onItemViewFocusChanged(boolean hasFocus, View view, int i) {
    view.clearAnimation();
    view.setBackground(null);

    if (triggerFocusSelection) {
      if (hasFocus) {
        view.clearAnimation();
        view.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.rounded_transparent_border));
        zoomOut(view);
        getOnItemSelectedListener().onItemSelected(view, i);
      }
    }
  }

  protected boolean onItemViewLongClick(View view, int position) {
    GalleryVideoOnItemLongClickListener onItemLongClickListener = new GalleryVideoOnItemLongClickListener(this);
    onItemLongClickListener.setPosition(position);
    return onItemLongClickListener.onLongClick(view);
  }

}
