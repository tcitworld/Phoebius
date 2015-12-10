package augier.fr.phoebius.core


import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.support.v4.app.NotificationCompat
import android.widget.RemoteViews
import augier.fr.phoebius.PhoebiusApplication
import augier.fr.phoebius.R
import augier.fr.phoebius.utils.Song
import augier.fr.phoebius.utils.SongList

/**
 * Helper class for showing and canceling player notifications
 *
 * This class makes heavy use of the {@link NotificationCompat.Builder} helper
 * class to create notifications in a backward-compatible way.
 */
enum NotificationPlayer
{
    INSTANCE()

    /** Unique identifier for this type of notification. */
    private static final String NOTIFICATION_TAG = "${R.string.app_name}#NotifPlayer"
    private final def FOR_BUTTON = [
        (R.id.btnPlayPause): MusicService.ACTIONS.ACTION_PLAY_PAUSE,
        (R.id.btnBackward) : MusicService.ACTIONS.ACTION_BACKWARD,
        (R.id.btnForward)  : MusicService.ACTIONS.ACTION_FORWARD,
        (R.id.btnNext)     : MusicService.ACTIONS.ACTION_NEXT,
        (R.id.btnPrevious) : MusicService.ACTIONS.ACTION_PREVIOUS
    ]
    private Notification notification
    private RemoteViews remoteViews

    private NotificationPlayer()
    {
        notification = new NotificationCompat.Builder(context)
            .setDefaults(Notification.DEFAULT_VIBRATE)
            .setSmallIcon(R.drawable.notification_player_icon)
            .setTicker("${R.string.app_name}")
            .setOngoing(true).build()

        remoteViews = new RemoteViews(context.packageName, R.layout.player_notification)

        FOR_BUTTON.keySet().each{
            remoteViews.setOnClickPendingIntent(it, generateAction(it))
        }
    }

    private PendingIntent generateAction(int btn)
    {
        String action = FOR_BUTTON[btn]
        Intent intent = new Intent(context, MusicService.class)
        intent.setAction(action)
        return PendingIntent.getService(context, 1, intent, 0);
    }

    /**
     * Shows or updates the notification
     *
     * Shows or update a previously shown notification of
     * this type, with the given parameters. Make sure to follow the
     * <a href="https://developer.android.com/design/patterns/notifications.html">
     * Notification design guidelines</a> when doing so.
     *
     * @see #cancel()
     */
    public void fireNotification()
    {
        Song song = songList.currentSong
        def play = { remoteViews.setImageViewResource(R.id.btnPlayPause, R.drawable.btn_pause) }
        def pause = { remoteViews.setImageViewResource(R.id.btnPlayPause, R.drawable.btn_play) }
        remoteViews.setImageViewBitmap(R.id.notifAlbumCover, song.cover)
        remoteViews.setTextViewText(R.id.notifSongTitleLabel, song.title)
        remoteViews.setTextViewText(R.id.notifSongArtistLabel, song.artist)

        if(musicService.playing){ play() }
        else{ pause() }

        notification.contentView = remoteViews
        notification.bigContentView = remoteViews
    }

    /**
     * Cancels any notifications of this type previously shown using
     * {@link #notify()}.
     */
    public void cancel()
    {
        def nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        nm.cancel(NOTIFICATION_TAG, 0)
    }

    private Context getContext(){ return PhoebiusApplication.context }

    private SongList getSongList(){ return SongList.INSTANCE }

    private MusicService getMusicService(){ return PhoebiusApplication.musicService }

    public Notification getNotification(){ return this.@notification }
}