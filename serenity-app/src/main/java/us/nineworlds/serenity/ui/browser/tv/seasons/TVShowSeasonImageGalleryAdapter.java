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

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import us.nineworlds.plex.rest.PlexappFactory;
import us.nineworlds.serenity.R;
import us.nineworlds.serenity.core.model.SeriesContentInfo;
import us.nineworlds.serenity.injection.InjectingRecyclerViewAdapter;

/**
 * An adapter that handles the population of views for TV Show Seasons.
 * <p>
 * The fetching of the data is handled by a RESTFul Volley call to plex and then
 * the data is parsed and returned.
 *
 * @author dcarver
 */
public class TVShowSeasonImageGalleryAdapter extends InjectingRecyclerViewAdapter {

    private List<SeriesContentInfo> seasonList = null;

    @Inject
    PlexappFactory plexFactory;

    public TVShowSeasonImageGalleryAdapter() {
        super();
        seasonList = new ArrayList<>();
    }

    public Object getItem(int position) {
        if (seasonList.isEmpty()) {
            return null;
        }
        return seasonList.get(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.poster_tvshow_indicator_view, null);
        SeasonViewHolder seasonViewHolder = new SeasonViewHolder(view);
        return seasonViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        SeasonViewHolder viewHolder = (SeasonViewHolder) holder;

        SeriesContentInfo pi = seasonList.get(position);

        viewHolder.reset();
        viewHolder.createImage(pi, 120, 180);
        viewHolder.toggleWatchedIndicator(pi);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return seasonList.size();
    }

    public void updateSeasonsList(List<SeriesContentInfo> seasonList) {
        this.seasonList = seasonList;
        notifyDataSetChanged();
    }

}
