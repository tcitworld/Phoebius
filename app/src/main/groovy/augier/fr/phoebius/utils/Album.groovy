package augier.fr.phoebius.utils



import android.graphics.Bitmap
import android.graphics.BitmapFactory
import augier.fr.phoebius.MainActivity
import augier.fr.phoebius.PhoebiusApplication
import augier.fr.phoebius.R
import groovy.transform.CompileStatic


/**
 * Represents an album
 *
 * This class is used by {@link augier.fr.phoebius.UI.AlbumListFragment}
 * in adaptater to display albums
 *
 * @see {@link SongList}, {@link MusicQueryBuilder}
 */
@CompileStatic
public class Album implements Comparable
{
	private String albumArtist
	private String albumTitle
	private String date
	private String nbSongs
	private Bitmap cover

	/**
	 * Constructor
	 *
	 * @param albumTitle Title of album
	 * @param albumArtist Name of album's artist
	 * @param date Date of release
	 * @param nbSongs Number of songs on the album
	 * @param coverPath Artwork path (will be resoved to a {@link Bitmap}
	 */
	public Album(String albumTitle,String albumArtist,
	             String date, String nbSongs, String coverPath)
	{
		this.albumArtist = albumArtist
		this.albumTitle = albumTitle
		this.date = date?:""
		this.nbSongs = nbSongs

		if(coverPath != null && new File(coverPath).exists())
			{ this.cover = BitmapFactory.decodeFile(coverPath) }
		else this.cover = defaultCover

	}

	/** @return Album artist */
	String getAlbumArtist(){ return albumArtist }
	/** @return Album titel */
	String getAlbumTitle(){ return albumTitle }
	/** @return Album release date */
	String getDate(){ return date }
	/** @return Album artist */
	String getNbSongs(){ return nbSongs }
	/** @return Album artist */
	Bitmap getCover(){ return cover }


	@Override
	int compareTo(Object o)
	{
		if(!o instanceof Album) return 0
		Album other = o as Album
		int byArtist = this.albumArtist.compareTo(other.albumArtist)
		int byDate   = this.date.compareTo(other.date)
		int byTitle  = this.albumTitle.compareTo(other.albumTitle)

		if(byArtist) return byArtist
		if(byDate) return byDate
		return byTitle
	}

	public static Bitmap getDefaultCover()
	{
		return BitmapFactory.decodeResource(
			PhoebiusApplication.resources, R.drawable.default_cover)
	}
}
