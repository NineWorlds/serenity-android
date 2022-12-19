package us.nineworlds.serenity.ui.browser.tv;

import com.birbit.android.jobqueue.JobManager;
import java.util.List;
import org.apache.commons.lang3.RandomStringUtils;
import org.greenrobot.eventbus.EventBus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import toothpick.config.Module;
import us.nineworlds.serenity.TestingModule;
import us.nineworlds.serenity.common.rest.SerenityClient;
import us.nineworlds.serenity.emby.model.MediaContainer;
import us.nineworlds.serenity.events.TVCategoryEvent;
import us.nineworlds.serenity.events.TVCategorySecondaryEvent;
import us.nineworlds.serenity.events.TVShowRetrievalEvent;
import us.nineworlds.serenity.jobs.TVCategoryJob;
import us.nineworlds.serenity.jobs.TVShowRetrievalJob;
import us.nineworlds.serenity.test.InjectingTest;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class TVShowBrowserPresenterTest extends InjectingTest {

  @Mock JobManager mockJobManager;

  @Mock EventBus mockEventBus;

  @Mock SerenityClient mockSerenityClient;

  @Mock TVShowBrowserView mockView;

  TVShowBrowserPresenter presenter;

  @Before public void setUp() throws Exception {
    initMocks(this);
    super.setUp();
    presenter = new TVShowBrowserPresenter();
    presenter.attachView(mockView);
  }

  @After public void tearDown() throws Exception {
    presenter.detachView(mockView);
  }

  @Test public void fetchTVCategoriesAddsTVCategoryJobWithExpectedKey() {
    String key = RandomStringUtils.randomAlphanumeric(12);
    doNothing().when(mockJobManager).addJobInBackground(any(TVCategoryJob.class));

    presenter.fetchTVCategories(key);

    ArgumentCaptor<TVCategoryJob> argument = ArgumentCaptor.forClass(TVCategoryJob.class);

    verify(mockJobManager).addJobInBackground(argument.capture());
    TVCategoryJob tvCategoryJob = argument.getValue();
    assertThat(tvCategoryJob.getKey()).isEqualTo(key);
  }

  @Test public void fetchTVShowsAddsTVShowRetrievalJobWithExpectedKeyAndCategory() {
    String expectedKey = RandomStringUtils.randomAlphanumeric(10);
    String expectedCategory = RandomStringUtils.randomAlphanumeric(5);

    doNothing().when(mockJobManager).addJobInBackground(any(TVShowRetrievalJob.class));

    presenter.fetchTVShows(expectedKey, expectedCategory);

    ArgumentCaptor<TVShowRetrievalJob> argument = ArgumentCaptor.forClass(TVShowRetrievalJob.class);

    verify(mockJobManager).addJobInBackground(argument.capture());

    TVShowRetrievalJob tvShowRetrievalJob = argument.getValue();
    assertThat(tvShowRetrievalJob.getKey()).isEqualTo(expectedKey);
    assertThat(tvShowRetrievalJob.getCategory()).isEqualTo(expectedCategory);
  }

  @Test public void onTVCategoryResponseCallsUpdateCategories() throws Exception {
    MediaContainer mediaContainer = new MediaContainer();
    String expectedKey = RandomStringUtils.randomAlphanumeric(10);
    TVCategoryEvent expectedEvent = new TVCategoryEvent(mediaContainer, expectedKey);

    ArgumentCaptor<List> argument = ArgumentCaptor.forClass(List.class);
    presenter.onTVCategoryResponse(expectedEvent);

    verify(mockView).updateCategories(argument.capture());
  }

  @Test public void onTVShowResponseCallsDisplayShows() throws Exception {
    MediaContainer mediaContainer = new MediaContainer();
    String expectedKey = RandomStringUtils.randomAlphanumeric(10);
    String expectedCategory = RandomStringUtils.randomAlphanumeric(5);
    TVShowRetrievalEvent expectedEvent = new TVShowRetrievalEvent(mediaContainer, expectedKey, expectedCategory);

    presenter.onTVShowResponse(expectedEvent);

    ArgumentCaptor<List> argument = ArgumentCaptor.forClass(List.class);

    verify(mockView).displayShows(argument.capture(), eq(expectedCategory));
  }

  @Test public void onTVCategorySecondaryResponseCallsPopulateSecondaryCategories() throws Exception {
    MediaContainer mediaContainer = new MediaContainer();
    String expectedKey = RandomStringUtils.randomAlphanumeric(10);
    String expectedCategory = RandomStringUtils.randomAlphanumeric(5);
    TVCategorySecondaryEvent expectedEvent =
        new TVCategorySecondaryEvent(mediaContainer, expectedKey, expectedCategory);

    presenter.onTVCategorySecondaryResponse(expectedEvent);

    ArgumentCaptor<List> argument = ArgumentCaptor.forClass(List.class);

    verify(mockView).populateSecondaryCategories(argument.capture(), eq(expectedCategory));
  }

  @Override public void installTestModules() {
    scope.installTestModules(new TestingModule(), new TestModule());
  }

  public class TestModule extends Module {

    public TestModule() {
      bind(JobManager.class).toInstance(mockJobManager);
      bind(SerenityClient.class).toInstance(mockSerenityClient);
      bind(EventBus.class).toInstance(mockEventBus);
    }
  }
}