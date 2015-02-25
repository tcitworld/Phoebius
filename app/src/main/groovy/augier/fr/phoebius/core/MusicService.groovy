package augier.fr.phoebius.core

import android.app.Service
import android.content.ContentUris
import android.content.Intent
import android.media.MediaPlayer
import android.media.MediaPlayer.OnPreparedListener
import android.media.MediaPlayer.OnErrorListener
import android.media.MediaPlayer.OnCompletionListener
import android.provider.MediaStore.Audio.Media as AudioMedia
import android.os.Binder
import android.os.IBinder
import android.os.PowerManager
import android.util.Log
import augier.fr.phoebius.utils.SongList

public class MusicService extends Service implements 
		OnPreparedListener, OnErrorListener,
		OnCompletionListener
{
	private MediaPlayer mediaPlayer
	private SongList songList
	private int songPosition
	private final IBinder musicBinder = new MusicBinder()

	@Override
	void onCreate()
	{
		super.onCreate()
		songPosition = 0
		mediaPlayer = new MediaPlayer()
		mediaPlayerInit()
	}

	//region Player logic
	public void playSong()
	{
		mediaPlayer.reset()
		def playingSong = songList[songPosition]
		def currentSong = playingSong.ID
		def trackURI = ContentUris.withAppendedId(AudioMedia.EXTERNAL_CONTENT_URI, currentSong)
		try{ mediaPlayer.setDataSource(applicationContext, trackURI) }
		catch(Exception e){ Log.e("MUSIC SERVICE", "Error setting data source", e) /* TODO: Handle fucking exception */ }
		mediaPlayer.prepareAsync()
	}
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
	void onCompletion(MediaPlayer mediaPlayer){}

	@Override
	boolean onError(MediaPlayer mediaPlayer, int i, int i2){ return false }

	@Override
	void onPrepared(MediaPlayer mediaPlayer){ mediaPlayer.start() }
	//endregion

	private mediaPlayerInit()
	{
		mediaPlayer.setWakeMode(applicationContext, PowerManager.PARTIAL_WAKE_LOCK)
		mediaPlayer.onPreparedListener = this
		mediaPlayer.onCompletionListener = this
		mediaPlayer.onErrorListener = this
	}

	//region GET/SET
	SongList getSongList(){ return songList }
	void setSongList(SongList songList){ this.songList = songList }
	int getSongPos(){ return songPosition }
	void setSongPos(int songPos){ this.songPosition = songPos }
	//endregion

	public class MusicBinder extends Binder
	{
		MusicService getService(){ return MusicService.this }
	}
}
