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
import android.content.Intent;
import android.view.View;

import us.nineworlds.serenity.core.model.ContentInfo;
import us.nineworlds.serenity.core.model.SeriesContentInfo;
import us.nineworlds.serenity.core.model.VideoContentInfo;
import us.nineworlds.serenity.ui.adapters.AbstractPosterImageGalleryAdapter;
import us.nineworlds.serenity.ui.browser.tv.episodes.EpisodeBrowserActivity;
import us.nineworlds.serenity.ui.listeners.AbstractVideoOnItemClickListener;

public class TVShowSeasonOnItemClickListener extends AbstractVideoOnItemClickListener {

  private Activity context;

  @Deprecated
  public TVShowSeasonOnItemClickListener(Context c, AbstractPosterImageGalleryAdapter adapter) {
    super(adapter);
    context = (Activity) c;
    this.adapter = adapter;
  }

  public TVShowSeasonOnItemClickListener() {
    super();
  }

  @Override protected VideoContentInfo getVideoInfo(int position) {
    return null;
  }

  @Override
  public void onItemClick(View view, int i) {
    SeriesContentInfo info = (SeriesContentInfo) adapter.getItem(i);
    onItemClick(view, info);
  }

  @Override
  public void onItemClick(View v, ContentInfo item) {
    context = getActivity(v.getContext());
    SeriesContentInfo info = (SeriesContentInfo) item;
    Intent intent = new Intent(context, EpisodeBrowserActivity.class);
    intent.putExtra("key", info.getKey());
    context.startActivityForResult(intent, 0);

  }

}
