package augier.fr.phoebius.UI


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import augier.fr.phoebius.R
import augier.fr.phoebius.model.Album
import augier.fr.phoebius.utils.AbstractAdaptater
import com.arasthel.swissknife.SwissKnife
import com.arasthel.swissknife.annotations.InjectView


/**
 * Fragment to display the albumName list
 *
 * This class uses <a href="https://github.com/Arasthel/SwissKnife">SwissKnife</a>.
 * The views are injected in the {@link AlbumListFragment#onCreateView onCreateView} method
 */
public class AlbumListFragment extends Fragment
{
    /**
     * Album view (RLY!?)
     * The view s automatically injected by SwissKnife on start.
     *
     * See https://github.com/Arasthel/SwissKnife/wiki#how-to-use-the-annotations
     */
    @InjectView private GridView albumView

    @Override
    View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_album_list, container, false)
        SwissKnife.inject(this, view)
        SongAdapter songAdapter = new SongAdapter()
        albumView.adapter = songAdapter

        return view
    }
    /**
     * Adaptater to createOne a grid of albums
     */
    class SongAdapter extends AbstractAdaptater<LinearLayout>
    {
        @Override public int getCount(){ return Album.allAlbums.size() }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            layout = inflate(activity, R.layout.album_item, parent)

            Album currAlbum = Album.allAlbums[position]
            this.<TextView>getView(R.id.albumTitle).text = currAlbum.albumTitle
            this.<TextView>getView(R.id.albumArtist).text = currAlbum.albumArtist
            this.<TextView>getView(R.id.albumNbSongs).text = "${currAlbum.nbSongs} pistes"
            this.<ImageView>getView(R.id.albumCover).imageBitmap = currAlbum.cover

            return layout
        }
    }
}
