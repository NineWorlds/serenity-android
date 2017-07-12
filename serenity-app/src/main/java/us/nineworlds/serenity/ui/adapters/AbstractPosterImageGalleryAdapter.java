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

package us.nineworlds.serenity.ui.adapters;

import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import us.nineworlds.serenity.R;
import us.nineworlds.serenity.core.model.VideoContentInfo;
import us.nineworlds.serenity.injection.InjectingRecyclerViewAdapter;
import us.nineworlds.serenity.ui.util.ImageUtils;

/**
 * An abstract class for handling the creation of video content for use during
 * browsing. Implementations need to implement the abstract methods to provide
 * functionality for retrieval and display of video content when browsing the
 * episodes.
 *
 * @author dcarver
 *
 */
public abstract class AbstractPosterImageGalleryAdapter extends InjectingRecyclerViewAdapter {

    protected static List<VideoContentInfo> posterList = null;
    protected Handler handler;
    protected String key;
    protected String category;

    public AbstractPosterImageGalleryAdapter(String key) {
        posterList = new ArrayList<>();
        this.key = key;
        fetchDataFromService();
    }

    public AbstractPosterImageGalleryAdapter(String key, String category) {
        this.key = key;
        this.category = category;
        posterList = new ArrayList<>();

        fetchDataFromService();
    }

    protected abstract void fetchDataFromService();

    @Override
    public int getItemCount() {
        return posterList.size();
    }

    public Object getItem(int position) {
        if (position > posterList.size()) {
            return null;
        }
        try {
            return posterList.get(position);
        } catch (IndexOutOfBoundsException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public List<VideoContentInfo> getItems() {
        return posterList;
    }

    public void setWatchedStatus(View galleryCellView, VideoContentInfo pi) {
        ImageView watchedView = (ImageView) galleryCellView
                .findViewById(R.id.posterWatchedIndicator);

        if (pi.isPartiallyWatched()) {
            ImageUtils.toggleProgressIndicator(galleryCellView,
                    pi.getResumeOffset(), pi.getDuration());
        } else if (pi.isWatched()) {
            watchedView.setImageResource(R.drawable.overlaywatched);
            watchedView.setVisibility(View.VISIBLE);
        } else {
            watchedView.setVisibility(View.INVISIBLE);
        }
    }

}
