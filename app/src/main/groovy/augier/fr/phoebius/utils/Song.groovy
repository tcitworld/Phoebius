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
	private long albumId
	private String album
	private int trackNumber
	private int year

	public Song(long songID, String songTitle, String songArtist, long songAlbumId,
	            String songAlbum, int songNb, int songYear)
	{
		id = songID
		title = songTitle
		artist = songArtist
		albumId = songAlbumId
		album = songAlbum
		trackNumber = songNb
		year = songYear
	}

	public long getID(){ return id }
	public String getTitle(){ return title }
	public String getArtist(){ return artist }
	public Uri getURI(){ return ContentUris.withAppendedId(SongList.MUSIC_URI, id) }
	public long getAlbumId(){ return albumId }
	public String getAlbum(){ return album }
	public int getTrackNumber(){ return trackNumber }
	public int getYear(){ return year }

	@Override
	public String toString(){
		return """
			ID: ${id},
			Artist: ${artist},
			Title: ${title},
			Album: ${album},
			Number: ${trackNumber}
			Year: ${year}"""
	}
}