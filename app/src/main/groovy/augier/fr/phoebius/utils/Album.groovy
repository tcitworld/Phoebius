package augier.fr.phoebius.utils


import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.R
import android.util.Log
import groovy.transform.CompileStatic

@CompileStatic
public class Album
{
	private String albumArtist
	private String albumTitle
	private String date
	private String nbSongs
	private Bitmap cover

	public Album(String albumTitle,String albumArtist,
	             String date, String nbSongs, String coverPath)
	{
		this.albumArtist = albumArtist
		this.albumTitle = albumTitle
		this.date = date
		this.nbSongs = nbSongs

		if(coverPath != null && new File(coverPath).exists())
			{ this.cover = BitmapFactory.decodeFile(coverPath) }
		else{ BitmapFactory.decodeResource(Resources.getSystem(), R.drawable.star_on) }

	}

	String getAlbumArtist(){ return albumArtist }
	String getAlbumTitle(){ return albumTitle }
	String getDate(){ return date }
	String getNbSongs(){ return nbSongs }
	Bitmap getCover(){ return cover }

	@Override
	public String toString()
	{
		return """Album{
	Artist: ${albumArtist},
	Title: ${albumTitle},
	Date : ${date},
	Number of songs: ${nbSongs},
	Cover: ${cover?.toString()}
}"""
	}
}
