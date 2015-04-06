package augier.fr.phoebius.UI


import android.app.Fragment
import android.content.Context
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

	@OnItemClick(R.id.songView)
	public void onItemClick(int position)
	{
		Song song = musicService.songList[position]
		musicService.play(song)
	}

	class SongAdapter extends BaseAdapter
	{
		private LayoutInflater songInf = LayoutInflater.from(activity)

		@Override
		public int getCount()
		{ return songs.size() }

		@Override
		public Object getItem(int arg0)
		{
			return null;
		}

		@Override
		public long getItemId(int arg0)
		{
			return 0;
		}

		public View getView(int position, View convertView, ViewGroup parent)
		{
			LinearLayout songLay = songInf.
					inflate(R.layout.song_item, parent, false) as LinearLayout
			Song currSong = songs.get(position)
			TextView songTitle = songLay.findViewById(R.id.songTitle) as TextView
			TextView songArtist = songLay.findViewById(R.id.songArtist) as TextView
			songTitle.setText(currSong.getTitle())
			songArtist.setText(currSong.getArtist())
			songLay.setTag(position)
			return songLay
		}
		private ArrayList<Song> getSongs(){ return musicService.songList }
	}
}