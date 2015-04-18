package augier.fr.phoebius.UI


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
	MediaController mediaController
	ListView listView

	public PlayerControl(MusicServiceConnection musicConnection, MediaController mediaController)
	{
		this.musicConnection = musicConnection

		mediaController.mediaPlayer = mediaPlayerControl
		mediaController.anchorView = listView
		mediaController.enabled = true

		this.mediaController = mediaController
		mediaController.setPrevNextListeners(
				new View.OnClickListener() { @Override public void onClick(View v) { playNext() }},
				new View.OnClickListener() { @Override public void onClick(View v) { playPrev() }})
	}

	//region MediaController
	private void playNext(){
		musicService.playNext()
		mediaController.show()
	}

	private void playPrev(){
		musicService.playPrevious()
		mediaController.show()
	}

	void show(){ mediaController.show() }

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
}
