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
import com.squareup.otto.Subscribe

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
        (R.id.btnPlayPause): PlayerActions.ACTION_PLAY_PAUSE,
        (R.id.btnBackward) : PlayerActions.ACTION_BACKWARD,
        (R.id.btnForward)  : PlayerActions.ACTION_FORWARD,
        (R.id.btnNext)     : PlayerActions.ACTION_NEXT,
        (R.id.btnPrevious) : PlayerActions.ACTION_PREVIOUS
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

        PhoebiusApplication.bus.register(this)
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
    @Subscribe
    public void getSong(Song song)
    {
        remoteViews.setImageViewBitmap(R.id.notifAlbumCover, song.cover)
        remoteViews.setTextViewText(R.id.notifSongTitleLabel, song.title)
        remoteViews.setTextViewText(R.id.notifSongArtistLabel, song.artist)

        update()
    }

    @Subscribe
    public void getPlayerAction(PlayerActions action)
    {
        switch(action)
        {
            case PlayerActions.ACTION_STOP:
                cancel()
                break
            case PlayerActions.ACTION_PLAY_PAUSE:
                computePlayPauseState()
                break
        }
    }

    private computePlayPauseState()
    {
        if(musicService.playing)
            remoteViews.setImageViewResource(R.id.btnPlayPause, R.drawable.btn_pause)
        else
            remoteViews.setImageViewResource(R.id.btnPlayPause, R.drawable.btn_play)
        update()
    }

    private void update()
    {
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

    private MusicService getMusicService(){ return PhoebiusApplication.musicService }

    public Notification getNotification(){ return this.@notification }
}