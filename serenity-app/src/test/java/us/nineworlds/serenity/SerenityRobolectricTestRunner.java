package us.nineworlds.serenity;

import java.lang.reflect.Method;

import org.junit.runners.model.InitializationError;
import org.robolectric.AndroidManifest;
import org.robolectric.DefaultTestLifecycle;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.TestLifecycle;

import android.app.Application;

/**
 * Custom extension to make sure the TestApplication is loaded instead of the
 * default SerenityApplication. This the Google Analytics to be disabled as part
 * of the tests.
 * 
 * @author dcarver
 * 
 */
public class SerenityRobolectricTestRunner extends RobolectricTestRunner {

	public SerenityRobolectricTestRunner(final Class<?> testClass)
			throws InitializationError {
		super(testClass);
	}

	@Override
	protected Class<? extends TestLifecycle> getTestLifecycleClass() {
		return MyTestLifecycle.class;
	}

	public static class MyTestLifecycle extends DefaultTestLifecycle {
		@Override
		public Application createApplication(final Method method,
				final AndroidManifest appManifest) {
			return new TestApplication();
		}
	}

}