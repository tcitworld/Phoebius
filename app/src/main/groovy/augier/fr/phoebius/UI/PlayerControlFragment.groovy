package augier.fr.phoebius.UI


import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.MediaController.MediaPlayerControl
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
import augier.fr.phoebius.R
import augier.fr.phoebius.core.MusicService
import augier.fr.phoebius.core.MusicServiceConnection
import com.arasthel.swissknife.SwissKnife
import com.arasthel.swissknife.annotations.InjectView
import com.arasthel.swissknife.annotations.OnBackground
import com.arasthel.swissknife.annotations.OnClick

public class PlayerControlFragment extends Fragment implements MediaPlayerControl
{
	@InjectView private TextView currentDurationLabel
	@InjectView private TextView totalDurationLabel
	@InjectView private SeekBar songProgressBar
	@InjectView private ImageButton btnPlayPause
	private boolean userTrackingSongBar = false
	private int songProgression = 0
	private MusicServiceConnection musicConnection
	private Handler handler = new Handler()
	private Runnable refresh = new Runnable(){
		@Override void run()
		{
			currentDurationLabel.text = fromMilliSeconds(currentPosition)
			totalDurationLabel.text = fromMilliSeconds(duration)
			songProgressBar.max = duration
			songProgressBar.progress = playing ? currentPosition : songProgression
			btnPlayPause.imageResource = playing ? R.drawable.btn_pause : R.drawable.btn_play
			handler.postDelayed(this, 500)
		}
	}

	PlayerControlFragment(MusicServiceConnection musicConnection)
	{ this.musicConnection = musicConnection }

	@Override
	View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.fragment_player_control, container, false)
		SwissKnife.inject(this, view)
		SwissKnife.runOnBackground(this, "doOnBackground")
		songProgressBar.setOnSeekBarChangeListener(new SongBarListener())
		return view
	}

	@OnBackground public void doOnBackground(){ handler.postDelayed(refresh, 500) }

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

	@OnClick(R.id.btnBackward)
	public void onBtnBackwardClick(){ if(playing) seekTo(currentPosition - 10000) }

	@OnClick(R.id.btnForward)
	public void onBtnForwardClick(){ if(playing) seekTo(currentPosition + 10000) }

	@OnClick(R.id.btnPrevious)
	public void onBtnPreviousClick()
	{
		if(playing)
		{
			if(currentPosition < 2000) playPrev()
			else seekTo(0)
		}
	}

	@OnClick(R.id.btnNext)
	public void onBtnNextClick()
	{ if(playing) playNext() }

	private static String fromMilliSeconds(int ms)
	{
		ms /= 1000
		int hours = ms / 3600
		int remainder = ms % 3600
		int _mins = remainder / 60
		remainder = remainder % 60
		int _secs = remainder

		String mins = _mins < 10 ? "0${_mins}" : "${_mins}"
		String secs = _secs < 10 ? "0${_secs}" : "${_secs}"

		return hours > 0 ? "${hours}:${mins}:${secs}" : "${mins}:${secs}"
	}

	//region MediaController
	private void playNext(){ musicService.playNext() }
	private void playPrev(){ musicService.playPrevious() }

	@Override int getDuration()
	{
		if(musicService == null) return 0
		return playing ? musicService.duration : 0
	}

	@Override int getBufferPercentage()
	{
		if(!playing || currentPosition == 0) return 0
		int percent = (currentPosition / duration) * 100
		return percent
	}

	@Override int getCurrentPosition()
	{
		if(musicService == null) return 0
		if(!userTrackingSongBar)
		{
			songProgression = musicService.position
			return songProgression
		}
		else return songProgression
	}
	@Override boolean isPlaying(){ return musicService != null ? musicService.playing : false }
	@Override void seekTo(int i){ musicService.seek(i) }
	@Override boolean canSeekBackward(){ return true }
	@Override boolean canSeekForward(){ return true }
	@Override boolean canPause(){ return true }
	@Override void start(){ musicService.start() }
	@Override void pause(){ musicService.pause() }
	@Override int getAudioSessionId(){ return 0 }
	//endregion
	private MusicService getMusicService(){ return musicConnection.musicService }

	private class SongBarListener implements OnSeekBarChangeListener
	{
		@Override void onProgressChanged(SeekBar seekBar, int i, boolean b)
		{
			if(b) songProgression = i
			currentDurationLabel.text = fromMilliSeconds(i)
		}

		@Override void onStartTrackingTouch(SeekBar seekBar){ userTrackingSongBar = true }

		@Override void onStopTrackingTouch(SeekBar seekBar)
		{
			seekTo(songProgression)
			userTrackingSongBar = false
		}
	}
}
