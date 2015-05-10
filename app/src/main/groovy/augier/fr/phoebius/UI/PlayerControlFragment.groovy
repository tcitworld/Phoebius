package augier.fr.phoebius.UI


import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
import augier.fr.phoebius.MainActivity
import augier.fr.phoebius.PhoebiusApplication
import augier.fr.phoebius.R
import augier.fr.phoebius.core.MusicService
import augier.fr.phoebius.core.MusicServiceConnection
import com.arasthel.swissknife.SwissKnife
import com.arasthel.swissknife.annotations.InjectView
import com.arasthel.swissknife.annotations.OnBackground
import com.arasthel.swissknife.annotations.OnClick
import groovy.transform.CompileStatic


/**
 * {@link Fragment} to display the customized player controller
 *
 * This class uses <a href="https://github.com/Arasthel/SwissKnife">SwissKnife</a>.
 * The views are injected in the {@link MainPageFragment#onCreateView onCreateView} method
 */
@CompileStatic
public class PlayerControlFragment extends Fragment
{
	/** Frequency of refresh, in milliseconds */
	private static final int REFRESH_TIME = 500
	@InjectView private TextView currentDurationLabel
	@InjectView private TextView totalDurationLabel
	@InjectView private SeekBar songProgressBar
	@InjectView private ImageButton btnPlayPause
	private SongBarListener songBarListener = new SongBarListener()
	private Handler handler = new Handler()
	private Runnable refresh = new Refresher()

