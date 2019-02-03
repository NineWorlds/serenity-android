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

package us.nineworlds.serenity.ui.leanback.search;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.support.v17.leanback.app.SearchSupportFragment;
import android.support.v17.leanback.app.SearchSupportFragment.SearchResultProvider;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.HeaderItem;
import android.support.v17.leanback.widget.ListRow;
import android.support.v17.leanback.widget.ListRowPresenter;
import android.support.v17.leanback.widget.ObjectAdapter;
import android.support.v17.leanback.widget.OnItemViewSelectedListener;
import android.support.v17.leanback.widget.Presenter.ViewHolder;
import android.support.v17.leanback.widget.Row;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import java.net.URLEncoder;
import java.util.List;
import javax.inject.Inject;
import toothpick.Toothpick;
import us.nineworlds.serenity.R;
import us.nineworlds.serenity.common.annotations.InjectionConstants;
import us.nineworlds.serenity.common.rest.SerenityClient;
import us.nineworlds.serenity.core.imageloader.BackgroundBitmapDisplayer;
import us.nineworlds.serenity.core.menus.MenuItem;
import us.nineworlds.serenity.core.model.VideoContentInfo;
import us.nineworlds.serenity.core.services.MovieSearchIntentService;
import us.nineworlds.serenity.core.util.AndroidHelper;
import us.nineworlds.serenity.ui.util.VideoPlayerIntentUtils;

public class MovieSearchFragment extends SearchSupportFragment implements SearchResultProvider {

  private ArrayObjectAdapter rowsAdapter;
  private List<MenuItem> menuItems;
  private String key;

  private Handler searchHandler;

  @Inject VideoPlayerIntentUtils vpUtils;
  @Inject SerenityClient plexFactory;
  @Inject AndroidHelper androidHelper;

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Toothpick.inject(this, Toothpick.openScope(InjectionConstants.APPLICATION_SCOPE));

    if (androidHelper.isAmazonFireTV()) {
      setSpeechRecognitionCallback(() -> {
        // DO NOTHING;
      });
    }

    rowsAdapter = new ArrayObjectAdapter(new ListRowPresenter());
    setSearchResultProvider(this);
    setOnItemViewClickedListener((itemViewHolder, item, rowViewHolder, row) -> {
      Activity activity = getActivity();
      VideoContentInfo video = (VideoContentInfo) item;
      vpUtils.playVideo(activity, video, false);
    });

    setOnItemViewSelectedListener(new OnItemViewSelectedListener() {

      private VideoContentInfo video;

      @Override public void onItemSelected(ViewHolder arg0, Object item,
          android.support.v17.leanback.widget.RowPresenter.ViewHolder arg2, Row arg3) {
        if (item == null) {
          return;
        }

        video = (VideoContentInfo) item;
        changeBackgroundImage();
      }

      public void changeBackgroundImage() {

        if (video.getBackgroundURL() == null) {
          return;
        }

        final View fanArt = getActivity().findViewById(R.id.fanArt);

        String transcodingURL = plexFactory.createImageURL(video.getBackgroundURL(), 1280, 720);

        SimpleTarget<Bitmap> target = new SimpleTarget<Bitmap>(1280, 720) {
          @Override public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
            getActivity().runOnUiThread(new BackgroundBitmapDisplayer(resource, R.drawable.movies, fanArt));
          }
        };

        Glide.with(getContext()).load(transcodingURL).asBitmap().into(target);
      }
    });

    setBadgeDrawable(getResources().getDrawable(R.drawable.androidtv_icon_mono));
  }

  private void queryByWords(String words) {
    rowsAdapter.clear();
    if (!TextUtils.isEmpty(words)) {

      searchHandler = new MovieSearchHandler();
      Messenger messenger = new Messenger(searchHandler);

      Intent searchIntent = new Intent(getActivity(), MovieSearchIntentService.class);

      searchIntent.putExtra("key", key);
      searchIntent.putExtra("query", URLEncoder.encode(words));
      searchIntent.putExtra("MESSENGER", messenger);
      getActivity().startService(searchIntent);
    }
  }

  @Override public ObjectAdapter getResultsAdapter() {
    return rowsAdapter;
  }

  @Override public boolean onQueryTextChange(String newQuery) {
    queryByWords(newQuery);
    return true;
  }

  @Override public boolean onQueryTextSubmit(String query) {
    queryByWords(query);
    return true;
  }

  protected void loadRows(List<VideoContentInfo> videos) {
    ArrayObjectAdapter listRowAdapter = new ArrayObjectAdapter(new CardPresenter(getContext()));
    for (VideoContentInfo video : videos) {
      listRowAdapter.add(video);
    }

    HeaderItem header = new HeaderItem(0, "Search Results");
    rowsAdapter.add(new ListRow(header, listRowAdapter));
  }

  protected class MovieSearchHandler extends Handler {

    @Override public void handleMessage(Message msg) {
      if (msg.obj != null) {
        List<VideoContentInfo> videos = (List<VideoContentInfo>) msg.obj;
        if (videos != null && videos.isEmpty()) {
          Toast.makeText(getActivity(), R.string.no_videos_found_that_match_the_search_criteria, Toast.LENGTH_LONG)
              .show();
          getActivity().finish();
        }
        loadRows(videos);
      }
    }
  }
}
