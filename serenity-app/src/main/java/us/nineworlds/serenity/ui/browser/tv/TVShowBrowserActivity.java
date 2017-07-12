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

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.arellomobile.mvp.presenter.InjectPresenter;

import net.ganin.darv.DpadAwareRecyclerView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import us.nineworlds.serenity.R;
import us.nineworlds.serenity.core.menus.MenuDrawerItem;
import us.nineworlds.serenity.core.menus.MenuDrawerItemImpl;
import us.nineworlds.serenity.core.model.CategoryInfo;
import us.nineworlds.serenity.core.model.SecondaryCategoryInfo;
import us.nineworlds.serenity.core.model.SeriesContentInfo;
import us.nineworlds.serenity.recyclerutils.ItemOffsetDecoration;
import us.nineworlds.serenity.recyclerutils.SpaceItemDecoration;
import us.nineworlds.serenity.ui.activity.SerenityMultiViewVideoActivity;
import us.nineworlds.serenity.ui.adapters.AbstractPosterImageGalleryAdapter;
import us.nineworlds.serenity.ui.adapters.MenuDrawerAdapter;
import us.nineworlds.serenity.ui.util.DisplayUtils;
import us.nineworlds.serenity.widgets.SerenityMenuGridLayoutManager;

public class TVShowBrowserActivity extends SerenityMultiViewVideoActivity implements TVShowBrowserView {

    private boolean restarted_state = false;
    private String key;

    @InjectPresenter
    TVShowBrowserPresenter presenter;

    @Inject
    protected SharedPreferences preferences;

    @Inject
    protected TVCategoryState categoryState;

    @BindView(R.id.tvShowBannerGallery) @Nullable DpadAwareRecyclerView tvShowBannerGalleryView;
    @BindView(R.id.tvShowGridView) @Nullable DpadAwareRecyclerView tvShowGridView;
    @BindView(R.id.fanArt) View fanArt;
    @BindView(R.id.tvShowItemCount) TextView tvShowItemCountView;
    @BindView(R.id.categoryFilter2) Spinner secondarySpinner;
    @BindView(R.id.categoryFilter) Spinner categorySpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actionBar.setCustomView(R.layout.tvshow_custom_actionbar);
        actionBar.setDisplayShowCustomEnabled(true);
        key = presenter.getKey(getIntent());

        createSideMenu();

        fanArt = findViewById(R.id.fanArt);
        fanArt.setBackgroundResource(R.drawable.tvshows);

        populateTVShowContent();

