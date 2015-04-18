package augier.fr.phoebius.UI


import android.support.v4.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import augier.fr.phoebius.R
import augier.fr.phoebius.core.MusicService
import augier.fr.phoebius.utils.Album
import com.arasthel.swissknife.SwissKnife
import com.arasthel.swissknife.annotations.InjectView

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
			ImageView albumCover = songLay.findViewById(R.id.albumCover) as ImageView

			albumTitle.text = currAlbum.albumTitle
			albumArtist.text = currAlbum.albumArtist
			albumDate.text = currAlbum.date
			albumNbSongs.text = currAlbum.nbSongs
			albumCover.imageBitmap = currAlbum.cover
			songLay.tag = position
			return songLay
		}
		private ArrayList<Album> getAlbums(){ return musicService.albumList }
	}
}
