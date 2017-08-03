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

package us.nineworlds.serenity.ui.activity;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import java.util.List;
import us.nineworlds.serenity.R;
import us.nineworlds.serenity.core.model.CategoryInfo;
import us.nineworlds.serenity.ui.browser.movie.MovieCategorySpinnerOnItemSelectedListener;

public class CategoryHandler extends Handler {

  private List<CategoryInfo> categories;
  private final SerenityMultiViewVideoActivity context;
  private final String savedCategory;
  private final String key;

  public CategoryHandler(SerenityMultiViewVideoActivity context, String savedCategory, String key) {
    this.context = context;
    this.savedCategory = savedCategory;
    this.key = key;
  }

  @Override public void handleMessage(Message msg) {
    if (msg.obj != null) {
      categories = (List<CategoryInfo>) msg.obj;
      setupMovieBrowser();
    }
  }

  /**
   * Setup the Gallery and Category spinners
   */
  protected void setupMovieBrowser() {
    ArrayAdapter<CategoryInfo> spinnerArrayAdapter =
        new ArrayAdapter<CategoryInfo>(context, R.layout.serenity_spinner_textview, categories);
    spinnerArrayAdapter.setDropDownViewResource(R.layout.serenity_spinner_textview_dropdown);

    Spinner categorySpinner = (Spinner) context.findViewById(R.id.categoryFilter);
    if (categorySpinner != null) {
      categorySpinner.setVisibility(View.VISIBLE);
      categorySpinner.setAdapter(spinnerArrayAdapter);
      if (savedCategory == null) {
        categorySpinner.setOnItemSelectedListener(
            new MovieCategorySpinnerOnItemSelectedListener("all", key, context));
      } else {
        // categorySpinner
        // .setOnItemSelectedListener(new
        // MovieCategorySpinnerOnItemSelectedListener(
        // savedCategory, key, false));
      }
      categorySpinner.requestFocus();
    }
  }
}
