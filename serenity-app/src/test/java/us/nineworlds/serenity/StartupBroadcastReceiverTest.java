package us.nineworlds.serenity;

import android.content.Intent;
import android.content.SharedPreferences;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowApplication;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import us.nineworlds.serenity.injection.modules.AndroidModule;
import us.nineworlds.serenity.injection.modules.SerenityModule;
import us.nineworlds.serenity.test.InjectingTest;

import static org.assertj.android.api.Assertions.assertThat;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.robolectric.RuntimeEnvironment.application;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class StartupBroadcastReceiverTest extends InjectingTest {

    @Mock
    SharedPreferences mockSharedPrefences;

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

    @Override
    public List<Object> getModules() {
        List<Object> module = new ArrayList<>();
        module.add(new AndroidModule(application));
        module.add(new TestingModule());
        module.add(new TestModule());
        return module;
    }


    @Module(addsTo = AndroidModule.class,
            includes = SerenityModule.class,
            library = true,
            overrides = true,
            injects = StartupBroadcastReceiverTest.class)
    public class TestModule {

        @Provides
        @Singleton
        SharedPreferences providesSharedPreferences() {
            return mockSharedPrefences;
        }

    }
}