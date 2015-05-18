package augier.fr.phoebius


import android.app.Application
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.Resources
import android.media.AudioManager
import augier.fr.phoebius.core.ConfigManager
import augier.fr.phoebius.core.MediaButtonReceiver
import augier.fr.phoebius.core.MusicService
import augier.fr.phoebius.core.MusicServiceConnection
import augier.fr.phoebius.core.NotificationPlayer
import augier.fr.phoebius.utils.SongList
import groovy.transform.CompileStatic

@CompileStatic
public class PhoebiusApplication extends Application
{
	public static final String APP_NAME = R.string.app_name
	private static Context context
	private static MusicService musicService
	private static Resources resources
	private static ConfigManager configManager = new ConfigManager()
	private static Closure terminate
	private MusicServiceConnection musicCo

	@Override
	public void onCreate()
	{
		super.onCreate()
		terminate = this.&onTerminate
		context = applicationContext
		resources = getResources()
		def callback = { musicService = musicCo.musicService }
		musicCo = new MusicServiceConnection(this, callback)
	}

	@Override
	void onTerminate()
	{
		SongList.instance.finalize()
		configManager.dump()
		NotificationPlayer.instance.cancel()
		super.onTerminate()
		System.exit(0)
	}

	public static Context getContext(){ return context }
	public static MusicService getMusicService(){ return musicService }
	public static Resources getResources(){ return context.resources }
	public static ConfigManager getConfigManager(){ return configManager }
	public static Closure getTerminate(){ return terminate }
}
