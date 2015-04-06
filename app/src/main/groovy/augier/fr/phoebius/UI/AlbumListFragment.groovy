package augier.fr.phoebius.UI


import android.app.Fragment
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
import augier.fr.phoebius.utils.Album
import augier.fr.phoebius.utils.Song
import com.arasthel.swissknife.SwissKnife
import com.arasthel.swissknife.annotations.InjectView
import com.arasthel.swissknife.annotations.OnItemClick;


public class AlbumListFragment extends Fragment
{
	@InjectView private ListView songView
	private MusicService musicService

	public AlbumListFragment(MusicService musicService){ this.musicService = musicService }

	@Override
	View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.fragment_song_list, container, false);
		SwissKnife.inject(this, view)
		SongAdapter songAdapter = new SongAdapter()
		songView.setAdapter(songAdapter)

		return view
	}

	class SongAdapter extends BaseAdapter
	{
		private LayoutInflater songInf = LayoutInflater.from(activity)
		@Override public int getCount(){ return albums.size() }
		@Override public Object getItem(int arg0){ return null; }
		@Override public long getItemId(int arg0){ return 0; }

		public View getView(int position, View convertView, ViewGroup parent)
		{
			LinearLayout songLay = songInf.
					inflate(R.layout.album_item, parent, false) as LinearLayout
			Album currAlbum = albums.get(position)
			TextView albumTitle = songLay.findViewById(R.id.albumTitle) as TextView
			TextView albumArtist = songLay.findViewById(R.id.albumArtist) as TextView
			TextView albumDate = songLay.findViewById(R.id.albumDate) as TextView
			TextView albumNbSongs = songLay.findViewById(R.id.albumNbSongs) as TextView
			albumTitle.setText(currAlbum.albumTitle)
			albumArtist.setText(currAlbum.albumArtist)
			albumDate.setText(currAlbum.date)
			albumNbSongs.setText(currAlbum.nbSongs)
			songLay.setTag(position)
			return songLay
		}
		private ArrayList<Album> getAlbums(){ return musicService.albumList }
	}
}
