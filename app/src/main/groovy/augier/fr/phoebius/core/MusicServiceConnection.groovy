package augier.fr.phoebius.core


import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.content.ComponentName
import android.content.ServiceConnection
import augier.fr.phoebius.core.MusicService.MusicBinder
import com.arasthel.swissknife.SwissKnife
import com.arasthel.swissknife.annotations.OnBackground
import groovy.transform.CompileStatic


/**
 * Class for binding a class to the {@link MusicService}
 */
@CompileStatic
public class MusicServiceConnection implements ServiceConnection
{
	/**
	 * Service to be binded to
	 */
	public MusicService musicService
	private Closure callback
	private Context context

	MusicServiceConnection(Context c1, Closure c2)
	{
		super()
		context = c1
		callback = c2
		SwissKnife.runOnBackground(this, "backgroundBind")
	}

	@Override
	void onServiceConnected(ComponentName componentName, IBinder iBinder)
	{
		MusicBinder binder = iBinder as MusicBinder
		musicService = binder.service

		callback()
	}

	@Override
	void onServiceDisconnected(ComponentName componentName){}

	@OnBackground
	private void backgroundBind()
	{
		def intent = new Intent(context, MusicService.class)
		context.bindService(intent, this, context.BIND_AUTO_CREATE)
	}
}
