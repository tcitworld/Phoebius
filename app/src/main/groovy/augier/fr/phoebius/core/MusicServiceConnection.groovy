package augier.fr.phoebius.core


import android.os.IBinder
import android.content.ComponentName
import android.content.ServiceConnection
import augier.fr.phoebius.core.MusicService.MusicBinder


public class MusicServiceConnection implements ServiceConnection
{
	private MusicService musicService
	private boolean musicBound
	private Closure serviceConnectedEvent = {}

	public MusicServiceConnection()
	{
		musicService = null
		musicBound = false
	}

	@Override
	void onServiceConnected(ComponentName componentName, IBinder iBinder)
	{
		MusicBinder binder = iBinder as MusicBinder
		musicService = binder.service
		musicBound = true

		serviceConnectedEvent()
	}

	@Override
	void onServiceDisconnected(ComponentName componentName){ musicBound = false }

	void destroy(){ musicService = null }

	MusicService getMusicService(){ return musicService }
	boolean getMusicBound(){ return musicBound }
	void setServiceConnectedEvent(Closure serviceConnectedEvent)
		{ this.serviceConnectedEvent = serviceConnectedEvent }
}
