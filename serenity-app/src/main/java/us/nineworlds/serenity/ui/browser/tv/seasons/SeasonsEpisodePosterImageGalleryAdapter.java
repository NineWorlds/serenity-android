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

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import net.ganin.darv.DpadAwareRecyclerView;

import java.util.List;

import javax.inject.Inject;

import us.nineworlds.plex.rest.PlexappFactory;
import us.nineworlds.serenity.R;
import us.nineworlds.serenity.core.model.VideoContentInfo;
import us.nineworlds.serenity.ui.browser.tv.episodes.EpisodePosterImageGalleryAdapter;
import us.nineworlds.serenity.ui.util.ImageUtils;

public class SeasonsEpisodePosterImageGalleryAdapter extends EpisodePosterImageGalleryAdapter {

    private static SeasonsEpisodePosterImageGalleryAdapter notifyAdapter;

    @Inject
    protected SharedPreferences prefs;

    @Inject
    PlexappFactory plexFactory;

    public SeasonsEpisodePosterImageGalleryAdapter(String key) {
        super(key);
        notifyAdapter = this;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        VideoContentInfo pi = posterList.get(position);
        ImageView mpiv = (ImageView) holder.itemView
                .findViewById(R.id.posterImageView);
        mpiv.setBackgroundResource(R.drawable.gallery_item_background);
        mpiv.setScaleType(ImageView.ScaleType.FIT_XY);
        int width = ImageUtils.getDPI(270, (Activity) mpiv.getContext());
        int height = ImageUtils.getDPI(147, (Activity) mpiv.getContext());
        mpiv.setMaxWidth(width);
        mpiv.setMaxHeight(height);
        mpiv.setLayoutParams(new RelativeLayout.LayoutParams(width, height));
        holder.itemView.setLayoutParams(new DpadAwareRecyclerView.LayoutParams(
                width, height));

        Glide.with(mpiv.getContext()).load(pi.getImageURL()).into(mpiv);

        ImageView watchedView = (ImageView) holder.itemView
                .findViewById(R.id.posterWatchedIndicator);

        if (pi.isPartiallyWatched()) {
            ImageUtils.toggleProgressIndicator(holder.itemView,
                    pi.getResumeOffset(), pi.getDuration());
        } else if (pi.isWatched()) {
            watchedView.setImageResource(R.drawable.overlaywatched);
            watchedView.setVisibility(View.VISIBLE);
        } else {
            watchedView.setVisibility(View.INVISIBLE);
        }

        TextView metaData = (TextView) holder.itemView
                .findViewById(R.id.metaOverlay);
        String metaText = "";
        if (pi.getSeason() != null) {
            metaText = pi.getSeason() + " ";
        }

        if (pi.getEpisode() != null) {
            metaText = metaText + pi.getEpisode();
        }

        if (metaText.length() > 0) {
            metaData.setText(metaText);
            metaData.setVisibility(View.VISIBLE);
        }

        TextView title = (TextView) holder.itemView
                .findViewById(R.id.posterOverlayTitle);
        title.setText(pi.getTitle());
        title.setVisibility(View.VISIBLE);

    }

    @Override
    protected void fetchDataFromService() {
        retrieveEpisodes();
        notifyAdapter = this;
    }

}
