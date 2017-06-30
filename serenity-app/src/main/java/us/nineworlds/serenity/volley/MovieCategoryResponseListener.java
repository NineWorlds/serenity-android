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

package us.nineworlds.serenity.volley;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.android.volley.Response;

import java.util.List;

import javax.inject.Inject;

import us.nineworlds.plex.rest.model.impl.MediaContainer;
import us.nineworlds.serenity.R;
import us.nineworlds.serenity.core.model.CategoryInfo;
import us.nineworlds.serenity.core.model.impl.CategoryMediaContainer;
import us.nineworlds.serenity.injection.BaseInjector;
import us.nineworlds.serenity.ui.activity.SerenityMultiViewVideoActivity;
import us.nineworlds.serenity.ui.browser.movie.MovieCategorySpinnerOnItemSelectedListener;
import us.nineworlds.serenity.ui.browser.movie.MovieSelectedCategoryState;

public class MovieCategoryResponseListener extends BaseInjector implements
        Response.Listener<MediaContainer> {

    @Inject
    MovieSelectedCategoryState categoryState;

    private List<CategoryInfo> categories;
    private final SerenityMultiViewVideoActivity context;
    private final String key;

    public MovieCategoryResponseListener(SerenityMultiViewVideoActivity context, String key) {
        super();
        this.context = context;
        this.key = key;
    }

    @Override
    public void onResponse(MediaContainer mediaContainer) {
        CategoryMediaContainer categoryMediaContainer = new CategoryMediaContainer(
                mediaContainer);
        categories = categoryMediaContainer.createCategories();
        setupMovieBrowser();
    }

    /**
     * Setup the Gallery and Category spinners
     */
    protected void setupMovieBrowser() {
        ArrayAdapter<CategoryInfo> spinnerArrayAdapter = new ArrayAdapter<CategoryInfo>(
                context, R.layout.serenity_spinner_textview, categories);
        spinnerArrayAdapter
                .setDropDownViewResource(R.layout.serenity_spinner_textview_dropdown);

        Spinner categorySpinner = (Spinner) context
                .findViewById(R.id.categoryFilter);
        if (categorySpinner != null) {
            categorySpinner.setVisibility(View.VISIBLE);
            categorySpinner.setAdapter(spinnerArrayAdapter);
            if (categoryState.getCategory() == null) {
                categorySpinner
                        .setOnItemSelectedListener(new MovieCategorySpinnerOnItemSelectedListener(
                                "all", key, context));
            } else {
                categorySpinner
                        .setOnItemSelectedListener(new MovieCategorySpinnerOnItemSelectedListener(
                                categoryState.getCategory(), key, false, context));
            }
            categorySpinner.requestFocus();
        }
    }

}
