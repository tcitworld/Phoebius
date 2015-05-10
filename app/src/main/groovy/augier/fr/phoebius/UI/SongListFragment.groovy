package augier.fr.phoebius.UI


import android.support.v4.app.Fragment
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.TextView
import augier.fr.phoebius.PhoebiusApplication
import augier.fr.phoebius.R
import augier.fr.phoebius.core.MusicService
import augier.fr.phoebius.utils.Song
import augier.fr.phoebius.utils.SongList
import com.arasthel.swissknife.SwissKnife
import com.arasthel.swissknife.annotations.InjectView
import com.arasthel.swissknife.annotations.OnItemClick


/**
 * Fragment to display the song list
 *
 * This class uses <a href="https://github.com/Arasthel/SwissKnife">SwissKnife</a>.
 * The views are injected in the {@link SongListFragment#onCreateView onCreateView} method
 */
public class SongListFragment extends Fragment
{
	@InjectView private ListView songView

	@Override
	View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.fragment_song_list, container, false)
		SwissKnife.inject(this, view)
		SongAdapter songAdapter = new SongAdapter()
		songView.setAdapter(songAdapter)

		return view
	}

	/**
	 * Callback when user clicks on a song
	 *
	 * This method uses <a href="https://github.com/Arasthel/SwissKnife/wiki/@OnItemClick">SwissJnife's @OnItemClick annotation</a>
	 * @param position
	 */
	@OnItemClick(R.id.songView)
	public void onItemClick(int position)
	{
		Song song = songs[position]
		musicService?.play(song)
	}

	/** @return List of songs @see {@link SongList} */
	protected ArrayList<Song> getSongs(){ return SongList.instance.currSongList }
	protected MusicService getMusicService(){ return PhoebiusApplication.musicService }

	/** Adapter class, nothing special */
	class SongAdapter extends AbstractAdaptater
	{
		@Override public int getCount(){ return songs.size() }

		public View getView(int position, View convertView, ViewGroup parent)
		{
			layout = inflate(activity, R.layout.song_item, parent)

			Song currSong = songs[position]

			getView(R.id.songTitle, TextView.class).setText(currSong.title)
			getView(R.id.songArtist,TextView.class).setText(currSong.artist)

			layout.setTag(position)
			return layout
		}
	}
}