package augier.fr.phoebius.UI


import android.content.Context
import android.view.View
import android.widget.ListView
import android.widget.MediaController
import android.widget.MediaController.MediaPlayerControl
import augier.fr.phoebius.core.MusicService
import augier.fr.phoebius.core.MusicServiceConnection


public class PlayerControl implements MediaPlayerControl
{
	MusicServiceConnection musicConnection
	MediaPlayerControl mediaPlayerControl = this
	MusicController musicController
	ListView listView

	public PlayerControl(MusicServiceConnection musicConnection, Context c, ListView lv)
	{
		this.musicConnection = musicConnection
		this.listView = lv

		musicController = new MusicController(c)
		musicController.setPrevNextListeners(
				new View.OnClickListener() { @Override public void onClick(View v) { playNext() }},
				new View.OnClickListener() { @Override public void onClick(View v) { playPrev() }})
	}

	//region MediaController
	private void playNext(){
		musicService.playNext()
		musicController.show()
	}

	private void playPrev(){
		musicService.playPrevious()
		musicController.show()
	}

	void show(){ musicController.show() }

	@Override
	int getDuration()
	{
		if(musicService == null){ return 0 }
		return playing ? musicService.duration : 0
	}

	@Override
	int getBufferPercentage()
	{
		if(!playing || currentPosition == 0 ){ return 0 }
		int percent = (currentPosition / duration) * 100
		return percent
	}

	@Override int getCurrentPosition(){ return musicService.position }
	@Override boolean isPlaying(){ return musicService != null ? musicService.playing : false }
	@Override void seekTo(int i){ musicService.seek(i) }
	@Override boolean canSeekBackward(){ return true }
	@Override boolean canSeekForward(){ return true }
	@Override boolean canPause(){ return true }
	@Override void start(){ musicService.start() }
	@Override void pause(){ musicService.pause() }
	@Override int getAudioSessionId(){ return 0 }
	//endregion

	private MusicService getMusicService(){ return musicConnection.musicService }

	private class MusicController extends MediaController
	{
		MusicController(Context context)
		{
			super(context)

			mediaPlayer = mediaPlayerControl
			anchorView = listView
			enabled = true
		}

		@Override
		public void hide(){}
	}
}
