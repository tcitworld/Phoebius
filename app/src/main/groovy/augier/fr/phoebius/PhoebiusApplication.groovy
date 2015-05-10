package augier.fr.phoebius;


import android.app.Application
import android.app.Service;
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.util.Log
import augier.fr.phoebius.core.MusicService
import augier.fr.phoebius.core.MusicServiceConnection;


public class PhoebiusApplication extends Application
{
	private static Context context
	private static MusicService musicService
	private static Resources resources
	private def musicCo

	@Override
	public void onCreate()
	{
		super.onCreate()
		context = applicationContext
		resources = getResources()
		musicCo = new MusicServiceConnection()
		musicCo.callback = this.&callback
		def intent = new Intent(this, MusicService.class)
		bindService(intent, musicCo, BIND_AUTO_CREATE)
	}

	public static Context getContext(){ return context }
	public static MusicService getMusicService(){ return musicService }

	private def callback(){
		musicService = musicCo.musicService
		Log.e(this.class.toString(), "Binded")
	}
}
