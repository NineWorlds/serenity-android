package us.nineworlds.serenity.ui.views.statusoverlayview;

import android.app.Activity;
import org.assertj.core.api.Java6Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import us.nineworlds.serenity.BuildConfig;

import static org.assertj.android.api.Assertions.assertThat;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class StatusOverlayFrameLayoutTest {

  Activity activity;
  StatusOverlayFrameLayout view;

  @Before public void setUp() {
    activity = Robolectric.buildActivity(Activity.class).create().visible().get();
    view = new StatusOverlayFrameLayout(activity);
  }

  @After public void tearDown() {
    activity.finish();
    if (view.presenter != null) {
      view.onDetachedFromWindow();
    }
  }

  @Test public void verifyViewsAreInflated() {
    assertThat(view.infoGraphicMetaContainer).isNotNull();
    assertThat(view.metaOverlay).isNotNull();
    assertThat(view.posterInProgressIndicator).isNotNull();
    assertThat(view.posterOverlayTitle).isNotNull();
    assertThat(view.posterWatchedIndicator).isNotNull();
    assertThat(view.roundedImageView).isNotNull();
    assertThat(view.subtitleIndicator).isNotNull();
    assertThat(view.trailerIndicator).isNotNull();
  }

  @Test public void verifyPresenterSet() {
    Java6Assertions.assertThat(view.presenter).isNotNull();
  }
}