package osgi;

import gc.PeriodicGC;

import java.util.Date;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;

public class Activator implements BundleActivator, ServiceListener {
	private PeriodicGC gc;
	
    public void start(BundleContext bundleContext) throws Exception {
        System.out.println(String.format("Starting %s with %s on %s", this, bundleContext, new Date()));
        try {
            gc = new PeriodicGC().initialize();
        }catch(Throwable t){
            t.printStackTrace();
        }
        bundleContext.addServiceListener(this);
    }

    public void stop(BundleContext bundleContext) throws Exception {
        System.out.println(String.format("### Stopping %s with %s on %s",this, bundleContext, new Date()));
        try {
            gc.stopGC();
        }catch(Throwable t){
            t.printStackTrace();
        }
        bundleContext.removeServiceListener(this);
    }

    public void serviceChanged(ServiceEvent serviceEvent) {
        System.out.println(String.format("%s status changed to %s at %s", this, serviceEvent.getType(), new Date()));
    }

}
