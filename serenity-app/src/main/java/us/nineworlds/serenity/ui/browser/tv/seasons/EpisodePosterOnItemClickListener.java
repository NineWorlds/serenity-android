/**
 * The MIT License (MIT)
 * Copyright (c) 2013 David Carver
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

import android.view.View;

import us.nineworlds.serenity.core.model.ContentInfo;
import us.nineworlds.serenity.core.model.VideoContentInfo;
import us.nineworlds.serenity.ui.adapters.AbstractPosterImageGalleryAdapter;
import us.nineworlds.serenity.ui.listeners.AbstractVideoOnItemClickListener;

public class EpisodePosterOnItemClickListener extends AbstractVideoOnItemClickListener {

  public EpisodePosterOnItemClickListener(AbstractPosterImageGalleryAdapter adapter) {
    super(adapter);
  }

  @Override protected VideoContentInfo getVideoInfo(int position) {
    return (VideoContentInfo) adapter.getItem(position);
  }

  @Override
  public void onItemClick(View view, int i) {
    videoInfo = getVideoInfo(i);
    onItemClick(view, videoInfo);
  }

  @Override
  public void onItemClick(View v, ContentInfo item) {
    videoInfo = (VideoContentInfo) item;
    onItemClick(v, item);
  }
}
