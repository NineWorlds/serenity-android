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
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;

import javax.inject.Inject;

import butterknife.BindView;
import moxy.presenter.InjectPresenter;
import us.nineworlds.serenity.core.ServerConfig;
import us.nineworlds.serenity.core.services.OnDeckRecommendationIntentService;
import us.nineworlds.serenity.core.util.AndroidHelper;
import us.nineworlds.serenity.ui.activity.SerenityActivity;
import us.nineworlds.serenity.ui.activity.login.LoginUserActivity;
import us.nineworlds.serenity.ui.util.DisplayUtils;

import static butterknife.ButterKnife.bind;

public class MainActivity extends SerenityActivity implements MainView {

    public static final int MAIN_MENU_PREFERENCE_RESULT_CODE = 100;

    @Inject
    AndroidHelper androidHelper;

    @InjectPresenter
    MainPresenter presenter;

    @Inject
    SharedPreferences preferences;

    @BindView(R.id.mainGalleryMenu)
    RecyclerView mainMenuContainer;
    @BindView(R.id.data_loading_container)
    View dataLoadingContainer;

    MaterialToolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        DisplayUtils.overscanCompensation(this, getWindow().getDecorView());
        bind(this);

        initPreferences();

        initializeDefaultPlayer();
        boolean watchedStatusFirstTime = preferences.getBoolean("watched_status_firsttime", true);
        if (watchedStatusFirstTime) {
            presenter.clearCache(this);

            Editor editor = preferences.edit();
            editor.putBoolean("watched_status_firsttime", false);
            editor.apply();
        }

        toolbar = findViewById(R.id.action_toolbar);
        toolbar.setOnMenuItemClickListener(menuItem -> {
            if (menuItem.getItemId() == R.id.menu_user_selection) {
                startActivity(new Intent(this, LoginUserActivity.class));
                return true;
            }
            return false;
        });

        presenter.showOrHideUserSelection();
    }

    @Override
    protected String screenName() {
        return "Main Menu";
    }

    @Override
    protected void onResume() {
        super.onResume();

        Intent recommendationIntent = new Intent(getApplicationContext(), OnDeckRecommendationIntentService.class);
        startService(recommendationIntent);

        mainMenuContainer.setFocusable(true);
        mainMenuContainer.requestFocusFromTouch();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == MAIN_MENU_PREFERENCE_RESULT_CODE) {
            recreate();
        }
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
            if (!androidHelper.isAndroidTV()
                    && !androidHelper.isAmazonFireTV()
                    && !androidHelper.isLeanbackSupported()) {
                editor.putBoolean("external_player", false);
            }
            editor.putBoolean("serenity_first_run", false);
            editor.apply();
        }
    }

    @Override
    public void hideMultipleUsersOption() {
        toolbar.findViewById(R.id.menu_user_selection).setVisibility(View.GONE);
    }

    @Override
    public void showMultipleUsersOption() {
        toolbar.findViewById(R.id.menu_user_selection).setVisibility(View.VISIBLE);
    }
}
