package augier.fr.phoebius.utils


import android.content.ContentUris
import android.net.Uri
import groovy.json.JsonBuilder
import groovy.json.JsonSlurper
import groovy.transform.CompileStatic


/**
 * Representation of a song
 */
@CompileStatic
class Song implements Comparable
{
	private Long id
	private String title
	private String artist
	private String album
	private int trackNumber
	private int year
	private Uri URI

	/**
	 * Builds a new song
	 * @param songID ID of the song in the Android's DB
	 * @param songTitle Song title
	 * @param songArtist Artist of the song
	 * @param songAlbum Song's album
	 * @param songNb Song's rank on the album
	 * @param songYear Song's year of release
	 */
	public Song(Long songID, String songTitle, String songArtist,
	            String songAlbum, int songNb, int songYear)
	{
		id = songID
		title = songTitle
		artist = songArtist
		album = songAlbum
		trackNumber = songNb
		year = songYear
		URI = ContentUris.withAppendedId(SongList.MUSIC_URI, id)
	}

	/** @return Song id */
	public Long getID(){ return id }
	/** @return Song title */
	public String getTitle(){ return title }
	/** @return Song artist */
	public String getArtist(){ return artist }
	/** @return Song URI (path in FS) */
	public Uri getURI(){ return URI }
	/** @return Song album */
	public String getAlbum(){ return album }
	/** @return Song rank in album */
	public int getTrackNumber(){ return trackNumber }
	/** @return Song year of release */
	public int getYear(){ return year }

	@Override
	public String toString(){
		return new JsonBuilder(toJson()).toPrettyString()
	}

	public String toJson()
	{
		return """{
	        "ID": ${ID},
			"title": ${title},
			"artist": ${artist},
			"album": ${album},
			"trackNumber": ${trackNumber},
			"year": ${year}
		}"""
	}

	public static Song fromJson(String json)
	{
		def result = new JsonSlurper().parseText(json)
		String ID = result["ID"]
		String title = result["title"]
		String artist = result["artist"]
		String album = result["album"]
		String trackNumber = result["trackNumber"]
		String year = result["year"]

		return new Song(
				new Long(ID), title, artist,
				album, new Integer(trackNumber), new Integer(year))
	}

	/**
	 * Compares for sorting
	 *
	 * Compare by 2 criterias: by album title first and then by rank on the album.
	 *
	 * @param o Other object to compare to
	 * @return Result of comparison
	 */
	@Override
	int compareTo(Object o)
	{
		if(!o instanceof Song) return 0
		Song other = o as Song
		int byAlbum  = this.album.compareTo(other.album)
		int byNumber = this.trackNumber.compareTo(other.trackNumber)

		if(byAlbum) return byAlbum
		return byNumber
	}
}