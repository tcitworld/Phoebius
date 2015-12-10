package augier.fr.phoebius.utils


import android.os.Handler
import com.arasthel.swissknife.SwissKnife
import com.arasthel.swissknife.annotations.OnBackground

/**
 * This clas will handle a refreshing job executed regularly on a separate thread
 * Thread-safe
 */
public class Refresher
{
    private boolean isStarted = false
    private int refreshTime
    private Closure onRefresh
    private Handler handler = new Handler()
    private Runnable refresh = new Runnable(){
        @Override void run()
        {
            if(!isStarted){ return }
            onRefresh()
            handler.postDelayed(this, refreshTime)
        }
    }
    /**
     * Constructor
     * @param callback Job to be executed regularly
     * @param refreshTime Time between two refreshes in milliseconds ; by default 1000
     */
    public Refresher(Closure callback, int refreshTime = 1000)
    {
        this.refreshTime = refreshTime
        onRefresh = callback
    }

    @OnBackground private void threadedOnRefresh(){ handler.post(refresh) }

    /**
     *
     * Sets the calback to be executed
     * @param callback
     */
    public void setOnRefresh(Closure callback){ onRefresh = callback }

    /** Starts the job */
    public void start()
    {
        if(isStarted){ return }
        isStarted = true
        SwissKnife.runOnBackground(this, "threadedOnRefresh")
    }

    /** Stops the job */
    public void stop()
    {
        isStarted = false
        handler.removeCallbacks(refresh)
    }

    /**
     * Restart the job in case you want it to be sync with an event
     * If a job is currently being executed, it will be finished first
     * The time for the job to be restarted can be potentially long,
     * depending on the callbakc to be executed.
     */
    public void restart()
    {
        stop()
        start()
    }
}