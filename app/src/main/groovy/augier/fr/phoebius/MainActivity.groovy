package augier.fr.phoebius


import android.app.Activity
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.view.Menu
import android.view.MenuItem
import android.widget.ListView
import augier.fr.phoebius.UI.SongAdapter
import augier.fr.phoebius.core.MusicService
import augier.fr.phoebius.core.MusicService.MusicBinder
import augier.fr.phoebius.utils.SongList
import com.arasthel.swissknife.SwissKnife
import com.arasthel.swissknife.annotations.InjectView
import com.arasthel.swissknife.annotations.OnItemClick

public class MainActivity extends Activity
{
	public static final String APP_NAME = "Phoebius"
	@InjectView ListView songView
	private SongList songList
	private MusicService musicService
	private Intent playIntent
	private boolean musicBound = false
	private MusicServiceConnection musicConnection

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// Class init
		super.onCreate(savedInstanceState)
		contentView = R.layout.activity_main
		SwissKnife.inject(this)

		// Variables init
		songList = new SongList(contentResolver)
		musicConnection = new MusicServiceConnection()

		// UI init
		SongAdapter songAdapter = new SongAdapter(this, songList.songList)
		songView.setAdapter(songAdapter)
	}

	@Override
	protected void onStart()
	{
		super.onStart()

		if(playIntent == null)
		{
			playIntent = new Intent(this, MusicService.class)
			bindService(playIntent, musicConnection, BIND_AUTO_CREATE)
			startService(playIntent)
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		menuInflater.inflate(R.menu.main, menu)
		return true
	}

	@OnItemClick(R.id.songView)
	public void onItemClick(int position)
	{
		musicService.songPos = position
		musicService.playSong()
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch(item.itemId)
		{
			case R.id.action_shuffle:
				break
			case R.id.action_end:
				stopService(playIntent)
				musicService = null
				System.exit(0)
				break
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onDestroy()
	{
		stopService(playIntent);
		musicService = null;
		super.onDestroy();
	}

	private class MusicServiceConnection implements ServiceConnection
	{
		@Override
		void onServiceConnected(ComponentName componentName, IBinder iBinder)
		{
			MusicBinder binder = iBinder as MusicBinder
			musicService = binder.service
			musicService.songList = songList
			musicBound = true
		}

		@Override
		void onServiceDisconnected(ComponentName componentName){ musicBound = false }
	}
}