package augier.fr.phoebius.UI

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import augier.fr.phoebius.core.MusicService


	public class PageFragment extends FragmentPagerAdapter {

    static final int NUM_ITEMS = 2
	private MusicService musicService

    public PageFragment(FragmentManager fm , MusicService ms) {
        super(fm);
	    musicService = ms
    }

    @Override
    public Fragment getItem(int arg0) {
        // TODO Auto-generated method stub
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

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return NUM_ITEMS;
    }
}
