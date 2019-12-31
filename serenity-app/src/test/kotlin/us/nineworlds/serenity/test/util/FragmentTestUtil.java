package us.nineworlds.serenity.test.util;


import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import org.robolectric.Robolectric;

import static org.robolectric.shadows.ShadowLooper.shadowMainLooper;

public final class FragmentTestUtil {
  
  public static void startFragment(Fragment fragment) {
    buildFragmentManager(FragmentUtilActivity.class)
        .beginTransaction().add(fragment, null).commit();
    shadowMainLooper().idleIfPaused();
  }

  public static void startFragment(Fragment fragment, Class<? extends AppCompatActivity> activityClass) {
    buildFragmentManager(activityClass)
        .beginTransaction().add(fragment, null).commit();
    shadowMainLooper().idleIfPaused();
  }

  public static void startVisibleFragment(Fragment fragment) {
    buildFragmentManager(FragmentUtilActivity.class)
        .beginTransaction().add(1, fragment, null).commit();
    shadowMainLooper().idleIfPaused();
  }

  public static void startVisibleFragment(Fragment fragment,
      Class<? extends AppCompatActivity> activityClass, int containerViewId) {
    buildFragmentManager(activityClass)
        .beginTransaction().add(containerViewId, fragment, null).commit();
    shadowMainLooper().idleIfPaused();
  }

  private static FragmentManager buildFragmentManager(Class<? extends AppCompatActivity> activityClass) {
    AppCompatActivity activity = Robolectric.buildActivity(activityClass).create().start().resume().get();
    return activity.getSupportFragmentManager();
  }

  private static class FragmentUtilActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      LinearLayout view = new LinearLayout(this);
      view.setId(1);

      setContentView(view);
    }
  }
}
