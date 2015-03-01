package augier.fr.phoebius.core

import android.app.Service
import android.content.ContentUris
import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.MediaPlayer.OnPreparedListener
import android.media.MediaPlayer.OnErrorListener
import android.media.MediaPlayer.OnCompletionListener
import android.provider.MediaStore.Audio.Media as AudioMedia
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
	private SongIterator songPosition
	private final IBinder musicBinder = new MusicBinder()

	@Override
	void onCreate()
	{
		super.onCreate()
		songPosition = new SongIterator()
		mediaPlayer = new MediaPlayer()
		mediaPlayerInit()
	}

	//region Player logic
	public void play(int position = songPosition.songPosition)
	{
		mediaPlayer.reset()
		songPosition.songPosition = position
		def playingSong = songList[songPosition.songPosition]
		def currentSong = playingSong.ID
		def trackURI = ContentUris.withAppendedId(AudioMedia.EXTERNAL_CONTENT_URI, currentSong)
		try{ mediaPlayer.setDataSource(applicationContext, trackURI) }
		catch(Exception e){ Log.e("MUSIC SERVICE", "Error setting data source", e) /* TODO: Handle fucking exception */ }
		mediaPlayer.prepareAsync()
		Log.e("PLAYING", "${currentSong}")
	}

	public void pause(){ mediaPlayer.pause() }
	public void seek(int position){ mediaPlayer.seekTo(position) }
	public void start(){ mediaPlayer.start() }
	public void playPrevious(){ play(songPosition.previous) }
	public void playNext(){ play(songPosition.next) }
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
		mediaPlayer.audioStreamType = AudioManager.STREAM_MUSIC
		mediaPlayer.setWakeMode(applicationContext, PowerManager.PARTIAL_WAKE_LOCK)
		mediaPlayer.onPreparedListener = this
		mediaPlayer.onCompletionListener = this
		mediaPlayer.onErrorListener = this
	}

	//region GET/SET
	SongList getSongList(){ return songList }
	void setSongList(SongList songList){ this.songList = songList }
	int getSongPosition(){ return songPosition.songPosition }
	int getPosition(){ return mediaPlayer.currentPosition }
	int getDuration(){ return mediaPlayer.duration }
	boolean isPlaying(){ return mediaPlayer.playing }
	Song getCurrentSong(){ return songList[songPosition.songPosition]}
	//endregion


	public class MusicBinder extends Binder
	{
		MusicService getService(){ return MusicService.this }
	}

	public class SongIterator
	{
		private int songPosition

		public SongIterator(int initialPosition = 0){ songPosition = initialPosition }

		public int getNext()
		{
			if(songList != null)
				{ songPosition = (songPosition + 1) % songList.lenght }
			return songPosition
		}

		public int getPrevious()
		{
			if(songList != null)
				{ songPosition = (songList.lenght + songPosition - 1) % songList.lenght }
			return songPosition
		}

		int getSongPosition(){ return songPosition }
		void setSongPosition(int songPosition)
			{ this.songPosition = songList == null ? 0 : (songPosition % songList.lenght) }
	}
}
