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

@CompileStatic
public class MainActivity extends FragmentActivity
{
	public static final String APP_NAME = R.string.app_name
	private static Resources resources

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// Class init
		super.onCreate(savedInstanceState)
		resources = getResources()
		contentView = R.layout.activity_main

    }

	@Override
	protected void onStart()
	{
		super.onStart()

		def frag = new MainPageFragment(supportFragmentManager)
		def contr = new PlayerControlFragment()
		supportFragmentManager.beginTransaction().add(R.id.mainFrame, frag).commit()
		supportFragmentManager.beginTransaction().add(R.id.mediaController, contr).commit()
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
				System.exit(0)
				break
			default:
				break
		}
		return super.onOptionsItemSelected(item)
	}

	public static MusicService getMusicService(){ return PhoebiusApplication.musicService }
	public static Context getApplicationContext(){ return PhoebiusApplication.context }
	public static Resources getApplicationResources(){ return resources }
}