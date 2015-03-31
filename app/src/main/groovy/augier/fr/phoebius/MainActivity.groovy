package augier.fr.phoebius


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ListView
import android.widget.MediaController
import android.widget.MediaController.MediaPlayerControl
import augier.fr.phoebius.core.MusicService
import augier.fr.phoebius.core.MusicServiceConnection
import com.arasthel.swissknife.SwissKnife
import com.arasthel.swissknife.annotations.InjectView
import com.arasthel.swissknife.annotations.OnItemClick

public class MainActivity extends Activity implements MediaPlayerControl
{
	public static final String APP_NAME = "Phoebius"
	@InjectView ListView songView
	private MusicServiceConnection musicConnection
	private Intent playIntent
	private MusicController musicController
	private MediaPlayerControl mediaPlayerControl = this

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// Class init
		super.onCreate(savedInstanceState)
		contentView = R.layout.activity_main
		SwissKnife.inject(this)

		// Variables init
		/*
		 * TODO : Intencier dans un thread à part
		 */
		musicConnection = new MusicServiceConnection(this, songView)

		// UI init
		musicController = new MusicController(this)
		musicController.setPrevNextListeners(
			new View.OnClickListener() { @Override public void onClick(View v) { playNext() }},
			new View.OnClickListener() { @Override public void onClick(View v) { playPrev() }})
	}

	@Override
	protected void onStart()
	{
		super.onStart()

		if(playIntent == null)
		{
			playIntent = new Intent(this, MusicService.class)
			bindService(playIntent, musicConnection, BIND_AUTO_CREATE)
			startService(playIntent)
		}
	}

	@Override
	protected void onDestroy()
	{
		stopService(playIntent)
		musicConnection.destroy()
		super.onDestroy()
	}

	private void playNext(){
		musicService.playNext()
		musicController.show()
	}

	private void playPrev(){
		musicService.playPrevious()
		musicController.show()
	}

	// region User events callbacks
	@OnItemClick(R.id.songView)
	public void onItemClick(int position)
	{
		Uri songUri = musicService.songList[position].URI
		musicService.play(songUri)
		musicController.show()
	}

	@Override
	void start(){ musicService.start() }

	@Override
	void pause(){ musicService.pause() }

	@Override
	int getDuration()
	{
		if(musicService == null){ return 0 }
		return musicService.playing ? musicService.duration : 0
	}

	@Override
	int getCurrentPosition(){ return getBufferPercentage() }

	@Override
	void seekTo(int i){ musicService.seek(i) }

	@Override
	boolean isPlaying(){ return musicService != null ? musicService.playing : false }

	@Override
	int getBufferPercentage(){ return 50 }

	@Override
	boolean canPause(){ return true }

	@Override
	boolean canSeekBackward(){ return true }

	@Override
	boolean canSeekForward(){ return true }

	@Override
	int getAudioSessionId(){ return 0 }
	//endregion

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		menuInflater.inflate(R.menu.main, menu)
		return true
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch(item.itemId)
		{
			case R.id.action_shuffle:
				break
			case R.id.action_end:
				stopService(playIntent)
				musicConnection.destroy()
				System.exit(0)
				break
		}
		return super.onOptionsItemSelected(item)
	}

	private class MusicController extends MediaController
	{
		MusicController(Context context)
		{
			super(context)

			mediaPlayer = mediaPlayerControl
			anchorView = songView
			enabled = true
		}

		@Override
		public void hide(){}
	}

	private MusicService getMusicService(){ return musicConnection.musicService }
}