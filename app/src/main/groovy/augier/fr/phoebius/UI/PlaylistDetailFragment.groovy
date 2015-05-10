package augier.fr.phoebius.UI


import android.util.Log
import augier.fr.phoebius.utils.Song

public class PlaylistDetailFragment extends SongListFragment
{
	ArrayList<Song> songs

	PlaylistDetailFragment(ArrayList<Song> songs)
	{
		super()
		this.songs = songs
	}

	@Override
	protected ArrayList<Song> getSongs(){ return songs}

	@Override
	void onItemClick(int position){
		Log.e(this.class.toString(), "lol")
	}
}
