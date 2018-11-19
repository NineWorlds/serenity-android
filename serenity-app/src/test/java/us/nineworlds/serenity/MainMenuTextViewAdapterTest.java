package us.nineworlds.serenity;

import java.util.Collections;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
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

  }
}