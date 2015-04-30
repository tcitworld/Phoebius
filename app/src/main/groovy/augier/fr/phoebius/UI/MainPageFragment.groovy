package augier.fr.phoebius.UI


import android.app.ActionBar.Tab
import android.app.ActionBar.TabListener
import android.app.FragmentTransaction
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.PagerTitleStrip
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
	@InjectView PagerTitleStrip mainPagerTitleStrip
	private MusicService musicService
	private PagerAdaptater fragmentAdapter

	public MainPageFragment(FragmentManager fm , MusicService ms)
	{
		super()
		musicService = ms
		fragmentAdapter = new PagerAdaptater(fm)
	}

	@Override
	View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.fragment_main_page, container, false)
		SwissKnife.inject(this, view)

		mainPager.setAdapter(fragmentAdapter)
		return view
	}

	@Override public void onTabReselected(Tab tab, FragmentTransaction ft) {}
	@Override public void onTabUnselected(Tab tab, FragmentTransaction ft){}
	@Override public void onTabSelected(Tab tab, FragmentTransaction ft)
	{ mainPager.setCurrentItem(tab.getPosition()) }

	class PagerAdaptater extends FragmentStatePagerAdapter
	{
		private static int[] HEADER_ITEMS = [
			R.string.playlist_en,
			R.string.album
		]

		private Fragment[] FRAGMENTS = [
				new SongListFragment(musicService),
				new AlbumListFragment(musicService)
		]

		public PagerAdaptater(FragmentManager fm){ super(fm) }

		@Override public Fragment getItem(int arg0){ return FRAGMENTS[arg0] }
		@Override public int getCount(){ return HEADER_ITEMS.length }
		@Override CharSequence getPageTitle(int position){ return getText(HEADER_ITEMS[position]) }
	}
}
