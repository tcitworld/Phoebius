package augier.fr.phoebius.utils


import android.net.Uri
import android.provider.MediaStore;


interface ISongList
{
	public static final String SONG_TITLE = MediaStore.Audio.Media.TITLE
	public static final String SONG_ID = MediaStore.Audio.Media._ID
	public static final String SONG_ARTIST = MediaStore.Audio.Media.ARTIST
	public static final Uri MUSIC_URI = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI

	public Song getNextSong()
	public Song getPreviousSong()
	public Song getCurrentSong()
	public ISongList moveToNextSong()
	public ISongList moveToPreviousSong()
}