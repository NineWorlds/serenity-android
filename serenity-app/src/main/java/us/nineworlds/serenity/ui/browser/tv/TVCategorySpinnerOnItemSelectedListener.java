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

package us.nineworlds.serenity.ui.browser.tv;

import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.birbit.android.jobqueue.JobManager;

import net.ganin.darv.DpadAwareRecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import javax.inject.Inject;

import us.nineworlds.plex.rest.PlexappFactory;
import us.nineworlds.serenity.R;
import us.nineworlds.serenity.core.model.CategoryInfo;
import us.nineworlds.serenity.core.model.SecondaryCategoryInfo;
import us.nineworlds.serenity.core.model.impl.SecondaryCategoryMediaContainer;
import us.nineworlds.serenity.events.TVCategorySecondaryEvent;
import us.nineworlds.serenity.injection.BaseInjector;
import us.nineworlds.serenity.jobs.TVCategorySecondaryJob;
import us.nineworlds.serenity.ui.activity.SerenityMultiViewVideoActivity;
import us.nineworlds.serenity.ui.browser.tv.episodes.EpisodeBrowserActivity;

public class TVCategorySpinnerOnItemSelectedListener extends BaseInjector implements OnItemSelectedListener {

    private String selected;
    private static String key;
    private boolean firstSelection = true;
    private static SerenityMultiViewVideoActivity context;
    private static String category;
    private String savedInstanceCategory;

    @Inject
    JobManager jobManager;

    @Inject
    EventBus eventBus;

    @Inject
    SharedPreferences prefs;

    @Inject
    TVCategoryState categoryState;

    @Inject
    PlexappFactory factory;

    public TVCategorySpinnerOnItemSelectedListener(String defaultSelection,
                                                   String ckey, SerenityMultiViewVideoActivity activity) {
        selected = defaultSelection;
        key = ckey;
        context = activity;
        eventBus.register(this);
    }

    public TVCategorySpinnerOnItemSelectedListener(String defaultSelection,
                                                   String ckey, boolean sw, SerenityMultiViewVideoActivity activity) {
        selected = defaultSelection;
        key = ckey;
        savedInstanceCategory = defaultSelection;
        firstSelection = sw;
        context = activity;
        eventBus.register(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> viewAdapter, View view,
                               int position, long id) {
        CategoryInfo item = (CategoryInfo) viewAdapter
                .getItemAtPosition(position);

        Spinner secondarySpinner = (Spinner) context
                .findViewById(R.id.categoryFilter2);

        if (savedInstanceCategory != null) {
            int savedInstancePosition = getSavedInstancePosition(viewAdapter);
            item = (CategoryInfo) viewAdapter
                    .getItemAtPosition(savedInstancePosition);
            viewAdapter.setSelection(savedInstancePosition);
            savedInstanceCategory = null;
            if (item.getLevel() == 0) {
                populatePrimaryCategory(item, secondarySpinner);
            } else {
                populateSecondaryCategory();
            }
            return;
        }

        if (firstSelection) {
            String filter = prefs.getString("serenity_series_category_filter",
                    "all");

            int count = viewAdapter.getCount();
            for (int i = 0; i < count; i++) {
                CategoryInfo citem = (CategoryInfo) viewAdapter
                        .getItemAtPosition(i);
                if (citem.getCategory().equals(filter)) {
                    item = citem;
                    selected = citem.getCategory();
                    viewAdapter.setSelection(i);
                    continue;
                }
            }

            if (item.getCategory().equals("newest")
                    || item.getCategory().equals("recentlyAdded")
                    || item.getCategory().equals("recentlyViewed")
                    || item.getCategory().equals("onDeck")) {
                Intent i = new Intent(context, EpisodeBrowserActivity.class);
                i.putExtra("key",
                        "/library/sections/" + key + "/" + item.getCategory());
                context.startActivityForResult(i, 0);
            } else {
                setupImageGallery(item);
            }
            firstSelection = false;
            return;
        }

        if (selected.equalsIgnoreCase(item.getCategory())) {
            return;
        }

        selected = item.getCategory();
        category = item.getCategory();
        categoryState.setCategory(selected);

        if (item.getLevel() == 0) {
            populatePrimaryCategory(item, secondarySpinner);
        } else {
            populateSecondaryCategory();
        }

    }

    protected void populatePrimaryCategory(CategoryInfo item,
                                           Spinner secondarySpinner) {
        if (item.getCategory().equals("newest")
                || item.getCategory().equals("recentlyAdded")
                || item.getCategory().equals("recentlyViewed")
                || item.getCategory().equals("onDeck")) {
            Intent i = new Intent(context, EpisodeBrowserActivity.class);
            i.putExtra("key",
                    "/library/sections/" + key + "/" + item.getCategory());
            context.startActivityForResult(i, 0);
        } else {
            secondarySpinner.setVisibility(View.INVISIBLE);
            setupImageGallery(item);
        }
    }

    protected void populateSecondaryCategory() {
        TVCategorySecondaryJob tvCategoryJob = new TVCategorySecondaryJob(key, category);
        jobManager.addJobInBackground(tvCategoryJob);

    }

    private int getSavedInstancePosition(AdapterView<?> viewAdapter) {
        int count = viewAdapter.getCount();
        for (int i = 0; i < count; i++) {
            CategoryInfo citem = (CategoryInfo) viewAdapter
                    .getItemAtPosition(i);
            if (citem.getCategory().equals(savedInstanceCategory)) {
                return i;
            }
        }
        return 0;
    }

    protected void setupImageGallery(CategoryInfo item) {
        DpadAwareRecyclerView recyclerView;

        if (context.isGridViewActive()) {
            recyclerView = (DpadAwareRecyclerView) context.findViewById(R.id.tvShowGridView);
            recyclerView.setAdapter(new TVShowPosterImageGalleryAdapter(context,
                    key, item.getCategory()));
            recyclerView.setOnKeyListener(new TVShowGridOnKeyListener(context));
        } else {
            DpadAwareRecyclerView posterGallery = (DpadAwareRecyclerView) context
                    .findViewById(R.id.tvShowBannerGallery);
            if (!context.isPosterLayoutActive()) {
                posterGallery.setAdapter(new TVShowBannerImageGalleryAdapter(
                        context, key, item.getCategory()));
            } else {
                posterGallery.setAdapter(new TVShowPosterImageGalleryAdapter(
                        context, key, item.getCategory()));
            }
//			posterGallery
//			.setOnItemLongClickListener(new ShowOnItemLongClickListener());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onTVCategorySecondaryResponse(TVCategorySecondaryEvent event) {
        SecondaryCategoryMediaContainer scMediaContainer = new SecondaryCategoryMediaContainer(
                event.getMediaContainer(), event.getCategory());

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


    @Override
    public void onNothingSelected(AdapterView<?> va) {

    }

}
