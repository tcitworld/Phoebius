package augier.fr.phoebius.utils


import android.net.Uri
import android.provider.MediaStore;


interface ISongList
{


	public Song getNextSong()
	public Song getPreviousSong()
	public Song getCurrentSong()
	public ISongList moveToNextSong()
	public ISongList moveToPreviousSong()
}