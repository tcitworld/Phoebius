package augier.fr.phoebius


import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.widget.RelativeLayout
import android.widget.TextView
import augier.fr.phoebius.model.Song
import augier.fr.phoebius.utils.SquareImageView
import com.arasthel.swissknife.SwissKnife
import com.arasthel.swissknife.annotations.InjectView
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import com.squareup.otto.Subscribe
import groovy.transform.CompileStatic

@CompileStatic
public class MainActivity extends AppCompatActivity
{
    @InjectView private TextView playbarMinSongTitle
    @InjectView private TextView playbarMinArtistName
    @InjectView private SquareImageView mainPlayingCoverView
    @InjectView private SquareImageView playbarMinCover
    @InjectView private RelativeLayout playbar
    @InjectView private SlidingUpPanelLayout slidingLayout

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState)
        contentView = R.layout.activity_main
        SwissKnife.inject(this)
        slidingLayout.dragView = playbar
        PhoebiusApplication.bus.register(this)
        getSong(Song.defaultSong)
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    @Override protected void onResume(){ super.onResume() }

    @Override protected void onPause()
    {
        PhoebiusApplication.save()
        super.onPause()
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
