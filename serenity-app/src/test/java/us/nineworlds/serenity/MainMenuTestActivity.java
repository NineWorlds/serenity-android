package us.nineworlds.serenity;

import android.app.Activity;

public class MainMenuTestActivity extends Activity {

    public boolean onSearchActivtyCalled = false;
    public boolean openOptionsMenu = false;

    @Override
    public boolean onSearchRequested() {
        onSearchActivtyCalled = true;
        return onSearchActivtyCalled;
    }

    @Override
    public void openOptionsMenu() {
        openOptionsMenu = true;
    }
}
