package augier.fr.phoebius.UI


import android.app.ActionBar
import android.app.ActionBar.Tab
import android.app.ActionBar.TabListener
import android.app.FragmentTransaction
import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import augier.fr.phoebius.R
import augier.fr.phoebius.core.MusicService
import com.arasthel.swissknife.SwissKnife
import com.arasthel.swissknife.annotations.InjectView

public class MainPageFragment extends Fragment implements TabListener
{
	@InjectView ViewPager mainPager
	private MusicService musicService
	private ActionBar actionBar
	private PagerAdaptater fragmentAdapter

	public MainPageFragment(FragmentManager fm , MusicService ms)
	{
		super()
		musicService = ms
		fragmentAdapter = new PagerAdaptater(fm);
	}

	@Override
	View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.fragment_main_page, container, false);
		SwissKnife.inject(this, view)

		mainPager.setAdapter(fragmentAdapter);

		actionBar = activity.actionBar
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		actionBar.addTab(actionBar.newTab().setText(R.string.playlist_en).setTabListener(this))
		actionBar.addTab(actionBar.newTab().setText(R.string.album).setTabListener(this))
		actionBar.addTab(actionBar.newTab().setText(R.string.artiste).setTabListener(this))

		mainPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0){ actionBar.setSelectedNavigationItem(arg0) }
			@Override public void onPageScrolled(int arg0, float arg1, int arg2){}
			@Override public void onPageScrollStateChanged(int arg0){}
		})

		return view
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft)
		{ mainPager.setCurrentItem(tab.getPosition()) }

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft){}

	class PagerAdaptater extends FragmentPagerAdapter
	{

		private static final int NUM_ITEMS = 2

		public PagerAdaptater(FragmentManager fm){ super(fm) }

		@Override
		public Fragment getItem(int arg0)
		{
			switch (arg0) {
				case 0:
					return new SongListFragment(musicService)
				case 1:
					return new AlbumListFragment(musicService)
				default:
					break;
			}
			return null;
		}

		@Override public int getCount(){ return NUM_ITEMS }
	}
}
