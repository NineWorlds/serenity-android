package us.nineworlds.serenity.ui.browser.tv;

import android.app.Activity;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;

import net.ganin.darv.DpadAwareRecyclerView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import us.nineworlds.serenity.R;
import us.nineworlds.serenity.core.model.SeriesContentInfo;
import us.nineworlds.serenity.injection.BaseInjector;
import us.nineworlds.serenity.ui.adapters.AbstractPosterImageGalleryAdapter;
import us.nineworlds.serenity.widgets.DrawerLayout;

public class OnKeyDownDelegate extends BaseInjector {

    @Inject
    SharedPreferences preferences;

    @BindView(R.id.left_drawer)
    LinearLayout linearDrawerLayout;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    @BindView(R.id.tvShowBannerGallery) @Nullable
    DpadAwareRecyclerView galleryView;

    Activity activity;

    public OnKeyDownDelegate(Activity activity) {
        super();
        this.activity = activity;
        ButterKnife.bind(this, activity);
    }

    public boolean onKeyDown(int keyCode, KeyEvent keyEvent) {
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

        if (keyCode == KeyEvent.KEYCODE_BACK
                && drawerLayout.isDrawerOpen(linearDrawerLayout)) {
            drawerLayout.closeDrawers();
            galleryView.requestFocusFromTouch();
            return true;
        }

        AbstractPosterImageGalleryAdapter adapter = (AbstractPosterImageGalleryAdapter) galleryView.getAdapter();
        if (adapter != null) {
            int itemsCount = adapter.getItemCount();

            if (contextMenuRequested(keyCode)) {
                int pos = galleryView.getSelectedItemPosition();
                RecyclerView.LayoutManager layoutManager = galleryView.getLayoutManager();
                View view = layoutManager.findViewByPosition(pos);
                view.performLongClick();
                return true;
            }
            if (isKeyCodeSkipBack(keyCode)) {
                int selectedItem = galleryView.getSelectedItemPosition();
                int newPosition = selectedItem - 10;
                if (newPosition < 0) {
                    newPosition = 0;
                }
                galleryView.setSelection(newPosition);
                galleryView.requestFocusFromTouch();
                return true;
            }
            if (isKeyCodeSkipForward(keyCode)) {
                int selectedItem = galleryView.getSelectedItemPosition();
                int newPosition = selectedItem + 10;
                if (newPosition > itemsCount) {
                    newPosition = itemsCount - 1;
                }
                galleryView.setSelection(newPosition);
                galleryView.requestFocusFromTouch();
                return true;
            }

            if (keyCode == KeyEvent.KEYCODE_MEDIA_PLAY
                    || keyCode == KeyEvent.KEYCODE_BUTTON_R1) {
                int selectedItem = galleryView.getSelectedItemPosition();
                SeriesContentInfo info = (SeriesContentInfo) ((AbstractPosterImageGalleryAdapter) galleryView.getAdapter()).getItem(selectedItem);
                new FindUnwatchedAsyncTask(activity).execute(info);
                return true;
            }
        }

        return false;
    }

    protected boolean contextMenuRequested(int keyCode) {
        boolean menuKeySlidingMenu = preferences.getBoolean("remote_control_menu", true);
        return keyCode == KeyEvent.KEYCODE_C
                || keyCode == KeyEvent.KEYCODE_BUTTON_Y
                || keyCode == KeyEvent.KEYCODE_BUTTON_R2
                || keyCode == KeyEvent.KEYCODE_PROG_RED
                || (keyCode == KeyEvent.KEYCODE_MENU && menuKeySlidingMenu == false);
    }

    protected boolean isKeyCodeSkipForward(int keyCode) {
        return keyCode == KeyEvent.KEYCODE_F
                || keyCode == KeyEvent.KEYCODE_PAGE_UP
                || keyCode == KeyEvent.KEYCODE_CHANNEL_UP
                || keyCode == KeyEvent.KEYCODE_MEDIA_NEXT
                || keyCode == KeyEvent.KEYCODE_MEDIA_FAST_FORWARD
                || keyCode == KeyEvent.KEYCODE_BUTTON_R1;
    }

    protected boolean isKeyCodeSkipBack(int keyCode) {
        return keyCode == KeyEvent.KEYCODE_R
                || keyCode == KeyEvent.KEYCODE_PAGE_DOWN
                || keyCode == KeyEvent.KEYCODE_CHANNEL_DOWN
                || keyCode == KeyEvent.KEYCODE_MEDIA_PREVIOUS
                || keyCode == KeyEvent.KEYCODE_MEDIA_REWIND
                || keyCode == KeyEvent.KEYCODE_BUTTON_L1;
    }

}
