package augier.fr.phoebius.core

import android.app.Service
import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.MediaPlayer.OnPreparedListener
import android.media.MediaPlayer.OnErrorListener
import android.media.MediaPlayer.OnCompletionListener
import android.os.Binder
import android.os.IBinder
import android.os.PowerManager
import android.util.Log
import augier.fr.phoebius.utils.Song
import augier.fr.phoebius.utils.SongList
import groovy.transform.CompileStatic


/**
 * This class takes care of playing the music and interacting with the controls
 */
@CompileStatic
class MusicService extends Service implements OnPreparedListener,
		OnErrorListener, OnCompletionListener
{
	/**
	 * Our actual music player that will broadcast sound
	 */
	private MediaPlayer mediaPlayer

	/**
	 * Our manager for the songs
	 */
	private SongList songList

	/**
	 * Our binder (thanks, Captain Obvious !)
	 */
	private final IBinder musicBinder = new MusicBinder()

	/**
	 * Variable to ensure the player is in a validate state
	 */
	private IdleStateHandler idle = new IdleStateHandler()

	private NotificationPlayer notificationPlayer = NotificationPlayer.getInstance()

	@Override
	void onDestroy()
	{
		super.onDestroy()
		notificationPlayer.cancel()
	}

	@Override
	void onCreate()
	{
		super.onCreate()
		songList = SongList.getInstance()
		songList.stopCallback = this.&stop
		songList.playCallback = this.&play
		mediaPlayer = new MediaPlayer()
		mediaPlayerInit()
	}

	//region Player logic
	/**
	 * Plays a song
	 * @param song Song to be played (see {@link Song ])
	 */
	public void play(Song song)
	{
		mediaPlayer.reset()
		try
		{
			mediaPlayer.setDataSource(applicationContext, song.URI)
		}
		catch(Exception e)
		{
			Log.e(this.class.toString(), "Error setting data source: ${e}")
		}
		mediaPlayer.prepare()
		mediaPlayer.start()
		songList.currentSong = song
		notificationPlayer.notify(songList.getCoverFor(song.album), song.title, song.album)
	}

	/**
	 * Stops the player
	 */
	public void stop(){ mediaPlayer.stop() }

	/**
	 * Pauses the player
	 */
	public void pause(){ mediaPlayer.pause() }

	/**
	 * Seeks the song currently playing to a given position
	 *
	 * If nithing is playing, does nothing
	 * @param position position to seek to given in milliseconds
	 */
	public void seek(int position){ mediaPlayer.seekTo(position) }

	/**
	 * Starts playing the music
	 */
	public void start()
	{
		mediaPlayer.start()
	}

	/**
	 * Moves the song playing (or song to be played if the player
	 * is paused) to the previous song. see {@link SongList#moveToPreviousSong()}
	 */
	public void playPrevious(){ songList.moveToPreviousSong() }

	/**
	 * Moves the song playing (or song to be played if the player
	 * is paused) to the next song. see {@link SongList#moveToNextSong()}
	 */
	public void playNext(){ songList.moveToNextSong() }
	//endregion


	//region Overrided methods
	@Override
	IBinder onBind(Intent intent){ return musicBinder }

	@Override
	public boolean onUnbind(Intent intent)
	{
		mediaPlayer.stop()
		mediaPlayer.release()
		return false
	}

	/**
	 * Will play the next song as soon as the current on is finished
	 * @param mediaPlayer
	 */
	@Override void onCompletion(MediaPlayer mediaPlayer){ playNext() }

	/** Does nothing  */
	@Override boolean onError(MediaPlayer mediaPlayer, int i, int i2){ return false }

	/** Does nothing */
	@Override void onPrepared(MediaPlayer mediaPlayer){}
	//endregion

	/**
	 * Initializes the player
	 *
	 * This will set the listeners of the player and confgurate it
	 *
	 * @see {@link MusicService#mediaPlayer}
	 */
	private void mediaPlayerInit()
	{
		mediaPlayer.audioStreamType = AudioManager.STREAM_MUSIC
		mediaPlayer.setWakeMode(applicationContext, PowerManager.PARTIAL_WAKE_LOCK)
		mediaPlayer.onPreparedListener = this
		mediaPlayer.onCompletionListener = this
		mediaPlayer.onErrorListener = this
		def uri = songList.currentSong?.URI
		if(uri != null)
		{
			mediaPlayer.setDataSource(applicationContext, uri)
			mediaPlayer.prepare()
			idle.validate()
		}
	}

	//region GET/SET
	/**
	 * Returns the total duration of the song
	 * @return Total duration in milliseconds
	 */
	int getDuration(){ return ready ? mediaPlayer.duration : 0 }

	/**
	 * Inidicates whether our player is actually playing
	 * @return Playing or not
	 */
	boolean isPlaying(){ return ready ? mediaPlayer.playing : false }

	/**
	 * Indicates if the media player is ready to play
	 * @return Reafy or not
	 */
	boolean isReady(){ return idle.ready }

	/**
	 * Returns the elapsed time the song has been playing
	 * @return Position in the playback in milliseconds
	 */
	int getPosition(){ return ready ? mediaPlayer.currentPosition : 0 }
	//endregion

	/**
	 * Our Binder
	 */
	public class MusicBinder extends Binder{ MusicService getService(){ return MusicService.this } }

	private class IdleStateHandler
	{
		private boolean ready = false
		public boolean isReady(){ return ready }
		public void validate(){ ready = true }
	}
}
