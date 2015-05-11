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
import augier.fr.phoebius.core.MusicService.ACTIONS
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

		[R.id.btnPrevious, R.id.btnBackward, R.id.btnPlayPause,
		 R.id.btnForward,R.id.btnNext].each{
			remoteViews.setOnClickPendingIntent(
					(int)it, generateAction(it))
		}
	}

	private PendingIntent generateAction(int btn)
	{
		String action = ""
		switch(btn)
		{
			case R.id.btnPrevious:
				action = ACTIONS.ACTION_PREVIOUS.VAL
				break
			case R.id.btnNext:
				action = ACTIONS.ACTION_NEXT.VAL
				break
			case R.id.btnBackward:
				action = ACTIONS.ACTION_BACKWARD.VAL
				break
			case R.id.btnForward:
				action = ACTIONS.ACTION_FORWARD.VAL
				break
			case R.id.btnPlayPause:
				action = ACTIONS.ACTION_PLAY_PAUSE.VAL
				break
		}
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
		def play = { remoteViews.setImageViewResource(R.id.btnPlayPause,R.drawable.btn_pause) }
		def pause = { remoteViews.setImageViewResource(R.id.btnPlayPause,R.drawable.btn_play) }
		remoteViews.setImageViewBitmap(R.id.notifAlbumCover, song.cover)
		remoteViews.setTextViewText(R.id.notifSongTitleLabel, song.title)
		remoteViews.setTextViewText(R.id.notifSongArtistLabel, song.artist)

        if(musicService.playing) play()
		else pause()

		notification.contentView = remoteViews
		notification.bigContentView = remoteViews
		def nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
		nm.notify(NOTIFICATION_TAG, 0, notification)
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

	public static NotificationPlayer getInstance()
	{
		if(!INSTANCE) INSTANCE = new NotificationPlayer()
		return INSTANCE
	}

	private Context getContext(){ return PhoebiusApplication.context }
	private SongList getSongList(){ return  SongList.instance }
	private MusicService getMusicService(){ return PhoebiusApplication.musicService }
}