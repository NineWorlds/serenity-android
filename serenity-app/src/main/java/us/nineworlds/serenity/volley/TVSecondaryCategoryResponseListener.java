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
import android.widget.Toast;

import com.android.volley.Response;

import java.util.List;

import us.nineworlds.plex.rest.model.impl.MediaContainer;
import us.nineworlds.serenity.R;
import us.nineworlds.serenity.core.model.SecondaryCategoryInfo;
import us.nineworlds.serenity.core.model.impl.SecondaryCategoryMediaContainer;
import us.nineworlds.serenity.ui.activity.SerenityMultiViewVideoActivity;
import us.nineworlds.serenity.ui.browser.tv.TVSecondaryCategorySpinnerOnItemSelectedListener;

public class TVSecondaryCategoryResponseListener implements
        Response.Listener<MediaContainer> {

    protected SerenityMultiViewVideoActivity context;
    protected String category;
    protected String key;

    public TVSecondaryCategoryResponseListener(SerenityMultiViewVideoActivity context,
                                               String category, String key) {
        super();
        this.category = category;
        this.context = context;
        this.key = key;
    }

    @Override
    public void onResponse(MediaContainer mediaContainer) {
        SecondaryCategoryMediaContainer scMediaContainer = new SecondaryCategoryMediaContainer(
                mediaContainer, category);

        List<SecondaryCategoryInfo> secondaryCategories = scMediaContainer
                .createCategories();

        if (secondaryCategories == null || secondaryCategories.isEmpty()) {
            Toast.makeText(context,
                    R.string.no_entries_available_for_category_,
                    Toast.LENGTH_LONG).show();
            return;
        }

        Spinner secondarySpinner = (Spinner) context
                .findViewById(R.id.categoryFilter2);
        secondarySpinner.setVisibility(View.VISIBLE);

        ArrayAdapter<SecondaryCategoryInfo> spinnerSecArrayAdapter = new ArrayAdapter<SecondaryCategoryInfo>(
                context, R.layout.serenity_spinner_textview,
                secondaryCategories);
        spinnerSecArrayAdapter
                .setDropDownViewResource(R.layout.serenity_spinner_textview_dropdown);
        secondarySpinner.setAdapter(spinnerSecArrayAdapter);
        secondarySpinner
                .setOnItemSelectedListener(new TVSecondaryCategorySpinnerOnItemSelectedListener(
                        category, key, context));

    }
}
