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
import augier.fr.phoebius.utils.SongList
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
		View view = inflater.inflate(R.layout.fragment_song_list, container, false)
		SwissKnife.inject(this, view)
		SongAdapter songAdapter = new SongAdapter()
		songView.setAdapter(songAdapter)

		return view
	}

	private static ArrayList<Album> getAlbums(){ return SongList.instance.albumList }

	class SongAdapter extends AbstractAdaptater
	{
		@Override public int getCount(){ return albums.size() }
		@Override public Object getItem(int arg0){ return null; }
		@Override public long getItemId(int arg0){ return 0; }

		public View getView(int position, View convertView, ViewGroup parent)
		{
			songLay = inflate(activity, R.layout.album_item, parent)

			Album currAlbum = albums[position]
			getView(R.id.albumTitle, TextView.class).text = currAlbum.albumTitle
			getView(R.id.albumArtist, TextView.class).text = currAlbum.albumArtist
			getView(R.id.albumDate, TextView.class).text = currAlbum.date
			getView(R.id.albumNbSongs, TextView.class).text = currAlbum.nbSongs
			getView(R.id.albumCover, ImageView.class).imageBitmap = currAlbum.cover

			return songLay
		}
	}
}
