package augier.fr.phoebius.core

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.media.MediaPlayer.OnPreparedListener
import android.media.MediaPlayer.OnErrorListener
import android.media.MediaPlayer.OnCompletionListener
import android.os.IBinder
import org.androidannotations.annotations.EService;

@EService
public class MusicService extends Service implements 
		OnPreparedListener, OnErrorListener,
		OnCompletionListener
{
	@Override
	IBinder onBind(Intent intent)
	{
		return null
	}

	@Override
	void onCompletion(MediaPlayer mediaPlayer)
	{

	}

	@Override
	boolean onError(MediaPlayer mediaPlayer, int i, int i2)
	{
		return false
	}

	@Override
	void onPrepared(MediaPlayer mediaPlayer)
	{

	}
}
