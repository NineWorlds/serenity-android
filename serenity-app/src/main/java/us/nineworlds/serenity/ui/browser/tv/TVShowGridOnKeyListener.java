/**
 * The MIT License (MIT)
 * Copyright (c) 2014 David Carver and others
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

package us.nineworlds.serenity.ui.browser.tv;

import android.app.Activity;
import androidx.recyclerview.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import us.nineworlds.serenity.core.model.SeriesContentInfo;
import us.nineworlds.serenity.injection.BaseInjector;
import us.nineworlds.serenity.ui.adapters.AbstractPosterImageGalleryAdapter;

import static android.view.View.OnKeyListener;

public class TVShowGridOnKeyListener extends BaseInjector implements OnKeyListener {

  @Override public boolean onKey(View v, int keyCode, KeyEvent event) {
    if ((keyCode != KeyEvent.KEYCODE_MEDIA_PLAY && keyCode != KeyEvent.KEYCODE_BUTTON_R1)
        || event.getAction() == KeyEvent.ACTION_DOWN) {
      return false;
    }

    final SeriesContentInfo info = getSelectedSeries(v);
    new FindUnwatchedAsyncTask((Activity) v.getContext()).execute(info);
    return true;
  }

  protected SeriesContentInfo getSelectedSeries(View v) {
    final RecyclerView gridView = (RecyclerView) v;
    AbstractPosterImageGalleryAdapter adapter =
        (AbstractPosterImageGalleryAdapter) gridView.getAdapter();
    int pos = gridView.getChildAdapterPosition(gridView.getFocusedChild());
    final SeriesContentInfo info = (SeriesContentInfo) adapter.getItem(pos);
    return info;
  }
}
