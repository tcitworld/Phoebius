package augier.fr.phoebius.core


import android.content.ComponentName
import android.content.Context
import android.content.ServiceConnection
import android.os.IBinder
import android.widget.ListView
import augier.fr.phoebius.UI.SongAdapter;
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
