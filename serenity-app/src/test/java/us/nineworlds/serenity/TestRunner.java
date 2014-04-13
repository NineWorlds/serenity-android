package us.nineworlds.serenity;

import java.lang.reflect.Method;

import org.junit.runners.model.InitializationError;
import org.robolectric.AndroidManifest;
import org.robolectric.DefaultTestLifecycle;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.TestLifecycle;

import android.app.Application;

public class TestRunner extends RobolectricTestRunner {

	public TestRunner(final Class<?> testClass) throws InitializationError {
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