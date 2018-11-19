package us.nineworlds.serenity;

import android.content.Intent;
import android.content.SharedPreferences;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowApplication;
import toothpick.config.Module;
import us.nineworlds.serenity.core.util.AndroidHelper;
import us.nineworlds.serenity.test.InjectingTest;

import static org.assertj.android.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.robolectric.RuntimeEnvironment.application;

@RunWith(RobolectricTestRunner.class)
public class StartupBroadcastReceiverTest extends InjectingTest {

  @Mock
  SharedPreferences mockSharedPrefences;

  @Mock
  AndroidHelper mockAndroidHelper;

  StartupBroadcastReceiver receiver;

  @Before
  public void setUp() throws Exception {
    initMocks(this);
    super.setUp();

    receiver = new StartupBroadcastReceiver();
  }

  @Test
  public void onReceiveDoesNothingWhenIntentActionIsNull() {
    Intent intent = new Intent();

    receiver.onReceive(application.getApplicationContext(), intent);

    verifyZeroInteractions(mockSharedPrefences);
    Intent nextStartedActivity = ShadowApplication.getInstance().getNextStartedActivity();
    assertThat(nextStartedActivity).isNull();
  }

  @Test
  public void onReceiveDoesNothingWhenIntentActionIsIsNotBootCompleted() {
    Intent intent = new Intent();
    String action = RandomStringUtils.randomAlphanumeric(10);
    intent.setAction(action);

    receiver.onReceive(application.getApplicationContext(), intent);

    verifyZeroInteractions(mockSharedPrefences);
    Intent nextStartedActivity = ShadowApplication.getInstance().getNextStartedActivity();
    assertThat(nextStartedActivity).isNull();
  }

  @Test
  public void onReceiveHandlesBootCompleted() {
    Intent intent = new Intent();
    String action = "android.intent.action.BOOT_COMPLETED";
    intent.setAction(action);

    doReturn(true).when(mockSharedPrefences).getBoolean("serenity_boot_startup", false);

    receiver.onReceive(application.getApplicationContext(), intent);

    Intent nextStartedActivity = ShadowApplication.getInstance().peekNextStartedActivity();
    assertThat(nextStartedActivity).isNotNull();

    verify(mockSharedPrefences).getBoolean("serenity_boot_startup", false);
  }

  @Test
  public void onReceiveHandelsLeanBackSupport() {
    Intent intent = new Intent();
    String action = "android.intent.action.BOOT_COMPLETED";
    intent.setAction(action);

    doReturn(true).when(mockSharedPrefences).getBoolean("serenity_boot_startup", false);
    doReturn(true).when(mockSharedPrefences).getBoolean("androidtv_recommendation_ondeck", false);
    doReturn(true).when(mockAndroidHelper).isLeanbackSupported();

    receiver.onReceive(application.getApplicationContext(), intent);

    Intent nextStartedActivity = ShadowApplication.getInstance().peekNextStartedActivity();
    assertThat(nextStartedActivity).isNotNull();

    verify(mockSharedPrefences).getBoolean("serenity_boot_startup", false);
    verify(mockSharedPrefences).getBoolean("androidtv_recommendation_ondeck", false);
    verify(mockAndroidHelper).isLeanbackSupported();
  }

  @Override public void installTestModules() {
    scope.installTestModules(new TestingModule(), new TestModule());
  }

  public class TestModule extends Module {

    public TestModule() {
      bind(SharedPreferences.class).toInstance(mockSharedPrefences);
      bind(AndroidHelper.class).toInstance(mockAndroidHelper);
    }
  }
}