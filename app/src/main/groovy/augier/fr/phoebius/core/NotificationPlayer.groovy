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
import augier.fr.phoebius.model.Song
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
        (R.id.notifBtnPlayPause): PlayerActions.ACTION_PLAY_PAUSE,
        (R.id.notifBtnBackward) : PlayerActions.ACTION_BACKWARD,
        (R.id.notifBtnForward)  : PlayerActions.ACTION_FORWARD,
        (R.id.notifBtnNext)     : PlayerActions.ACTION_NEXT,
        (R.id.notifBtnPrevious) : PlayerActions.ACTION_PREVIOUS
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

    public void updateNotification(Song song)
    {
        remoteViews.setImageViewBitmap(R.id.notifAlbumCover, song.cover)
        remoteViews.setTextViewText(R.id.notifSongTitleLabel, song.title)
        remoteViews.setTextViewText(R.id.notifSongArtistLabel, song.artist)
        if(musicService.playing)
            remoteViews.setImageViewResource(R.id.notifBtnPlayPause, R.drawable.btn_pause)
        else
            remoteViews.setImageViewResource(R.id.notifBtnPlayPause, R.drawable.btn_play)

        notification.contentView = remoteViews
        notification.bigContentView = remoteViews
    }

    private Context getContext(){ return PhoebiusApplication.context }

    private MusicService getMusicService(){ return PhoebiusApplication.musicService }

    public Notification getNotification(){ return this.@notification }
}