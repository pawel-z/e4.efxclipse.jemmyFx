package pawelz.e4.javafx.application;

import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.osgi.service.datalocation.Location;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Filter;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;

public class Activator implements BundleActivator {

	private static BundleContext context;
	// The shared instance
	private static Activator plugin;

	private ServiceTracker locationTracker;

	static BundleContext getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext
	 * )
	 */
	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		plugin = this;
	}

	public static Activator getDefault() {
		return plugin;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
		plugin = null;
	}

	public Location getInstanceLocation() {
		if (locationTracker == null) {
			Filter filter = null;
			try {
				filter = context.createFilter(Location.INSTANCE_FILTER);
			} catch (InvalidSyntaxException e) {
				// ignore this. It should never happen as we have tested the
				// above format.
			}
			locationTracker = new ServiceTracker(context, filter, null);
			locationTracker.open();
		}
		return (Location) locationTracker.getService();
	}

	@SuppressWarnings("unchecked")
	public IApplicationContext getApplicationContext() {
		ServiceReference<IApplicationContext>[] ref;
		try {
			ref = (ServiceReference<IApplicationContext>[]) context.getServiceReferences(IApplicationContext.class.getName(),
					"(eclipse.application.type=main.thread)");
		} catch (InvalidSyntaxException e) {
			return null;
		}
		if (ref == null || ref.length == 0)
			return null;
		// assumes the application context is available as a service
		IApplicationContext result = context.getService(ref[0]);
		if (result != null) {
			context.ungetService(ref[0]);
			return result;
		}
		return null;
	}
}
