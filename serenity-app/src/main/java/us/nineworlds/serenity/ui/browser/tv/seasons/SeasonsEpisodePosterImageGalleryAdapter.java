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

import java.util.List;

import javax.inject.Inject;

import us.nineworlds.plex.rest.PlexappFactory;
import us.nineworlds.plex.rest.model.impl.MediaContainer;
import us.nineworlds.serenity.R;
import us.nineworlds.serenity.core.TrailersYouTubeSearch;
import us.nineworlds.serenity.core.model.DBMetaData;
import us.nineworlds.serenity.core.model.VideoContentInfo;
import us.nineworlds.serenity.core.util.DBMetaDataSource;
import us.nineworlds.serenity.ui.util.ImageUtils;
import us.nineworlds.serenity.volley.DefaultLoggingVolleyErrorListener;
import us.nineworlds.serenity.volley.GridSubtitleVolleyResponseListener;
import us.nineworlds.serenity.volley.SimpleXmlRequest;
import us.nineworlds.serenity.volley.YouTubeTrailerSearchResponseListener;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.youtube.player.YouTubeApiServiceUtil;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.jess.ui.TwoWayAbsListView;

import net.ganin.darv.DpadAwareRecyclerView;

public class SeasonsEpisodePosterImageGalleryAdapter
        extends
        us.nineworlds.serenity.ui.browser.tv.episodes.EpisodePosterImageGalleryAdapter {

    private static SeasonsEpisodePosterImageGalleryAdapter notifyAdapter;
    private DBMetaDataSource datasource;

    @Inject
    protected SharedPreferences prefs;

    @Inject
    PlexappFactory plexFactory;

    public SeasonsEpisodePosterImageGalleryAdapter(Context c, String key) {
        super(c, key);
        notifyAdapter = this;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        VideoContentInfo pi = posterList.get(position);
        gridViewMetaData(holder.itemView, pi);
        ImageView mpiv = (ImageView) holder.itemView
                .findViewById(R.id.posterImageView);
        mpiv.setBackgroundResource(R.drawable.gallery_item_background);
        mpiv.setScaleType(ImageView.ScaleType.FIT_XY);
        int width = ImageUtils.getDPI(270, context);
        int height = ImageUtils.getDPI(147, context);
        mpiv.setMaxWidth(width);
        mpiv.setMaxHeight(height);
        mpiv.setLayoutParams(new RelativeLayout.LayoutParams(width, height));
        holder.itemView.setLayoutParams(new DpadAwareRecyclerView.LayoutParams(
                width, height));

        imageLoader.displayImage(pi.getImageURL(), mpiv);

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
        handler = new EpisodeHandler();
        retrieveEpisodes();
        notifyAdapter = this;
    }

    protected void gridViewMetaData(View galleryCellView, VideoContentInfo pi) {
        checkDataBaseForTrailer(pi);

        boolean trailersEnabled = prefs.getBoolean("episode_trailers", false);
        if (trailersEnabled) {
            if (pi.hasTrailer() == false) {
                if (YouTubeInitializationResult.SUCCESS
                        .equals(YouTubeApiServiceUtil
                                .isYouTubeApiServiceAvailable(context))) {
                    fetchTrailer(pi, galleryCellView);
                }
            } else {
                View v = galleryCellView.findViewById(R.id.infoGraphicMeta);
                v.setVisibility(View.VISIBLE);
                v.findViewById(R.id.trailerIndicator).setVisibility(
                        View.VISIBLE);
            }
        }

        if (pi.getAvailableSubtitles() != null) {
            View v = galleryCellView.findViewById(R.id.infoGraphicMeta);
            v.setVisibility(View.VISIBLE);
            v.findViewById(R.id.subtitleIndicator).setVisibility(View.VISIBLE);
        } else {
            fetchSubtitle(pi, galleryCellView);
        }
    }

    public void fetchSubtitle(VideoContentInfo mpi, View view) {
        String url = plexFactory.getMovieMetadataURL("/library/metadata/"
                + mpi.id());
        SimpleXmlRequest<MediaContainer> xmlRequest = new SimpleXmlRequest<MediaContainer>(
                Request.Method.GET, url, MediaContainer.class,
                new GridSubtitleVolleyResponseListener(mpi, context, view),
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        queue.add(xmlRequest);

    }

    protected void checkDataBaseForTrailer(VideoContentInfo pi) {
        datasource = new DBMetaDataSource(context);
        datasource.open();
        DBMetaData metaData = datasource.findMetaDataByPlexId(pi.id());
        if (metaData != null) {
            pi.setTrailer(true);
            pi.setTrailerId(metaData.getYouTubeID());
        }
        datasource.close();
    }

    public void fetchTrailer(VideoContentInfo mpi, View view) {

        TrailersYouTubeSearch trailerSearch = new TrailersYouTubeSearch();
        String queryURL = trailerSearch.queryURL(mpi);

        volley.volleyJSonGetRequest(queryURL,
                new YouTubeTrailerSearchResponseListener(view, mpi),
                new DefaultLoggingVolleyErrorListener());
    }

    private static class EpisodeHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            posterList = (List<VideoContentInfo>) msg.obj;
            notifyAdapter.notifyDataSetChanged();
        }
    }
}
