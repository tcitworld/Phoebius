package augier.fr.phoebius.utils


import android.content.ContentUris
import android.net.Uri
import groovy.transform.CompileStatic

@CompileStatic
class Song
{
	private long id
	private String title
	private String artist

	public Song(long songID, String songTitle, String songArtist)
	{
		id = songID
		title = songTitle
		artist = songArtist
	}

	public long getID(){ return id }
	public String getTitle(){ return title }
	public String getArtist(){ return artist }
	public Uri getURI(){ return ContentUris.withAppendedId(SongList.MUSIC_URI, id) }

	@Override
	public String toString()
	{
		return """
			ID: ${id},
			Artist: ${artist},
			Title: ${title}"""
	}
}