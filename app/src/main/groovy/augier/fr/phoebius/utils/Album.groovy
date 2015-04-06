package augier.fr.phoebius.utils


import groovy.transform.CompileStatic;

@CompileStatic
public class Album
{
	private long albumId
	private String albumArtist
	private String albumTitle
	private String date
	private String nbSongs

	public Album(long albumId, String albumTitle, String albumArtist, String date, String nbSongs)
	{
		this.albumId = albumId
		this.albumArtist = albumArtist
		this.albumTitle = albumTitle
		this.date = date
		this.nbSongs = nbSongs
	}

	long getAlbumId(){ return albumId }
	String getAlbumArtist(){ return albumArtist }
	String getAlbumTitle(){ return albumTitle }
	String getDate(){ return date }
	String getNbSongs(){ return nbSongs }

	@Override
	boolean equals(Object o)
	{
		if(!o instanceof Album){ return false }
		return ((Album)o).albumId == this.albumId
	}
}
