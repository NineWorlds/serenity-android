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

package us.nineworlds.serenity.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import us.nineworlds.serenity.R;
import us.nineworlds.serenity.ui.browser.movie.MovieGridPosterOnItemSelectedListener;
import us.nineworlds.serenity.ui.browser.movie.MoviePosterImageAdapter;
import us.nineworlds.serenity.ui.listeners.AbstractVideoOnItemSelectedListener;
import us.nineworlds.serenity.ui.recyclerview.FocusableGridLayoutManager;

public class VideoGridFragment extends MovieVideoGalleryFragment {

  public VideoGridFragment() {
    super();
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflateView(inflater, container);
    setupRecyclerView();
    return view;
  }

  @Override
  protected void setupRecyclerView() {
    super.setupRecyclerView();
    recyclerView.setNumRows(3);
  }

  @Override protected View inflateView(LayoutInflater inflater, ViewGroup container) {
    return inflater.inflate(R.layout.video_grid_fragment, null);
  }

  protected LinearLayoutManager createLayoutManager() {
    GridLayoutManager layoutManager =
        new FocusableGridLayoutManager(getActivity(), 3,
            GridLayoutManager.HORIZONTAL, false);
    return layoutManager;
  }

  @NonNull protected AbstractVideoOnItemSelectedListener createOnItemSelectedListener(MoviePosterImageAdapter adapter) {
    return new MovieGridPosterOnItemSelectedListener(adapter);
  }

}
