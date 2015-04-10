package augier.fr.phoebius.UI


import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import augier.fr.phoebius.R
import augier.fr.phoebius.utils.Song
import augier.fr.phoebius.utils.SongList
import com.arasthel.swissknife.SwissKnife
import com.arasthel.swissknife.annotations.InjectView
import com.arasthel.swissknife.annotations.OnBackground
import groovy.transform.CompileStatic;

@CompileStatic
public class PlayingFragment extends Fragment
{
	private static final int REFRESH_TIME = 500
	@InjectView private TextView playbarMinSongTitle
	@InjectView private TextView playbarMinArtistName
	@InjectView private SquareImageView mainPlayingCoverView
	@InjectView private SquareImageView playbarMinCover
	private Handler handler = new Handler()
	private Runnable refresh = new Refresher()

	@Override
	View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.fragment_main_playing, container, false)
		SwissKnife.inject(this, view)
		SwissKnife.runOnBackground(this, "doOnBackground")

		return view
	}

	@OnBackground
	private void doOnBackground(){ handler.postDelayed(refresh, REFRESH_TIME) }

	public Song getCurrentSong(){ return SongList.instance.currentSong }

	private class Refresher implements Runnable
	{
		@Override void run()
		{
			playbarMinArtistName.setText(getCurrentSong().getArtist())
			playbarMinSongTitle.setText(getCurrentSong().getTitle())
			mainPlayingCoverView.setImageBitmap(getCurrentSong().getCover())
			playbarMinCover.setImageBitmap(getCurrentSong().getCover())

			handler.postDelayed(this, REFRESH_TIME)
		}
	}
}
