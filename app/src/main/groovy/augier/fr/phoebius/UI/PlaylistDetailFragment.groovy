package augier.fr.phoebius.UI


import android.app.Activity
import android.util.Log
import augier.fr.phoebius.PlaylistDetailActivity
import augier.fr.phoebius.model.Playlist
import augier.fr.phoebius.model.Song
import groovy.transform.CompileStatic

@CompileStatic
public class PlaylistDetailFragment extends SongListFragment
{
    Playlist songs

    @Override
    void onAttach(Activity activity)
    {
        super.onAttach(activity)
        songs = (activity as PlaylistDetailActivity).songs
    }

    @Override
    protected Playlist getSongs(){ return songs }

    @Override
    void onItemClick(int position)
    {
        Log.e(this.class.toString(), "Playing ${position}th song of the playlist")
    }
}
