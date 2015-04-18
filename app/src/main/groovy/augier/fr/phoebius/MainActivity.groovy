package augier.fr.phoebius


import android.app.ActionBar
import android.app.ActionBar.Tab
import android.app.ActionBar.TabListener
import android.app.Activity
import android.app.FragmentTransaction
import android.content.Intent
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.view.Menu
import android.view.MenuItem
import android.widget.MediaController
import augier.fr.phoebius.UI.AlbumListFragment
import augier.fr.phoebius.UI.FragmentAdapter
import augier.fr.phoebius.UI.PlayerControl
import augier.fr.phoebius.core.MusicService
import augier.fr.phoebius.core.MusicServiceConnection
import com.arasthel.swissknife.SwissKnife
import com.arasthel.swissknife.annotations.InjectView

public class MainActivity extends Activity implements TabListener
{
	public static final String APP_NAME = R.string.app_name
	@InjectView MediaController mediaController
	@InjectView ViewPager mainPager;
	private MusicServiceConnection musicConnection
	private Intent playIntent
	private PlayerControl playerControl
	private ActionBar actionBar;
    private FragmentAdapter fragmentAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// Class init
		super.onCreate(savedInstanceState)
		//contentView = R.layout.activity_main
		SwissKnife.inject(this)

		// Variables init
		/*
		 * TODO : Intencier dans un thread à part
		 */
		musicConnection = new MusicServiceConnection()
		musicConnection.serviceConnectedEvent = this.&onServiceConnected
		playerControl = new PlayerControl(musicConnection, mediaController)

        mainPagerInit();

    }

    public void mainPagerInit()
    {
	    fragmentAdapter = new FragmentAdapter(getSupportFragmentManager(), musicService);
        actionBar = getActionBar();
        mainPager.setAdapter(fragmentAdapter);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.addTab(actionBar.newTab().setText(R.string.playlist_en).setTabListener(this));
        actionBar.addTab(actionBar.newTab().setText(R.string.album).setTabListener(this));
        actionBar.addTab(actionBar.newTab().setText(R.string.artiste).setTabListener(this));
        mainPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                actionBar.setSelectedNavigationItem(arg0);

            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
                // TODO Auto-generated method stub

            }
        });
    }

    @Override
    public void onTabReselected(Tab tab, FragmentTransaction ft) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onTabSelected(Tab tab, FragmentTransaction ft) {
        mainPager.setCurrentItem(tab.getPosition());

    }

    @Override
    public void onTabUnselected(Tab tab, FragmentTransaction ft) {
        // TODO Auto-generated method stub

    }
	@Override
	protected void onStart()
	{
		super.onStart()

		Activity thisActivity = this
		if(playIntent == null)
		{
			playIntent = new Intent(thisActivity, MusicService.class)
			bindService(playIntent, musicConnection, BIND_AUTO_CREATE)
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