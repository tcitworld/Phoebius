package augier.fr.phoebius


import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import augier.fr.phoebius.core.ConfigManager
import augier.fr.phoebius.core.MusicService
import augier.fr.phoebius.core.MusicServiceConnection
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
	private MusicServiceConnection musicCo

	@Override
	public void onCreate()
	{
		super.onCreate()
		context = applicationContext
		resources = getResources()
		musicCo = new MusicServiceConnection()
		musicCo.callback = { musicService = musicCo.musicService }
		def intent = new Intent(this, MusicService.class)
		bindService(intent, musicCo, BIND_AUTO_CREATE)
	}

	@Override
	void onTerminate()
	{
		SongList.instance.finalize()
		configManager.dump()
		super.onTerminate()
		System.exit(0)
	}

	public static Context getContext(){ return context }
	public static MusicService getMusicService(){ return musicService }
	public static Resources getResources(){ return resources }
	public static ConfigManager getConfigManager(){ return configManager }
}
