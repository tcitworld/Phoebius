package augier.fr.phoebius.core


import android.os.IBinder
import android.content.ComponentName
import android.content.ServiceConnection
import augier.fr.phoebius.core.MusicService.MusicBinder


/**
 * Class for binding a class to the {@link MusicService}
 */
public class MusicServiceConnection implements ServiceConnection
{
	/**
	 * Service to be binded to
	 */
	public MusicService musicService
	private Closure callback

	@Override
	void onServiceConnected(ComponentName componentName, IBinder iBinder)
	{
		MusicBinder binder = iBinder as MusicBinder
		musicService = binder.service

		callback()
	}

	@Override
	void onServiceDisconnected(ComponentName componentName){}
	void setCallback(Closure c){ callback = c }
}
