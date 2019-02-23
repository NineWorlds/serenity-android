package us.nineworlds.serenity;

import java.util.Collections;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.quality.Strictness;
import org.robolectric.RobolectricTestRunner;
import toothpick.config.Module;
import us.nineworlds.serenity.core.menus.MenuItem;
import us.nineworlds.serenity.test.InjectingTest;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(RobolectricTestRunner.class)
public class MainMenuTextViewAdapterTest extends InjectingTest {

  MainMenuTextViewAdapter adapter;

  @Before
  public void setUp() throws Exception {
    super.setUp();

    adapter = new MainMenuTextViewAdapter();
  }

  @Test
  public void getItemAtPositionReturnsItemAtPositionZeroWhenPositionIsNegative() {
    MainMenuTextViewAdapter.menuItems = Collections.singletonList(new MenuItem());
    MenuItem result = adapter.getItemAtPosition(-1);

    assertThat(result).isNotNull();
  }

  @Override public void installTestModules() {
    scope.installTestModules(new TestingModule(), new TestModule());
  }

  public class TestModule extends Module {

    public TestModule() {
      bind(SharedPreferences.class).toInstance(PreferenceManager.getDefaultSharedPreferences(ApplicationProvider.getApplicationContext()));
    }
  }
}