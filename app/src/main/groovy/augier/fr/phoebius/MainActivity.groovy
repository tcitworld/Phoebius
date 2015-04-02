package augier.fr.phoebius

import android.app.ActionBar
import android.app.ActionBar.Tab
import android.app.ActionBar.TabListener
import android.app.Activity
import android.app.FragmentTransaction
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.support.v4.app.FragmentActivity
import android.support.v4.view.ViewPager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ListView
import android.widget.MediaController
import android.widget.MediaController.MediaPlayerControl
import augier.fr.phoebius.UI.FragmentAdapter
import augier.fr.phoebius.UI.SongAdapter
import augier.fr.phoebius.core.MusicService
import augier.fr.phoebius.core.MusicService.MusicBinder
import augier.fr.phoebius.utils.SongList
import com.arasthel.swissknife.SwissKnife
import com.arasthel.swissknife.annotations.InjectView
import com.arasthel.swissknife.annotations.OnItemClick

public class MainActivity extends FragmentActivity implements TabListener
{
	public static final String APP_NAME = R.string.app_name
	@InjectView MediaController mediaController
	private MusicServiceConnection musicConnection
	private Intent playIntent
	private PlayerControl playerControl
    private ActionBar actionbar;
    private ViewPager viewpager;
    private FragmentAdapter ft;

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

        viewpager = (ViewPager) findViewById(R.id.pager);
        ft = new FragmentAdapter(getSupportFragmentManager(), musicService);
        mainPagerInit();

    }

    public void mainPagerInit()
    {
        actionbar = getActionBar();
        viewpager.setAdapter(ft);
        actionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionbar.addTab(actionbar.newTab().setText(R.string.playlist_en).setTabListener(this));
        actionbar.addTab(actionbar.newTab().setText(R.string.album).setTabListener(this));
        actionbar.addTab(actionbar.newTab().setText(R.string.artiste).setTabListener(this));
        viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                actionbar.setSelectedNavigationItem(arg0);

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
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        viewpager.setCurrentItem(tab.getPosition());

    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
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