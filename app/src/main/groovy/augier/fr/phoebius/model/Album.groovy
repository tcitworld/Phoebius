package augier.fr.phoebius.model


import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import augier.fr.phoebius.PhoebiusApplication
import augier.fr.phoebius.R
import com.arasthel.swissknife.SwissKnife
import groovy.transform.CompileStatic

/**
 * Represents an albumName
 *
 * This class is used by {@link augier.fr.phoebius.UI.AlbumListFragment}
 * in adaptater to display albums
 *
 * @see {@link SongManager}
 */
@CompileStatic
public class Album extends SongDataBase implements Comparable
{
    private String albumArtist
    private String albumTitle
    private String date
    private String nbSongs
    private Bitmap cover
    private Playlist songs
    private static Album defaultAlbum = new Album("", "", "", "", "")

    /**
     * Constructor
     *
     * @param albumTitle Title of albumName
     * @param albumArtist Name of albumName's artist
     * @param date Date of release
     * @param nbSongs Number of songs on the albumName
     * @param coverPath Artwork path (will be resoved to a {@link Bitmap}
     */
    private Album(String albumTitle, String albumArtist,
        String date, String nbSongs, String coverPath)
    {
        this.albumArtist = albumArtist
        this.albumTitle = albumTitle
        this.date = date ?: ""
        this.nbSongs = nbSongs

        if(coverPath != null && new File(coverPath).exists())
            this.cover = BitmapFactory.decodeFile(coverPath)
        else this.cover = defaultCover

    }

    public static void createOne(String albumTitle, String albumArtist,
                          String date, String nbSongs, String coverPath)
    {
        this.@allAlbums << new Album(albumTitle, albumArtist, date, nbSongs, coverPath)
    }

    //region GET/SET
    /** @return Album artist     */
    String getAlbumArtist(){ return albumArtist }

    /** @return Album titel     */
    String getAlbumTitle(){ return albumTitle }

    /** @return Album release date     */
    String getDate(){ return date }

    /** @return Album artist     */
    String getNbSongs(){ return nbSongs }

    /** @return Album artist     */
    Bitmap getCover(){ return cover }

    Playlist getSongs()
    {
        if(!songs)
        {
            songs = new Playlist(this.@allSongs.findAll{
                it.album.albumTitle == this.albumTitle
            });
        }
        return songs
    }

    public AlbumList getAllAlbums(){ return this.@allAlbums }
    //endregion

    public static Album findByName(String name)
    {
        return this.@allAlbums.find{
            it.albumTitle == name
        } ?: defaultAlbum
    }

    @Override
    int compareTo(Object o)
    {
        if(!o instanceof Album) return 0
        Album other = o as Album
        int byArtist = this.albumArtist <=> other.albumArtist
        int byDate = this.date <=> other.date
        int byTitle = this.albumTitle <=> other.albumTitle

        if(byArtist) return byArtist
        if(byDate) return byDate
        return byTitle
    }

    private static Bitmap getDefaultCover()
    {
        return BitmapFactory.decodeResource(
            PhoebiusApplication.resources, R.drawable.default_cover)
    }

    //region QUERY
    public static void query(){ SwissKnife.runOnBackground(this, "bgCreateAlbumList") }

    private static void bgCreateAlbumList()
    {
        Cursor albumCursor = getCursor(ALBUM_URI, ALBUM_COLS)

        if(albumCursor?.moveToFirst())
        {
            def getString = { String columnName ->
                return albumCursor.getString(
                    albumCursor.getColumnIndex(columnName))
            }

            while(albumCursor.moveToNext())
            {
                createOne(
                    getString(ALBUM_TITLE),
                    getString(ALBUM_ARTIST),
                    getString(ALBUM_DATE),
                    getString(ALBUM_NB_SONG),
                    getString(ALBUM_COVER)
                )
            }
        }
        albumCursor.close()
        this.@allAlbums.sort()
    }
    //endregion
}