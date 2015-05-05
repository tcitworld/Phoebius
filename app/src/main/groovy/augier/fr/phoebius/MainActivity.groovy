package augier.fr.phoebius


import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.view.Menu
import android.view.MenuItem
import augier.fr.phoebius.UI.MainPageFragment
import augier.fr.phoebius.UI.PlayerControlFragment
import augier.fr.phoebius.core.MusicService
import augier.fr.phoebius.core.MusicServiceConnection
import augier.fr.phoebius.utils.SongList
import groovy.transform.CompileStatic
import java.sql.ResultSet

@CompileStatic
public class MainActivity extends FragmentActivity
{
	public static final String APP_NAME = R.string.app_name
	private static Context context
	private static Resources resources
	private static MusicServiceConnection musicConnection
	private Intent playIntent

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// Class init
		super.onCreate(savedInstanceState)
		context = this
		resources = getResources()
		contentView = R.layout.activity_main

		musicConnection = new MusicServiceConnection()
		musicConnection.serviceConnectedEvent = this.&onServiceConnected
		playIntent = new Intent(this, MusicService.class)
		bindService(playIntent, musicConnection, BIND_AUTO_CREATE)
    }

	@Override
	protected void onStart(){ super.onStart() }

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
			case R.id.action_end:
				stopService(playIntent)
				musicConnection.destroy()
				System.exit(0)
				break
			default:
				break
		}
		return super.onOptionsItemSelected(item)
	}

	/**
	 * Callback executed when the service is conneted
	 */
	private void onServiceConnected()
	{
		if(SongList.instance?.currSongList != null)
		{
			def frag = new MainPageFragment(supportFragmentManager)
			def contr = new PlayerControlFragment()
			supportFragmentManager.beginTransaction().add(R.id.mainFrame, frag).commit()
			supportFragmentManager.beginTransaction().add(R.id.mediaController, contr).commit()
		}
	}

	public static MusicService getMusicService(){ return musicConnection.musicService }
	public static Context getApplicationContext(){ return context }
	public static Resources getApplicationResources(){ return resources }
}