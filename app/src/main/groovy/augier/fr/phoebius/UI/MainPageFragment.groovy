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
import augier.fr.phoebius.MainActivity
import augier.fr.phoebius.PhoebiusApplication
import augier.fr.phoebius.R
import augier.fr.phoebius.core.MusicService
import com.arasthel.swissknife.SwissKnife
import com.arasthel.swissknife.annotations.InjectView


/**
 * Fragment for the main view of the application. Manages the {@link ViewPager} and the tabs.
 *
 * This class uses <a href="https://github.com/Arasthel/SwissKnife">SwissKnife</a>.
 * The views are injected in the {@link MainPageFragment#onCreateView onCreateView} method
 */
public class MainPageFragment extends Fragment implements TabListener
{
	@InjectView ViewPager mainPager
	private PagerAdaptater fragmentAdapter

	/**
	 * Constructor
	 * @param fm Fragment manager from main activity (just use {@link MainActivity#getSupportFragmentManager}
	 * @param ms Music service from the main activity (just use {@link PhoebiusApplication#getMusicService}
	 */
	public MainPageFragment(FragmentManager fm)
	{
		super()
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

	private MusicService getMusicService(){ return PhoebiusApplication.musicService }

	@Override public void onTabReselected(Tab tab, FragmentTransaction ft){}
	@Override public void onTabUnselected(Tab tab, FragmentTransaction ft){}
	@Override public void onTabSelected(Tab tab, FragmentTransaction ft)
		{ mainPager.setCurrentItem(tab.getPosition()) }

	/**
	 * Adaptater to manage displaying of the tabs
	 */
	class PagerAdaptater extends FragmentStatePagerAdapter
	{
		/**
		 * Title of each tab
		 * 
		 * The title have to be ordered the same order than
		 * the corresponding fragments
		 *
		 * @see {@link #FRAGMENTS}
		 */
		private int[] HEADER_ITEMS = [
			R.string.titles,
			R.string.album,
			R.string.playlist
		]

		/**
		 * {@link Fragment} to be displayed for each tab
		 *
		 * The fragments have to be ordered the same order than
		 * the correspondoig tab titles.
		 *
		 * @see {@link #HEADER_ITEMS}
		 */
		private Fragment[] FRAGMENTS = [
				new SongListFragment(),
				new AlbumListFragment(),
				new PlaylistsFragment()
		]

		/** Constructor. Nothing special */
		public PagerAdaptater(FragmentManager fm){ super(fm) }

		/**
		 * Returns the {@link Fragment} for the queried tab
		 * @param arg0 Index of the queried {@link Fragment}
		 * @return The {@link Fragment} corresponding to the index in the {@link #FRAGMENTS} array
		 */
		@Override public Fragment getItem(int arg0){ return FRAGMENTS[arg0] }

		/**
		 * Get the number of pages
		 * @return number of pages
		 */
		@Override public int getCount(){ return HEADER_ITEMS.length }

		/**
		 * Returns the title for the queried tab
		 * @param position Index of the queried title
		 * @return The title corresponding to the index in the {@link #HEADER_ITEMS} array
		 */
		@Override CharSequence getPageTitle(int position){ return getText(HEADER_ITEMS[position]) }
	}
}
