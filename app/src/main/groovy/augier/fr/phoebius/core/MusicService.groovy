package augier.fr.phoebius.core


import android.app.Service
import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.MediaPlayer.OnCompletionListener
import android.media.MediaPlayer.OnErrorListener
import android.media.MediaPlayer.OnPreparedListener
import android.os.Binder
import android.os.IBinder
import android.os.PowerManager
import augier.fr.phoebius.PhoebiusApplication
import augier.fr.phoebius.model.Song
import augier.fr.phoebius.model.SongManager

/**
 * This class takes care of playing the music and interacting with the controls
 */
class MusicService extends Service implements OnPreparedListener,
    OnErrorListener, OnCompletionListener
{
    /**
     * Our actual music player that will broadcast sound
     */
    private MediaPlayer mediaPlayer = new MediaPlayer()

    /**
     * The next music player for gapless playing
     */
    private MediaPlayer nextMediaPlayer = new MediaPlayer()

    /**
     * Our binder (thanks, Captain Obvious !)
     */
    private final IBinder musicBinder = new MusicBinder()

    /**
     * Variable to ensure the player is in a validate state
     */
    private IdleStateHandler idle = new IdleStateHandler()

    private NotificationPlayer notificationPlayer = NotificationPlayer.INSTANCE

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        handleIntent(intent);
        return START_NOT_STICKY
    }

    private void handleIntent(Intent intent)
    {
        if(intent == null || intent.getAction() == null) return
        PlayerActions action = PlayerActions.valueOf(intent.getAction());

        switch(action)
        {
            case PlayerActions.ACTION_PLAY_PAUSE:
                playPause()
                break
            case PlayerActions.ACTION_PREVIOUS:
                playPrevious()
                break
            case PlayerActions.ACTION_NEXT:
                playNext()
                break
            case PlayerActions.ACTION_FORWARD:
                forward()
                break
            case PlayerActions.ACTION_BACKWARD:
                backward()
                break
        }
    }

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
        mediaPlayerInit(mediaPlayer, songList.currentSong)
        prepareNextPlayer()
    }

    //region Player logic
    /**
     * Plays a song
     * @param song Song to be played (see {@link Song ])
     */
    public void play(Song song)
    {
        if(!song)
        { stop() }
        else
        {
            songList.currentSong = song
            mediaPlayerInit(mediaPlayer, songList.currentSong)
            prepareNextPlayer()
            playPause()
            PhoebiusApplication.bus.post(songList.currentSong)
            startForeground(1, notificationPlayer.notification)
        }
    }

    /**
     * Stops the player
     */
    public void stop()
    {
        mediaPlayer.stop()
        PhoebiusApplication.bus.post(PlayerActions.ACTION_STOP)
        stopForeground(false)
    }

    /**
     * Pauses the player
     */
    public void playPause()
    {
        if(playing) mediaPlayer.pause()
        else mediaPlayer.start()
        PhoebiusApplication.bus.post(PlayerActions.ACTION_PLAY_PAUSE)
    }

    /**
     * Seeks the song currently playing to a given position
     *
     * If nithing is playing, does nothing
     * @param position position to seek to given in milliseconds
     */
    public void seek(int position)
    {
        if(position > duration) mediaPlayer.seekTo(position - 2000)
        else if(position < 0) mediaPlayer.seekTo(0)
        else mediaPlayer.seekTo(position)
    }

    /**
     * Goes forward 10 seconds
     */
    public void forward(){ seek(position + 10000) }

    /**
     * Goes backward 10 seconds
     */
    public void backward(){ seek(position - 10000) }

    /**
     * Moves the song playing (or song to be played if the player
     * is paused) to the previous song.
     */
    public void playPrevious(){ play(songList.previousSong) }

    /**
     * Moves the song playing (or song to be played if the player
     * is paused) to the next song.
     */
    public void playNext(){ play(songList.nextSong) }

    //endregion

    //region Overrided methods
    @Override
    IBinder onBind(Intent intent)
    { return musicBinder }

    @Override
    public boolean onUnbind(Intent intent)
    {
        releasePlayers()
        notificationPlayer.cancel()
        return false
    }

    /**
     * Will play the next song as soon as the current on is finished
     * @param mediaPlayer
     */
    @Override void onCompletion(MediaPlayer me)
    {
        songList.next()
        mediaPlayer = nextMediaPlayer
        nextMediaPlayer = new MediaPlayer()
        prepareNextPlayer()
    }

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
    private void mediaPlayerInit(MediaPlayer m, Song song)
    {
        m.audioStreamType = AudioManager.STREAM_MUSIC
        m.setWakeMode(applicationContext, PowerManager.PARTIAL_WAKE_LOCK)
        m.onPreparedListener = this
        m.onCompletionListener = this
        m.onErrorListener = this

        m.reset()
        if(song?.URI)
        {
            m.setDataSource(applicationContext, song.URI)
            m.prepare()
            idle.validate()
        }
    }

    private void prepareNextPlayer()
    {
        Song next = songList.nextSong
        if(next) mediaPlayerInit(nextMediaPlayer, next)
        else nextMediaPlayer = null
        mediaPlayer.nextMediaPlayer = nextMediaPlayer
    }

    private void releasePlayers()
    {
        mediaPlayer.stop()
        mediaPlayer.release()
        mediaPlayer = null

        nextMediaPlayer.stop()
        nextMediaPlayer.release()
        nextMediaPlayer = null
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

    private SongManager getSongList(){ return SongManager.INSTANCE }

    //endregion

    /**
     * Our Binder
     */
    public class MusicBinder extends Binder
    {
        MusicService getService(){ return MusicService.this }
    }

    private class IdleStateHandler
    {
        private boolean ready = false

        public boolean isReady(){ return ready }

        public void validate(){ ready = true }
    }
}
