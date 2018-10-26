package us.nineworlds.serenity;

import android.content.Intent;

import org.assertj.android.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowActivity;

import us.nineworlds.serenity.ui.leanback.search.SearchActivity;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(RobolectricTestRunner.class)
public class AndroidTVTest {

    AndroidTV activity;
    @Before
    public void setUp() throws Exception {
        activity = Robolectric.buildActivity(AndroidTV.class).get();
    }

    @Test
    public void onSearchLaunchesIntendedActivity() {
        boolean result = activity.onSearchRequested();

        ShadowActivity shadowActivity = Shadows.shadowOf(activity);
        Intent nextStartedActivity = shadowActivity.getNextStartedActivity();

        assertThat(result).isTrue();
        Assertions.assertThat(nextStartedActivity).hasComponent(activity, SearchActivity.class);
    }


}