package augier.fr.phoebius.UI


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import augier.fr.phoebius.R
import augier.fr.phoebius.utils.Refresher
import augier.fr.phoebius.utils.Song
import augier.fr.phoebius.utils.SongList
import com.arasthel.swissknife.SwissKnife
import com.arasthel.swissknife.annotations.InjectView
import groovy.transform.CompileStatic

@CompileStatic
public class PlayingFragment extends Fragment
{
	@InjectView private TextView playbarMinSongTitle
	@InjectView private TextView playbarMinArtistName
	@InjectView private SquareImageView mainPlayingCoverView
	@InjectView private SquareImageView playbarMinCover
	private Refresher refresher = new Refresher(this.&onRefresh)

	@Override
	View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.fragment_main_playing, container, false)
		SwissKnife.inject(this, view)
		refresher.start()

		return view
	}

	public Song getCurrentSong(){ return SongList.INSTANCE.currentSong }

	private void onRefresh()
	{
		playbarMinArtistName.setText(currentSong.artist)
		playbarMinSongTitle.setText(currentSong.title)
		mainPlayingCoverView.setImageBitmap(currentSong.cover)
		playbarMinCover.setImageBitmap(currentSong.cover)
	}
}
