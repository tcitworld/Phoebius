package augier.fr.phoebius.UI


import android.content.Intent
import android.widget.TextView
import augier.fr.phoebius.PlaylistDetailActivity
import augier.fr.phoebius.R
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import augier.fr.phoebius.core.MusicService
import augier.fr.phoebius.utils.SongList
import com.arasthel.swissknife.SwissKnife
import com.arasthel.swissknife.annotations.InjectView
import com.arasthel.swissknife.annotations.OnItemClick

public class PlaylistsFragment extends Fragment
{
	@InjectView private ListView songView
	private MusicService musicService
	private SongList songList = SongList.instance

	public PlaylistsFragment(MusicService musicService){ this.musicService = musicService }

	@Override
	View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.fragment_song_list, container, false)
		SwissKnife.inject(this, view)
		def playlistAdaptater = new PlaylistAdaptater()
		songView.setAdapter(playlistAdaptater)
		return view
	}

	@OnItemClick(R.id.songView)
	public void onItemClick(int position)
	{
		Intent intent = new Intent(activity, PlaylistDetailActivity.class)
		intent.putExtra("songs", songList.songList)
		startActivity(intent)
	}

	private ArrayList<String> getAllPlaylists(){ return songList.allPlaylists }

	class PlaylistAdaptater extends AbstractAdaptater
	{
		@Override int getCount(){ return allPlaylists.size() }

		public View getView(int position, View convertView, ViewGroup parent)
		{
			layout = inflate(activity, R.layout.playlist_item, parent)
			getView(R.id.playlistName, TextView.class).text = allPlaylists[position]
			return layout
		}
	}
}
