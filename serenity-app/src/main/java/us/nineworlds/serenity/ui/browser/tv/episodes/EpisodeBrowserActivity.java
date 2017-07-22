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

package us.nineworlds.serenity.ui.browser.tv.episodes;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.KeyEvent;
import android.view.View;

import com.arellomobile.mvp.presenter.InjectPresenter;

import net.ganin.darv.DpadAwareRecyclerView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import us.nineworlds.serenity.R;
import us.nineworlds.serenity.core.menus.MenuDrawerItem;
import us.nineworlds.serenity.core.menus.MenuDrawerItemImpl;
import us.nineworlds.serenity.core.model.VideoContentInfo;
import us.nineworlds.serenity.ui.activity.SerenityVideoActivity;
import us.nineworlds.serenity.ui.adapters.AbstractPosterImageGalleryAdapter;
import us.nineworlds.serenity.ui.adapters.MenuDrawerAdapter;
import us.nineworlds.serenity.ui.util.DisplayUtils;

public class EpisodeBrowserActivity extends SerenityVideoActivity implements EpisodeBrowserView {

    @Inject
    protected SharedPreferences prefs;

    @InjectPresenter
    EpisodeBrowserPresenter presenter;

    public AbstractPosterImageGalleryAdapter seasonEpisodeAdapter;

    private static String key;
    private View bgLayout;
    private View metaData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actionBar.setCustomView(R.layout.move_custom_actionbar);
        actionBar.setDisplayShowCustomEnabled(true);

        key = getIntent().getExtras().getString("key");



        createSideMenu();

        bgLayout = findViewById(R.id.movieBrowserBackgroundLayout);
        metaData = findViewById(R.id.metaDataRow);
        metaData.setVisibility(View.VISIBLE);

        DisplayUtils.overscanCompensation(this, getWindow().getDecorView());
    }

    @Override
    protected void createSideMenu() {
        setContentView(R.layout.activity_episode_browser);

        initMenuDrawerViews();

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
                R.drawable.menudrawer_selector, R.string.drawer_open,
                R.string.drawer_closed) {
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
        drawerMenuItem.add(new MenuDrawerItemImpl(getResources().getString(
                R.string.play_all_from_queue), R.drawable.menu_play_all_queue));

        drawerList.setAdapter(new MenuDrawerAdapter(this, drawerMenuItem));
        drawerList
                .setOnItemClickListener(new EpisodeMenuDrawerOnItemClickedListener(
                        drawerLayout));
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean menuKeySlidingMenu = prefs.getBoolean("remote_control_menu",
                true);
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

        if (keyCode == KeyEvent.KEYCODE_BACK
                && drawerLayout.isDrawerOpen(linearDrawerLayout)) {
            drawerLayout.closeDrawers();

            View gallery = findViewById(R.id.moviePosterView);
            if (gallery != null) {
                gallery.requestFocusFromTouch();
            }
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        populateMenuDrawer();
    }

    @Override
    public AbstractPosterImageGalleryAdapter getAdapter() {
        return seasonEpisodeAdapter;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (key != null && key.contains("onDeck")) {
            recreate();
            return;
        }
    }

    public static String getKey() {
        return key;
    }

    @Override
    protected DpadAwareRecyclerView findGalleryView() {
        return (DpadAwareRecyclerView) findViewById(R.id.moviePosterView);
    }

    @Override
    protected DpadAwareRecyclerView findGridView() {
        return null;
    }

    @Override
    public void updateGallery(List<VideoContentInfo> episodes) {
        DpadAwareRecyclerView gallery = findGalleryView();
        EpisodePosterImageGalleryAdapter adapter = (EpisodePosterImageGalleryAdapter) gallery.getAdapter();
        adapter.updateEpisodes(episodes);
        gallery.requestFocus();
    }

    @Override
    public void fetchEpisodes(String key) {
        presenter.retrieveEpisodes(key);
    }
}
