package augier.fr.phoebius


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.MediaController
import augier.fr.phoebius.UI.AlbumListFragment
import augier.fr.phoebius.UI.PlayerControl
import augier.fr.phoebius.UI.SongListFragment
import augier.fr.phoebius.core.MusicService
import augier.fr.phoebius.core.MusicServiceConnection
import com.arasthel.swissknife.SwissKnife
import com.arasthel.swissknife.annotations.InjectView


public class MainActivity extends Activity
{
	public static final String APP_NAME = "Phoebius"
	@InjectView MediaController mediaController
	private MusicServiceConnection musicConnection
	private Intent playIntent
	private PlayerControl playerControl

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// Class init
		super.onCreate(savedInstanceState)
		contentView = R.layout.activity_main
		SwissKnife.inject(this)

		// Variables init
		/*
		 * TODO : Intencier dans un thread à part
		 */
		musicConnection = new MusicServiceConnection()
		musicConnection.serviceConnectedEvent = this.&onServiceConnected
		playerControl = new PlayerControl(musicConnection, mediaController)
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
	protected void onDestroy()
	{
		stopService(playIntent)
		musicConnection.destroy()
		super.onDestroy()
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		menuInflater.inflate(R.menu.main, menu)
		return true
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
				musicConnection.destroy()
				System.exit(0)
				break
		}
		return super.onOptionsItemSelected(item)
	}

	private void onServiceConnected()
	{
		if(musicService?.songList != null)
		{
			def frag = new AlbumListFragment(musicService)
			fragmentManager.beginTransaction().add(R.id.mainFrame, frag).commit()
		}
	}

	private MusicService getMusicService(){ return musicConnection.musicService }
}