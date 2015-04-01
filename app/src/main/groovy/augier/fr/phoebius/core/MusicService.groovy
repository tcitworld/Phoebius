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
		songList.stopCallback = this.&stop
		songList.playCallback = this.&play
		mediaPlayer = new MediaPlayer()
		mediaPlayerInit()
	}

	//region Player logic
	public void play(Song song)
	{
		mediaPlayer.reset()
		try{ mediaPlayer.setDataSource(applicationContext, song.URI) }
		catch(Exception e){ Log.e("MUSIC SERVICE", "Error setting data source", e) /* TODO: Handle fucking exception */ }
		mediaPlayer.prepareAsync()
		songList.currentSong = song
		Log.d("PLAYING", "${currentSong}")
	}

	public void stop(){ mediaPlayer.stop() }
	public void pause(){ mediaPlayer.pause() }
	public void seek(int position){ mediaPlayer.seekTo(position) }
	public void start(){ mediaPlayer.start() }
	public void playPrevious(){ songList.moveToPreviousSong() }
	public void playNext(){ songList.moveToNextSong() }
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
	int getPosition(){ return mediaPlayer.currentPosition }
	//endregion


	public class MusicBinder extends Binder
	{
		MusicService getService(){ return MusicService.this }
	}
}
