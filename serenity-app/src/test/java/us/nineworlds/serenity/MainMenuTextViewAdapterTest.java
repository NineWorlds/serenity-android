package us.nineworlds.serenity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dagger.Module;
import us.nineworlds.serenity.core.menus.MenuItem;
import us.nineworlds.serenity.injection.modules.AndroidModule;
import us.nineworlds.serenity.injection.modules.SerenityModule;
import us.nineworlds.serenity.test.InjectingTest;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.robolectric.RuntimeEnvironment.application;

@RunWith(RobolectricTestRunner.class)
public class MainMenuTextViewAdapterTest  extends InjectingTest {

    MainMenuTextViewAdapter adapter;

    @Before
    public void setUp() throws Exception  {
        super.setUp();

        adapter = new MainMenuTextViewAdapter();
    }

    @Test
    public void getItemAtPositionReturnsItemAtPositionZeroWhenPositionIsNegative() {
        MainMenuTextViewAdapter.menuItems = Collections.singletonList(new MenuItem());
        MenuItem result = adapter.getItemAtPosition(-1);

        assertThat(result).isNotNull();
    }

    @Override
    public List<Object> getModules() {
        List<Object> modules = new ArrayList<Object>();
        modules.add(new AndroidModule(application));
        modules.add(new TestModule());
        return modules;
    }

    @Module(library = true,
            includes = SerenityModule.class,
            addsTo = AndroidModule.class,
            injects = {MainMenuTextViewAdapter.class, MainMenuTextViewAdapterTest.class})
    public class TestModule {

    }
}