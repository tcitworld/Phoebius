package augier.fr.phoebius.UI


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.TextView
import augier.fr.phoebius.PlaylistDetailActivity
import augier.fr.phoebius.R
import augier.fr.phoebius.model.SongManager
import augier.fr.phoebius.utils.AbstractAdaptater
import com.arasthel.swissknife.SwissKnife
import com.arasthel.swissknife.annotations.InjectView
import com.arasthel.swissknife.annotations.OnItemClick

public class PlaylistsFragment extends Fragment
{
    @InjectView private ListView songView

    @Override
    View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_song_list, container, false)
        SwissKnife.inject(this, view)
        songView.adapter = new PlaylistAdaptater()
        return view
    }

    @OnItemClick(R.id.songView)
    public void onItemClick(int position)
    {
        String playlistName = allPlaylists[position]
        Intent intent = new Intent(activity, PlaylistDetailActivity.class)
        intent.putExtra("songs", SongManager.INSTANCE.getPlaylist(playlistName))
        startActivity(intent)
    }

    private ArrayList<String> getAllPlaylists(){ return SongManager.INSTANCE.allPlaylists }

    class PlaylistAdaptater extends AbstractAdaptater<LinearLayout>
    {
        @Override int getCount(){ return allPlaylists.size() }

        public View getView(int position, View convertView, ViewGroup parent)
        {
            layout = inflate(activity, R.layout.playlist_item, parent)
            this.<TextView> getView(R.id.playlistName).text = allPlaylists[position]
            return layout
        }
    }
}
