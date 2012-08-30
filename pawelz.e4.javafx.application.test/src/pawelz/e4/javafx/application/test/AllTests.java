package pawelz.e4.javafx.application.test;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.jemmy.fx.SceneDock;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import pawelz.e4.javafx.application.test.internal.TestJfxApplication;
import pawelz.e4.javafx.application.test.parts.PartTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({ PartTest.class })
public class AllTests {

	@BeforeClass
	public static void setUp() throws Exception {
		// start application
		final IApplicationContext applicationContext = Activator.getDefault().getApplicationContext();
		TestJfxApplication app = new TestJfxApplication();

		startJfxApplication(app, applicationContext);
		waitForApplication();
	}

	/**
	 * starts Javafx-e4 application
	 * 
	 * @param app
	 * @param context
	 */
	private static void startJfxApplication(final IApplication app, final IApplicationContext context) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					app.start(context);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	/**
	 * waits for full start of application
	 */
	protected static void waitForApplication() {
		boolean status = false;
		do {
			try {
				new SceneDock();
				// javafx Scene is present so we can start testing our
				// application
				status = true;
			} catch (org.jemmy.TimeoutExpiredException e) {
				// javafx Scene is not present yet
			}

		} while (!status);
	}
}
