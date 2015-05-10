package augier.fr.phoebius.core


import android.app.Notification
import android.app.NotificationManager
import android.content.Context
import android.graphics.Bitmap
import android.support.v4.app.NotificationCompat
import android.widget.RemoteViews
import augier.fr.phoebius.MainActivity
import augier.fr.phoebius.PhoebiusApplication
import augier.fr.phoebius.R
import groovy.transform.CompileStatic


/**
 * Helper class for showing and canceling player notifications
 *
 * This class makes heavy use of the {@link NotificationCompat.Builder} helper
 * class to create notifications in a backward-compatible way.
 */
@CompileStatic
public class NotificationPlayer
{
    /** Unique identifier for this type of notification. */
    private static final String NOTIFICATION_TAG = "${PhoebiusApplication.APP_NAME}#NotifPlayer"
	private static NotificationPlayer INSTANCE

	private Notification notification
	private RemoteViews remoteViews

	private NotificationPlayer()
	{
		notification = new NotificationCompat.Builder(context)
				.setDefaults(Notification.DEFAULT_VIBRATE)
				.setSmallIcon(R.drawable.notification_player_icon)
				.setTicker("${PhoebiusApplication.APP_NAME}")
				.setOngoing(true).build()

		remoteViews = new RemoteViews(context.packageName, R.layout.player_notification)
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
	public void notify(Bitmap picture, String songTitle, String songArtiste)
	{
		remoteViews.setImageViewBitmap(R.id.notifAlbumCover, picture)
		remoteViews.setTextViewText(R.id.notifSongTitleLabel, songTitle)
		remoteViews.setTextViewText(R.id.notifSongArtistLabel, songArtiste)

		notification.contentView = remoteViews
		notification.bigContentView = remoteViews
		def nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
		nm.notify(NOTIFICATION_TAG, 0, notification)
    }

    /**
     * Cancels any notifications of this type previously shown using
     * {@link #notify(Bitmap, String, String)}.
     */
    public void cancel()
    {
        def nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        nm.cancel(NOTIFICATION_TAG, 0)
    }

	public static NotificationPlayer getInstance()
	{
		if(!INSTANCE) INSTANCE = new NotificationPlayer()
		return INSTANCE
	}

	private Context getContext(){ return PhoebiusApplication.context }
}