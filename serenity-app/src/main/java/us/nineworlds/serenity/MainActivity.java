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

package us.nineworlds.serenity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import butterknife.BindView;
import com.arellomobile.mvp.presenter.InjectPresenter;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import net.ganin.darv.DpadAwareRecyclerView;
import us.nineworlds.serenity.core.ServerConfig;
import us.nineworlds.serenity.core.menus.MenuDrawerItem;
import us.nineworlds.serenity.core.menus.MenuDrawerItemImpl;
import us.nineworlds.serenity.core.services.OnDeckRecommendationIntentService;
import us.nineworlds.serenity.core.util.AndroidHelper;
import us.nineworlds.serenity.ui.activity.SerenityDrawerLayoutActivity;
import us.nineworlds.serenity.ui.adapters.MenuDrawerAdapter;
import us.nineworlds.serenity.ui.listeners.SettingsMenuDrawerOnItemClickedListener;
import us.nineworlds.serenity.ui.util.DisplayUtils;

import static butterknife.ButterKnife.bind;

public class MainActivity extends SerenityDrawerLayoutActivity implements MainView {

  public static final int MAIN_MENU_PREFERENCE_RESULT_CODE = 100;

  @Inject AndroidHelper androidHelper;
  @InjectPresenter MainPresenter presenter;
  @Inject SharedPreferences preferences;

  @BindView(R.id.mainGalleryMenu) DpadAwareRecyclerView mainMenuContainer;
  @BindView(R.id.drawer_settings) Button settingsButton;
  @BindView(R.id.data_loading_container) View dataLoadingContainer;

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    actionBar.setCustomView(R.layout.clock_layout);
    actionBar.setDisplayShowCustomEnabled(true);

    setContentView(R.layout.activity_main);
    DisplayUtils.overscanCompensation(this, getWindow().getDecorView());
    bind(this);
    initMenuDrawerViews();
    createSideMenu();

    initPreferences();

    initializeDefaultPlayer();
    boolean watchedStatusFirstTime = preferences.getBoolean("watched_status_firsttime", true);
    if (watchedStatusFirstTime) {
      presenter.clearCache(this);

      Editor editor = preferences.edit();
      editor.putBoolean("watched_status_firsttime", false);
      editor.apply();
    }

  }

  @Override protected void onRestart() {
    populateMenuOptions();
    super.onRestart();
  }

  @Override protected void onResume() {
    super.onResume();

    Intent recommendationIntent = new Intent(getApplicationContext(), OnDeckRecommendationIntentService.class);
    startService(recommendationIntent);

    drawerList.setVisibility(View.GONE);

    mainMenuContainer.setFocusable(true);
    mainMenuContainer.requestFocusFromTouch();
  }

  @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (resultCode == MAIN_MENU_PREFERENCE_RESULT_CODE) {
      recreate();
    }
  }

  @Override protected void createSideMenu() {
    mainMenuContainer.setVisibility(View.VISIBLE);
    drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.drawable.menudrawer_selector, R.string.drawer_open,
        R.string.drawer_closed) {
      @Override public void onDrawerOpened(View drawerView) {

        super.onDrawerOpened(drawerView);
        getSupportActionBar().setTitle(R.string.app_name);
        drawerList.requestFocusFromTouch();
      }

      @Override public void onDrawerClosed(View drawerView) {
        super.onDrawerClosed(drawerView);
        getSupportActionBar().setTitle(R.string.app_name);
        mainMenuContainer.requestFocusFromTouch();
      }
    };

    drawerLayout.setDrawerListener(drawerToggle);
    actionBar.setDisplayHomeAsUpEnabled(true);
    actionBar.setHomeButtonEnabled(true);

    settingsButton = (Button) findViewById(R.id.drawer_settings);
    settingsButton.setOnClickListener(new SettingsMenuDrawerOnItemClickedListener(drawerLayout));

    populateMenuOptions();
  }

  protected void populateMenuOptions() {
    drawerList.setVisibility(View.GONE);
    List<MenuDrawerItem> drawerMenuItem = new ArrayList<>();
    drawerMenuItem.add(new MenuDrawerItemImpl(getResources().getString(R.string.options_main_about),
        R.drawable.ic_action_action_about));
    drawerMenuItem.add(new MenuDrawerItemImpl(getResources().getString(R.string.options_main_clear_image_cache),
        R.drawable.ic_action_content_remove));
    drawerMenuItem.add(
        new MenuDrawerItemImpl(getResources().getString(R.string.clear_queue), R.drawable.ic_action_content_remove));

    drawerList.setAdapter(new MenuDrawerAdapter(this, drawerMenuItem));
    drawerList.setOnItemClickListener(new MainMenuDrawerOnItemClickedListener(drawerLayout));
    mainMenuContainer.requestFocusFromTouch();
  }

  protected void initPreferences() {
    ServerConfig config = (ServerConfig) ServerConfig.getInstance();
    if (config != null) {
      preferences.registerOnSharedPreferenceChangeListener(
          ((ServerConfig) ServerConfig.getInstance()).getServerConfigChangeListener());
    }
  }

  protected void initializeDefaultPlayer() {
    boolean initialRun = preferences.getBoolean("serenity_first_run", true);
    if (initialRun) {
      SharedPreferences.Editor editor = preferences.edit();
      if (!androidHelper.isGoogleTV()
          && !androidHelper.isAndroidTV()
          && !androidHelper.isAmazonFireTV()
          && !androidHelper.isLeanbackSupported()) {
        editor.putBoolean("external_player", false);
      }
      editor.putBoolean("serenity_first_run", false);
      editor.apply();
    }
  }

  @Override public boolean onKeyDown(int keyCode, KeyEvent event) {
    boolean menuKeySlidingMenu = preferences.getBoolean("remote_control_menu", true);

    if (menuKeySlidingMenu && keyCode == KeyEvent.KEYCODE_MENU) {
      if (drawerLayout.isDrawerOpen(linearDrawerLayout)) {
        drawerLayout.closeDrawers();
        if (mainMenuContainer != null) {
          mainMenuContainer.requestFocusFromTouch();
        }
      } else {
        drawerLayout.openDrawer(linearDrawerLayout);
        drawerList.requestFocus();
      }
      return true;
    }

    if (drawerLayout.isDrawerOpen(linearDrawerLayout) && keyCode == KeyEvent.KEYCODE_BACK) {
      drawerLayout.closeDrawer(linearDrawerLayout);
      mainMenuContainer.requestFocusFromTouch();
      return true;
    }

    return super.onKeyDown(keyCode, event);
  }

  @Override public void openOptionsMenu() {
    drawerLayout.openDrawer(linearDrawerLayout);
    drawerList.requestFocusFromTouch();
  }
}
