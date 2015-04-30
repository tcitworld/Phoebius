package augier.fr.phoebius.UI


import android.support.v4.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.TextView
import augier.fr.phoebius.R
import augier.fr.phoebius.core.MusicService
import augier.fr.phoebius.utils.Song
import augier.fr.phoebius.utils.SongList
import com.arasthel.swissknife.SwissKnife
import com.arasthel.swissknife.annotations.InjectView
import com.arasthel.swissknife.annotations.OnItemClick

public class SongListFragment extends Fragment
{
	@InjectView private ListView songView
	private MusicService musicService

	public SongListFragment(MusicService musicService){ this.musicService = musicService }

	@Override
	View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.fragment_song_list, container, false);
		SwissKnife.inject(this, view)
		SongAdapter songAdapter = new SongAdapter()
		songView.setAdapter(songAdapter)

		return view
	}

	private static ArrayList<Song> getSongs(){ return SongList.instance.currSongList }

	class SongAdapter extends AbstractAdaptater
	{
		@Override public int getCount(){ return songs.size() }
		@Override public Object getItem(int arg0){ return null; }
		@Override public long getItemId(int arg0){ return 0; }

		public View getView(int position, View convertView, ViewGroup parent)
		{
			songLay = inflate(activity, R.layout.song_item, parent)

			Song currSong = songs[position]

			getView(R.id.songTitle, TextView.class).setText(currSong.title)
			getView(R.id.songArtist,TextView.class).setText(currSong.artist)

			songLay.setTag(position)
			return songLay
		}
	}

	@OnItemClick(R.id.songView)
	public void onItemClick(int position)
	{
		Song song = songs[position]
		musicService.play(song)
	}
}