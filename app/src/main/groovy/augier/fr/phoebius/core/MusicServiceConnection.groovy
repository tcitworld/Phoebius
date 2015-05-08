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
	private MusicService musicService

	/**
	 * Callback to be executed when the service is binded
	 */
	private Closure serviceConnectedEvent = {}

	/**
	 * Does nothing
	 */
	public MusicServiceConnection(){}

	@Override
	void onServiceConnected(ComponentName componentName, IBinder iBinder)
	{
		MusicBinder binder = iBinder as MusicBinder
		musicService = binder.service

		serviceConnectedEvent()
	}

	@Override
	void onServiceDisconnected(ComponentName componentName){}

	/**
	 * Dereferences the service
	 */
	void destroy(){ musicService = null }

	MusicService getMusicService(){ return musicService }
	/**
	 * Sets the callback to be executed when the service is connected
	 *
	 * The callback will be called at the end of onServiceConnected()
	 *
	 * @param serviceConnectedEvent
	 */
	void setServiceConnectedEvent(Closure serviceConnectedEvent)
		{ this.serviceConnectedEvent = serviceConnectedEvent }
}
