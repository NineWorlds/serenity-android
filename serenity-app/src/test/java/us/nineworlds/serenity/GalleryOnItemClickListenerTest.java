package us.nineworlds.serenity;

import android.view.View;
import org.assertj.android.api.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.Shadows;
import org.robolectric.shadows.ShadowActivity;
import us.nineworlds.serenity.core.menus.MenuItem;
import us.nineworlds.serenity.ui.browser.movie.MovieBrowserActivity;
import us.nineworlds.serenity.ui.browser.tv.TVShowBrowserActivity;
import us.nineworlds.serenity.ui.preferences.LeanbackSettingsActivity;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.doReturn;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(RobolectricTestRunner.class)
public class GalleryOnItemClickListenerTest {

  @Mock
  MainMenuTextViewAdapter mockAdapter;

  MainMenuTestActivity context;
  GalleryOnItemClickListener onItemClickListener;

  @Before
  public void setUp() throws Exception {
    initMocks(this);
    context = Robolectric.setupActivity(MainMenuTestActivity.class);
    onItemClickListener = new GalleryOnItemClickListener(mockAdapter);
  }

  @After
  public void tearDown() {
    context.finish();
  }

  @Test
  public void onClickMenuSearchLaunchesExpectedActivity() {
    View view = new View(context);
    MenuItem searchMenuItem = new MenuItem();
    searchMenuItem.setType("search");
    doReturn(searchMenuItem).when(mockAdapter).getItemAtPosition(anyInt());

    onItemClickListener.onItemClick(view, 0);

    assertThat(context.onSearchActivtyCalled).isTrue();
  }

  @Test
  public void onClickOpensOptionsMenu() {
    View view = new View(context);
    MenuItem searchMenuItem = new MenuItem();
    searchMenuItem.setType("options");
    doReturn(searchMenuItem).when(mockAdapter).getItemAtPosition(anyInt());

    onItemClickListener.onItemClick(view, 0);

    assertThat(context.openOptionsMenu).isTrue();
  }

  @Test
  public void onClickLaunchesMovieBrowserActivity() {
    View view = new View(context);
    MenuItem searchMenuItem = new MenuItem();
    searchMenuItem.setType("movie");
    doReturn(searchMenuItem).when(mockAdapter).getItemAtPosition(anyInt());

    onItemClickListener.onItemClick(view, 0);

    ShadowActivity shadowActivity = Shadows.shadowOf(context);
    Assertions.assertThat(shadowActivity.getNextStartedActivity()).hasComponent(context, MovieBrowserActivity.class);
  }

  @Test
  public void onClickLaunchesTVShowBrowserActivity() {
    View view = new View(context);
    MenuItem searchMenuItem = new MenuItem();
    searchMenuItem.setType("show");
    doReturn(searchMenuItem).when(mockAdapter).getItemAtPosition(anyInt());

    onItemClickListener.onItemClick(view, 0);

    ShadowActivity shadowActivity = Shadows.shadowOf(context);
    Assertions.assertThat(shadowActivity.getNextStartedActivity()).hasComponent(context, TVShowBrowserActivity.class);
  }

  @Test
  public void onClickUnknownRequestLaunchesPreferences() {
    View view = new View(context);
    MenuItem searchMenuItem = new MenuItem();
    searchMenuItem.setType("unknown");
    doReturn(searchMenuItem).when(mockAdapter).getItemAtPosition(anyInt());

    onItemClickListener.onItemClick(view, 0);

    ShadowActivity shadowActivity = Shadows.shadowOf(context);
    Assertions.assertThat(shadowActivity.getNextStartedActivity())
        .hasComponent(context, LeanbackSettingsActivity.class);
  }
}