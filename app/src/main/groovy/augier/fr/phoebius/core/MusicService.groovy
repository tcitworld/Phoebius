package augier.fr.phoebius.core

import android.app.Service
import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.MediaPlayer.OnPreparedListener
import android.media.MediaPlayer.OnErrorListener
import android.media.MediaPlayer.OnCompletionListener
import android.net.Uri
import android.os.Binder
import android.os.IBinder
import android.os.PowerManager
import android.util.Log
import augier.fr.phoebius.utils.Song
import augier.fr.phoebius.utils.SongList

public class MusicService extends Service implements 
		OnPreparedListener, OnErrorListener,
		OnCompletionListener
{
	private MediaPlayer mediaPlayer
	private SongList songList
	private final IBinder musicBinder = new MusicBinder()

	@Override
	void onCreate()
	{
		super.onCreate()
		songList = new SongList(contentResolver)
		mediaPlayer = new MediaPlayer()
		mediaPlayerInit()

		Log.e("onCreate", this.class.toString())
	}

	//region Player logic
	public void play(Uri songUri)
	{
		mediaPlayer.reset()
		try{ mediaPlayer.setDataSource(applicationContext, songUri) }
		catch(Exception e){ Log.e("MUSIC SERVICE", "Error setting data source", e) /* TODO: Handle fucking exception */ }
		mediaPlayer.prepareAsync()
		Log.d("PLAYING", "${currentSong}")
	}

	public void stop(){ mediaPlayer.stop() }
	public void pause(){ mediaPlayer.pause() }
	public void seek(int position){ mediaPlayer.seekTo(position) }
	public void start(){ mediaPlayer.start() }
	public void playPrevious(){ songList.moveToNextSong(this.&play, this.&stop) }
	public void playNext(){ songList.moveToPreviousSong(this.&play, this.&stop) }
	//endregion


	//region Overrided methods
	@Override
	IBinder onBind(Intent intent){ return musicBinder }

	@Override
	public boolean onUnbind(Intent intent){
		mediaPlayer.stop()
		mediaPlayer.release()
		return false
	}

	@Override
	void onCompletion(MediaPlayer mediaPlayer){ playNext() }

	@Override
	boolean onError(MediaPlayer mediaPlayer, int i, int i2){ return false }

	@Override
	void onPrepared(MediaPlayer mediaPlayer){ mediaPlayer.start() }
	//endregion

	private mediaPlayerInit()
	{
		mediaPlayer.audioStreamType = AudioManager.STREAM_MUSIC
		mediaPlayer.setWakeMode(applicationContext, PowerManager.PARTIAL_WAKE_LOCK)
		mediaPlayer.onPreparedListener = this
		mediaPlayer.onCompletionListener = this
		mediaPlayer.onErrorListener = this
	}

	//region GET/SET
	ArrayList<Song> getSongList(){ return songList.currSongList }
	int getDuration(){ return mediaPlayer.duration }
	boolean isPlaying(){ return mediaPlayer.playing }
	Song getCurrentSong(){ return songList.getCurrentSong() }
	//endregion


	public class MusicBinder extends Binder
	{
		MusicService getService(){ return MusicService.this }
	}
}
