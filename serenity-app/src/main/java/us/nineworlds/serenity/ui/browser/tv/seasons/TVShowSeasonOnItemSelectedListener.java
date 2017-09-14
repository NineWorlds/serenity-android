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
import android.graphics.Bitmap;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import javax.inject.Inject;
import net.ganin.darv.DpadAwareRecyclerView;
import us.nineworlds.serenity.R;
import us.nineworlds.serenity.common.rest.SerenityClient;
import us.nineworlds.serenity.core.imageloader.BackgroundBitmapDisplayer;
import us.nineworlds.serenity.core.model.SeriesContentInfo;
import us.nineworlds.serenity.injection.BaseInjector;

public class TVShowSeasonOnItemSelectedListener extends BaseInjector
    implements DpadAwareRecyclerView.OnItemSelectedListener {

  private final Activity context;
  private SeriesContentInfo info;

  @Inject protected SerenityClient plexFactory;

  public TVShowSeasonOnItemSelectedListener(View bgv, Activity activity) {
    context = activity;
  }

  private void changeBackgroundImage(View v) {

    SeriesContentInfo mi = info;

    if (mi.getBackgroundURL() != null) {
      final View fanArt = context.findViewById(R.id.fanArt);
      String transcodingURL = plexFactory.getImageURL(mi.getBackgroundURL(), 1280, 720);

      SimpleTarget<Bitmap> target = new SimpleTarget<Bitmap>(1280, 720) {
        @Override public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
          context.runOnUiThread(new BackgroundBitmapDisplayer(resource, R.drawable.tvshows, fanArt));
        }
      };

      Glide.with(context).load(transcodingURL).asBitmap().into(target);
    }
  }

  @Override public void onItemSelected(DpadAwareRecyclerView dpadAwareRecyclerView, View view, int i, long l) {
    Activity context = (Activity) view.getContext();
    if (context.isDestroyed()) {
      return;
    }

    TVShowSeasonImageGalleryAdapter adapter = (TVShowSeasonImageGalleryAdapter) dpadAwareRecyclerView.getAdapter();
    if (i < 0) {
      Log.e(TVShowSeasonOnItemSelectedListener.class.getCanonicalName(),
          "Season list size: " + adapter.getItemCount() + " position: " + i);
      i = 0;
    }

    info = (SeriesContentInfo) adapter.getItem(i);
    ImageView mpiv = (ImageView) view.findViewById(R.id.posterImageView);
    DpadAwareRecyclerView episodeGrid = (DpadAwareRecyclerView) context.findViewById(R.id.episodeGridView);

    episodeGrid.setVisibility(View.VISIBLE);
    TVShowSeasonBrowserActivity seasonBrowserActivity = (TVShowSeasonBrowserActivity) context;
    if (seasonBrowserActivity.adapter == null) {
      seasonBrowserActivity.adapter = new SeasonsEpisodePosterImageGalleryAdapter();
      episodeGrid.setAdapter(seasonBrowserActivity.adapter);
    }
    episodeGrid.setLayoutManager(new GridLayoutManager(context, 2, GridLayoutManager.HORIZONTAL, false));
    episodeGrid.setOnItemClickListener(new EpisodePosterOnItemClickListener());
    seasonBrowserActivity.fetchEpisodes(info.getKey());

    TextView seasonsTitle = (TextView) context.findViewById(R.id.tvShowSeasonsTitle);
    seasonsTitle.setText(info.getTitle());

    changeBackgroundImage(mpiv);
  }

  @Override public void onItemFocused(DpadAwareRecyclerView dpadAwareRecyclerView, View view, int i, long l) {

  }
}