	@Override
	View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.fragment_player_control, container, false)
		SwissKnife.inject(this, view)
		SwissKnife.runOnBackground(this, "doOnBackground")
		songProgressBar.setOnSeekBarChangeListener(songBarListener)
		return view
	}

	/**
	 * Launch the recurrent refresh of the controller the first time on a separate thread
	 *
	 * The refreshing will then go on by calling itself recursively.
	 * This method uses <a href="https://github.com/Arasthel/SwissKnife/wiki/@OnBackground">SwissKnife's @OnBackground annotation </a>
	 *
	 * @see {@link Refresher}
	 */
	@OnBackground public void doOnBackground(){ handler.postDelayed(refresh, REFRESH_TIME) }

	/**
	 * Callback for play button
	 *
	 * This method uses <a href="https://github.com/Arasthel/SwissKnife/wiki/@OnClick">SwissKnife's @OnClick annotation </a>
	 */
	@OnClick(R.id.btnPlayPause)
	public void onBtnPlayClick()
	{
		if(playing)
		{
			pause()
			btnPlayPause.imageResource = R.drawable.btn_play
		}
		else
		{
			start()
			btnPlayPause.imageResource = R.drawable.btn_pause
		}
	}

	/**
	 * Callback for backward button
	 *
	 * This method uses <a href="https://github.com/Arasthel/SwissKnife/wiki/@OnClick">SwissKnife's @OnClick annotation </a>
	 */
	@OnClick(R.id.btnBackward)
	public void onBtnBackwardClick(){ if(playing) seekTo(Math.max(currentPosition - 10000, 0)) }

	/**
	 * Callback for forward button
	 *
	 * This method uses <a href="https://github.com/Arasthel/SwissKnife/wiki/@OnClick">SwissKnife's @OnClick annotation </a>
	 */
	@OnClick(R.id.btnForward)
	public void onBtnForwardClick()
		{ if(playing) seekTo(Math.min(currentPosition + 10000, duration)) }

	/**
	 * Callback for previous button
	 *
	 * This method uses <a href="https://github.com/Arasthel/SwissKnife/wiki/@OnClick">SwissKnife's @OnClick annotation </a>
	 */
	@OnClick(R.id.btnPrevious)
	public void onBtnPreviousClick()
	{
		if(playing)
		{
			if(currentPosition < 2000) playPrev()
			else seekTo(0)
		}
	}

	/**
	 * Callback for next button
	 *
	 * This method uses <a href="https://github.com/Arasthel/SwissKnife/wiki/@OnClick">SwissKnife's @OnClick annotation </a>
	 */
	@OnClick(R.id.btnNext)
	public void onBtnNextClick(){ if(isPlaying()) playNext() }

	/**
	 * Convert a given time in milliseconds to a human readable format
	 *
	 * Format is : "mm:ss" and "hh:mm:ss" if that time is more than an hour
	 * @param ms Time to convert in seconds
	 * @return String representing the time in a human readable format
	 */
	private static String fromMilliSeconds(int ms)
	{
		ms /= 1000
		int hours = (int)(ms / 3600)
		int remainder = ms % 3600
		int _mins = (int)(remainder / 60)
		remainder = remainder % 60
		int _secs = remainder

		String mins = _mins < 10 ? "0${_mins}" : "${_mins}"
		String secs = _secs < 10 ? "0${_secs}" : "${_secs}"

		return hours > 0 ? "${hours}:${mins}:${secs}" : "${mins}:${secs}"
	}

	//region MediaController
	/** Shorthand to {@link MusicService#playNext()} */
	private void playNext(){ musicService.playNext() }

	/** Shorthand to {@link MusicService#playPrevious()} */
	private void playPrev(){ musicService.playPrevious() }

	/**
	 * Shorthand to {@link MusicService#getDuration()}
	 * @return Total duration of the song, 0 if
	 * {@link #getMusicService()} returns null or {@link #isPlaying()} is false
	 */
	int getDuration()
	{
		if(musicService == null) return 0
		return musicService.duration
	}

	/**
	 * Returns the time elapsed during the playing of the song
	 *
	 * If the user is controlling the seekbar, this will return
	 * the time the seekbar is being seeked to.
	 * @return Rime elapsed during the playing, 0 if
	 * {@link #getMusicService()} returns null
	 */
	int getCurrentPosition()
	{
		if(musicService == null) return 0
		if(!songBarListener.userTrackingSongBar) musicService.position
		else return songBarListener.songProgression
	}

	/** Shorthand for {@link MusicService#isPlaying}, false is {@link #getMusicService()} is null */
	boolean isPlaying(){ return musicService != null ? musicService.playing : false }
	/** Shorthand for {@link MusicService#seek(int)} */
	void seekTo(int i){ musicService.seek(i) }
	/** Shorthand for {@link MusicService#start()} */
	void start(){ musicService.start() }
	/** Shorthand for {@link MusicService#start()} */
	void pause(){ musicService.pause() }
	//endregion

	private MusicService getMusicService(){ return PhoebiusApplication.musicService }

	/**
	 * Listener for the controller's seekbar
	 *
	 * This class is just an implementation of {@link OnSeekBarChangeListener}.
	 * It takes control of the {@link #getCurrentPosition()} when user starts
	 * to seek (see {@link OnSeekBarChangeListener#onStartTrackingTouch(android.widget.SeekBar)})
	 * to display the time to which the player is currently seeking.
	 */
	private class SongBarListener implements OnSeekBarChangeListener
	{
		private boolean userTrackingSongBar = false
		private int songProgression = 0

		@Override void onProgressChanged(SeekBar seekBar, int i, boolean b)
		{
			if(b) songProgression = i
			currentDurationLabel.text = fromMilliSeconds(i)
		}

		@Override void onStartTrackingTouch(SeekBar seekBar){ userTrackingSongBar = true }

		@Override void onStopTrackingTouch(SeekBar seekBar)
		{
			if(getMusicService().ready)
			{
				seekTo(songProgression)
				userTrackingSongBar = false
			}
		}

		/** @return Whether the user is currently seeking */
		public boolean getUserTrackingSongBar(){ return userTrackingSongBar }
		/** @return The time the user is seeking */
		public int getSongProgression(){ return songProgression }
	}

	/**
	 * This {@link Runnable} is responsible for regularly refresh the diaplsy
	 *
	 * Here is what it does :
	 *
	 *  <ol>
	 *      <li> setting the text for the elapsed time (text label right to the seekbar) </li>
	 *      <li> setting the the text for  total suration of the song </li>
	 *      <li> setting the max of the seekbar to the duration of the song </li>
	 *      <li> setting the correct {@link android.graphics.drawable.Drawable} for the play/pause button </li>
	 *      <li> plan to recall itself when the refresh time has elapsed (see {@link #REFRESH_TIME}) </li>
	 *  </ol>
	 */
	private class Refresher implements Runnable
	{
		@Override void run()
		{
			currentDurationLabel.text = fromMilliSeconds(currentPosition as int)
			totalDurationLabel.text = fromMilliSeconds(duration as int)
			songProgressBar.max = (int)duration
			songProgressBar.progress = (int)currentPosition
			if(isPlaying()) btnPlayPause.imageResource = (int)R.drawable.btn_pause
			else btnPlayPause.imageResource = (int)R.drawable.btn_play
			handler.postDelayed(this, REFRESH_TIME)
		}
	}
}
