package augier.fr.phoebius.UI


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import augier.fr.phoebius.PhoebiusApplication
import augier.fr.phoebius.R
import augier.fr.phoebius.model.Song
import augier.fr.phoebius.utils.SquareImageView
import com.arasthel.swissknife.SwissKnife
import com.arasthel.swissknife.annotations.InjectView
import com.squareup.otto.Subscribe

public class PlayingFragment extends Fragment
{
    @InjectView private TextView playbarMinSongTitle
    @InjectView private TextView playbarMinArtistName
    @InjectView private SquareImageView mainPlayingCoverView
    @InjectView private SquareImageView playbarMinCover

    @Override
    View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_main_playing, container, false)
        SwissKnife.inject(this, view)
        PhoebiusApplication.bus.register(this)
        getSong(Song.defaultSong)

        return view
    }

    @Subscribe
    public void getSong(Song currentSong)
    {
        playbarMinArtistName.text = currentSong.artist
        playbarMinSongTitle.text = currentSong.title
        mainPlayingCoverView.imageBitmap = currentSong.cover
        playbarMinCover.imageBitmap = currentSong.cover
    }
}
