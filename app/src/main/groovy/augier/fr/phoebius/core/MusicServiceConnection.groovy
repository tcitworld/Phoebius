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
	private Context context
	private ListView songView

	public MusicServiceConnection(Context c, ListView v = null)
	{
		context = c
		songView = v
		musicService = null
		musicBound = false
	}

	@Override
	void onServiceConnected(ComponentName componentName, IBinder iBinder)
	{
		MusicBinder binder = iBinder as MusicBinder
		musicService = binder.service
		musicBound = true

		if(songView != null)
		{
			SongAdapter songAdapter = new SongAdapter(context, musicService.songList)
			songView.setAdapter(songAdapter)
		}
	}

	@Override
	void onServiceDisconnected(ComponentName componentName){ musicBound = false }

	void destroy(){ musicService = null }

	MusicService getMusicService(){ return musicService }
	boolean getMusicBound(){ return musicBound }
}
