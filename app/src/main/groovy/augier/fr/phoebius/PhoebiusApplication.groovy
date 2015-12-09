package augier.fr.phoebius


import android.app.Application
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.res.Resources
import android.os.IBinder
import android.util.Log
import augier.fr.phoebius.core.ConfigManager
import augier.fr.phoebius.core.MusicService
import augier.fr.phoebius.core.NotificationPlayer
import augier.fr.phoebius.utils.SongList
import com.arasthel.swissknife.SwissKnife
import com.arasthel.swissknife.annotations.OnBackground
import groovy.transform.CompileStatic

@CompileStatic
public class PhoebiusApplication extends Application implements ServiceConnection
{
    private static PhoebiusApplication instance
    private MusicService musicService

    @Override
    void onCreate()
    {
        super.onCreate()
        instance = this;
        Runtime.runtime.addShutdownHook(new ShutdownHook(this))
        def intent = new Intent(context, MusicService.class)
        context.bindService(intent, this, BIND_AUTO_CREATE)
    }

    @Override
    void onServiceConnected(ComponentName componentName, IBinder iBinder)
    {
        MusicService.MusicBinder binder = iBinder as MusicService.MusicBinder
        musicService = binder.service
    }

    @Override
    void onServiceDisconnected(ComponentName componentName){}

    private void dumpConfig()
    {
        SongList.INSTANCE.dispose()
        ConfigManager.INSTANCE.dump()
    }

    public static Context getContext(){ return instance.applicationContext }

    public static MusicService getMusicService(){ return instance.@musicService }

    public static Resources getResources(){ return instance.applicationContext.resources }

    public static void save(){ instance.dumpConfig() }

    private class ShutdownHook extends Thread
    {
        PhoebiusApplication application

        ShutdownHook(PhoebiusApplication instance){ application = instance }

        @Override void run()
        {
            application.dumpConfig()
            NotificationPlayer.INSTANCE.cancel()
        }
    }
}