        DisplayUtils.overscanCompensation(this, getWindow().getDecorView());
    }

    private void populateTVShowContent() {
        DpadAwareRecyclerView dpadAwareRecyclerView = findGalleryView() != null ? findGalleryView() : findGridView();

        if (!gridViewActive) {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
            dpadAwareRecyclerView.setLayoutManager(linearLayoutManager);
            dpadAwareRecyclerView.addItemDecoration(new SpaceItemDecoration(getResources().getDimensionPixelSize(R.dimen.horizontal_spacing)));
            dpadAwareRecyclerView.setOnItemSelectedListener(new TVShowGalleryOnItemSelectedListener());
        } else {
            SerenityMenuGridLayoutManager serenityMenuGridLayoutManager = new SerenityMenuGridLayoutManager(this, 3, SerenityMenuGridLayoutManager.HORIZONTAL, false);
            serenityMenuGridLayoutManager.setCircular(true);
            dpadAwareRecyclerView.setLayoutManager(serenityMenuGridLayoutManager);
            dpadAwareRecyclerView.addItemDecoration(new ItemOffsetDecoration(getResources().getDimensionPixelSize(R.dimen.grid_spacing_dimen)));
        }
        dpadAwareRecyclerView.setOnItemClickListener(new TVShowBrowserGalleryOnItemClickListener(this));
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        restarted_state = true;
        populateMenuDrawer();
    }

    @Override
    protected void onResume() {
        super.onResume();
        populateMenuDrawer();
        if (!restarted_state) {
            presenter.fetchTVCategories(key);
        }
    }

    @Override
    protected void createSideMenu() {
        posterLayoutActive = preferences.getBoolean("series_layout_posters", false);
        gridViewActive = preferences.getBoolean("series_layout_grid", false);
        if (isGridViewActive()) {
            setContentView(R.layout.activity_tvbrowser_show_gridview_posters);
        } else if (posterLayoutActive) {
            setContentView(R.layout.activity_tvbrowser_show_posters);
        } else {
            setContentView(R.layout.activity_tvbrowser_show_banners);
        }

        ButterKnife.bind(this);

        initMenuDrawerViews();

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.drawable.menudrawer_selector, R.string.drawer_open, R.string.drawer_closed) {
            @Override
            public void onDrawerOpened(View drawerView) {

                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle(R.string.app_name);
                drawerList.requestFocusFromTouch();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getSupportActionBar().setTitle(R.string.app_name);
            }
        };

        drawerLayout.setDrawerListener(drawerToggle);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        populateMenuDrawer();
    }

    protected void populateMenuDrawer() {
        List<MenuDrawerItem> drawerMenuItem = new ArrayList<MenuDrawerItem>();
        drawerMenuItem.add(new MenuDrawerItemImpl("Grid View", R.drawable.ic_action_collections_view_as_grid));
        drawerMenuItem.add(new MenuDrawerItemImpl("Detail View", R.drawable.ic_action_collections_view_detail));
        drawerMenuItem.add(new MenuDrawerItemImpl("Play All from Queue", R.drawable.menu_play_all_queue));

        drawerList.setAdapter(new MenuDrawerAdapter(this, drawerMenuItem));
        drawerList.setOnItemClickListener(new TVShowMenuDrawerOnItemClickedListener(drawerLayout));
    }

    @Override
    @Deprecated
    public AbstractPosterImageGalleryAdapter getAdapter() {
        return null;
    }

    @Override
    protected DpadAwareRecyclerView findGalleryView() {
        return tvShowBannerGalleryView;
    }

    @Override
    protected DpadAwareRecyclerView findGridView() {
        return tvShowGridView;
    }

    @Override
    public void updateCategories(List<CategoryInfo> categories) {
        ArrayAdapter<CategoryInfo> spinnerArrayAdapter = new ArrayAdapter<CategoryInfo>(this, R.layout.serenity_spinner_textview, categories);

        spinnerArrayAdapter.setDropDownViewResource(R.layout.serenity_spinner_textview_dropdown);

        categorySpinner.setVisibility(View.VISIBLE);
        categorySpinner.setAdapter(spinnerArrayAdapter);

        if (categoryState.getCategory() == null) {
            categorySpinner
                    .setOnItemSelectedListener(new TVCategorySpinnerOnItemSelectedListener("all", key));
        } else {
            categorySpinner
                    .setOnItemSelectedListener(new TVCategorySpinnerOnItemSelectedListener(categoryState.getCategory(), key, false));
        }
        categorySpinner.requestFocus();
    }

    @Override
    public void displayShows(List<SeriesContentInfo> series, String category) {
        DpadAwareRecyclerView recyclerView = findGalleryView() != null ? findGalleryView() : findGridView();
        if (series.isEmpty()) {
            Toast.makeText(this, getString(R.string.no_shows_found_for_the_category_)
                    + category, Toast.LENGTH_LONG).show();
        }
        tvShowItemCountView.setText(Integer.toString(series.size()) + getString(R.string._item_s_));
        TVShowRecyclerAdapter adapter = (TVShowRecyclerAdapter) recyclerView.getAdapter();
        adapter.updateSeries(series);
        recyclerView.requestFocus();
    }

    @Override
    public void populateSecondaryCategories(List<SecondaryCategoryInfo> catagories, String category) {
        categorySpinner.setVisibility(View.VISIBLE);
        if (catagories == null || catagories.isEmpty()) {
            Toast.makeText(this, R.string.no_entries_available_for_category_, Toast.LENGTH_LONG).show();
            return;
        }

        secondarySpinner.setVisibility(View.VISIBLE);

        ArrayAdapter<SecondaryCategoryInfo> spinnerSecArrayAdapter = new ArrayAdapter<SecondaryCategoryInfo>(this, R.layout.serenity_spinner_textview, catagories);
        spinnerSecArrayAdapter.setDropDownViewResource(R.layout.serenity_spinner_textview_dropdown);
        secondarySpinner.setAdapter(spinnerSecArrayAdapter);
        secondarySpinner
                .setOnItemSelectedListener(new TVSecondaryCategorySpinnerOnItemSelectedListener(category, key));
        secondarySpinner.requestFocusFromTouch();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean menuKeySlidingMenu = preferences.getBoolean("remote_control_menu", true);
        if (menuKeySlidingMenu) {
            if (keyCode == KeyEvent.KEYCODE_MENU) {
                if (drawerLayout.isDrawerOpen(linearDrawerLayout)) {
                    drawerLayout.closeDrawers();
                } else {
                    drawerLayout.openDrawer(linearDrawerLayout);
                }
                return true;
            }
        }

        DpadAwareRecyclerView gallery = findGalleryView();
        if (gallery == null) {
            return super.onKeyDown(keyCode, event);
        }

        if (keyCode == KeyEvent.KEYCODE_BACK
                && drawerLayout.isDrawerOpen(linearDrawerLayout)) {
            drawerLayout.closeDrawers();
            if (gallery != null) {
                gallery.requestFocusFromTouch();
            }
            return true;
        }

        AbstractPosterImageGalleryAdapter adapter = (AbstractPosterImageGalleryAdapter) gallery.getAdapter();
        if (adapter != null) {
            int itemsCount = adapter.getItemCount();

            if (contextMenuRequested(keyCode)) {
                int pos = gallery.getSelectedItemPosition();
                RecyclerView.LayoutManager layoutManager = gallery.getLayoutManager();
                View view = layoutManager.findViewByPosition(pos);
                view.performLongClick();
                return true;
            }
            if (isKeyCodeSkipBack(keyCode)) {
                int selectedItem = gallery.getSelectedItemPosition();
                int newPosition = selectedItem - 10;
                if (newPosition < 0) {
                    newPosition = 0;
                }
                gallery.setSelection(newPosition);
                gallery.requestFocusFromTouch();
                return true;
            }
            if (isKeyCodeSkipForward(keyCode)) {
                int selectedItem = gallery.getSelectedItemPosition();
                int newPosition = selectedItem + 10;
                if (newPosition > itemsCount) {
                    newPosition = itemsCount - 1;
                }
                gallery.setSelection(newPosition);
                gallery.requestFocusFromTouch();
                return true;
            }

            if (keyCode == KeyEvent.KEYCODE_MEDIA_PLAY
                    || keyCode == KeyEvent.KEYCODE_BUTTON_R1) {
                int selectedItem = gallery.getSelectedItemPosition();
                SeriesContentInfo info = (SeriesContentInfo) ((AbstractPosterImageGalleryAdapter) gallery.getAdapter()).getItem(selectedItem);
                new FindUnwatchedAsyncTask(this).execute(info);
                return true;
            }
        }

        return super.onKeyDown(keyCode, event);
    }

}
