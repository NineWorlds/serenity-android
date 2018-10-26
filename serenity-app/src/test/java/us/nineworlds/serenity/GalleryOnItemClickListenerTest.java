package us.nineworlds.serenity;

import net.ganin.darv.DpadAwareRecyclerView;
import org.assertj.android.api.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
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

    context = Robolectric.buildActivity(MainMenuTestActivity.class).get();
    onItemClickListener = new GalleryOnItemClickListener();
  }

  @After
  public void tearDown() {
    context.finish();
  }

  @Test
  public void onClickMenuSearchLaunchesExpectedActivity() {
    DpadAwareRecyclerView recyclerView = new DpadAwareRecyclerView(context);
    recyclerView.setAdapter(mockAdapter);
    MenuItem searchMenuItem = new MenuItem();
    searchMenuItem.setType("search");
    doReturn(searchMenuItem).when(mockAdapter).getItemAtPosition(anyInt());

    onItemClickListener.onItemClick(recyclerView, null, 0, 0);

    assertThat(context.onSearchActivtyCalled).isTrue();
  }

  @Test
  public void onClickOpensOptionsMenu() {
    DpadAwareRecyclerView recyclerView = new DpadAwareRecyclerView(context);
    recyclerView.setAdapter(mockAdapter);
    MenuItem searchMenuItem = new MenuItem();
    searchMenuItem.setType("options");
    doReturn(searchMenuItem).when(mockAdapter).getItemAtPosition(anyInt());

    onItemClickListener.onItemClick(recyclerView, null, 0, 0);

    assertThat(context.openOptionsMenu).isTrue();
  }

  @Test
  public void onClickLaunchesMovieBrowserActivity() {
    DpadAwareRecyclerView recyclerView = new DpadAwareRecyclerView(context);
    recyclerView.setAdapter(mockAdapter);
    MenuItem searchMenuItem = new MenuItem();
    searchMenuItem.setType("movie");
    doReturn(searchMenuItem).when(mockAdapter).getItemAtPosition(anyInt());

    onItemClickListener.onItemClick(recyclerView, null, 0, 0);

    ShadowActivity shadowActivity = Shadows.shadowOf(context);
    Assertions.assertThat(shadowActivity.getNextStartedActivity()).hasComponent(context, MovieBrowserActivity.class);
  }

  @Test
  public void onClickLaunchesTVShowBrowserActivity() {
    DpadAwareRecyclerView recyclerView = new DpadAwareRecyclerView(context);
    recyclerView.setAdapter(mockAdapter);
    MenuItem searchMenuItem = new MenuItem();
    searchMenuItem.setType("show");
    doReturn(searchMenuItem).when(mockAdapter).getItemAtPosition(anyInt());

    onItemClickListener.onItemClick(recyclerView, null, 0, 0);

    ShadowActivity shadowActivity = Shadows.shadowOf(context);
    Assertions.assertThat(shadowActivity.getNextStartedActivity()).hasComponent(context, TVShowBrowserActivity.class);
  }

  @Test
  public void onClickUnknownRequestLaunchesPreferences() {
    DpadAwareRecyclerView recyclerView = new DpadAwareRecyclerView(context);
    recyclerView.setAdapter(mockAdapter);
    MenuItem searchMenuItem = new MenuItem();
    searchMenuItem.setType("unknown");
    doReturn(searchMenuItem).when(mockAdapter).getItemAtPosition(anyInt());

    onItemClickListener.onItemClick(recyclerView, null, 0, 0);

    ShadowActivity shadowActivity = Shadows.shadowOf(context);
    Assertions.assertThat(shadowActivity.getNextStartedActivity())
        .hasComponent(context, LeanbackSettingsActivity.class);
  }
}