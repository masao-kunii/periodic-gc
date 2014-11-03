package gc;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class PeriodicGC {
    public static final String PERIODIC_GC_INTERVAL_MINUTES_KEY = "plugin.periodic.gc.interval.min";
    private Timer timer;
    private long intervalMillis = 3600000;
    
    public PeriodicGC initialize() {
        System.out.println(String.format("Initializing %s on %s",this, new Date()));
        timer = new Timer();
        try {
            String min = System.getProperty(PERIODIC_GC_INTERVAL_MINUTES_KEY, "60");
            this.intervalMillis = Long.parseLong(min) * 60 * 1000;
        } catch (Throwable t) {
            t.printStackTrace();
        } finally {
            startGC(this.intervalMillis);
        }
        return this;
    }

    public void startGC(long intervalMillis) {
        this.intervalMillis = intervalMillis;
        System.out.println(String.format("Starting %s on %s", this, new Date()));
        timer.schedule(new GCTask(), this.intervalMillis, this.intervalMillis);
    }

    public void stopGC() {
        System.out.println(String.format("Stopping %s on %s", this, new Date()));
        timer.cancel();
        System.out.println(String.format("%s stopped at %s", this, new Date()));
    }

    static class GCTask extends TimerTask {
        @Override
        public void run() {
            long start = System.currentTimeMillis();
            try {
                System.out.println(String.format("%s started at %s", this, new Date()));
                System.gc();
            } catch (Exception e) {
                System.err.println(e.getMessage());
            } finally {
                System.out.println(String.format("%s ended in %s millis", this, (System.currentTimeMillis() - start)));
            }

        }
    }
}
