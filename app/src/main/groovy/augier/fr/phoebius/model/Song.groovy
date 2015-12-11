package augier.fr.phoebius.model


import android.content.ContentUris
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import com.arasthel.swissknife.annotations.Parcelable
import groovy.json.JsonBuilder
import groovy.transform.CompileStatic


/**
 * Representation of a song
 */
@CompileStatic
@Parcelable(exclude = { defaultSong })
class Song extends SongDataBase implements Comparable
{
    private Long id
    private String title
    private String artist
    private String albumName
    private Album album
    private Integer trackNumber
    private Integer year
    private static final Song defaultSong = new Song(
        new Long(-1), "Song title", "Song artist", "Song albumName", 0, 0)

    /**
     * Builds a new song
     * @param songID ID of the song in the Android's DB
     * @param songTitle Song title
     * @param songArtist Artist of the song
     * @param songAlbum Song's albumName
     * @param songNb Song's rank on the albumName
     * @param songYear Song's year of release
     */
    private Song(Long songID, String songTitle, String songArtist,
                String songAlbum, Integer songNb, Integer songYear)
    {
        id = songID
        title = songTitle
        artist = songArtist
        albumName = songAlbum
        trackNumber = songNb
        year = songYear
    }

    public static void createOne(Long songID, String songTitle, String songArtist,
                              String songAlbum, Integer songNb, Integer songYear)
    {
        this.@allSongs << new Song(songID, songTitle, songArtist,
                             songAlbum, songNb, songYear)
    }

    //region GET/SET
    /** @return Song id     */
    public Long getID(){ return id }

    /** @return Song title     */
    public String getTitle(){ return title }

    /** @return Song artist     */
    public String getArtist(){ return artist }

    /** @return Song URI (path in FS)     */
    public Uri getURI()
    {
        return ContentUris.withAppendedId(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id)
    }

    /** @return Song albumName     */
    public Album getAlbum()
    {
        if(!album){ album = Album.findByName(albumName) }
        return album
    }

    /** @return Song rank in albumName     */
    public int getTrackNumber(){ return trackNumber }

    /** @return Song year of release     */
    public int getYear(){ return year }

    public Bitmap getCover(){ return this.getAlbum().cover }

    //endregion
    @Override
    public String toString()
    {
        return new JsonBuilder(toMap()).toPrettyString()
    }

    public LinkedHashMap<String, String> toMap()
    {
        return [
            ID         : "${ID}",
            title      : "${title}",
            artist     : "${artist}",
            album      : "${albumName}",
            trackNumber: "${trackNumber}",
            year       : "${year}"
        ]
    }

    public static Song fromMap(Map values)
    {
        String ID = values["ID"]
        String title = values["title"]
        String artist = values["artist"]
        String album = values["albumName"]
        String trackNumber = values["trackNumber"]
        String year = values["year"]

        return new Song(
            new Long(ID), title, artist, album,
            new Integer(trackNumber), new Integer(year))
    }

    public static Song getDefaultSong(){ return defaultSong }

    /**
     * Compares for sorting
     *
     * Compare by 2 criterias: by albumName title first and then by rank on the albumName.
     *
     * @param o Other object to compare to
     * @return Result of comparison
     */
    @Override
    int compareTo(Object o)
    {
        if(!o instanceof Song) return 0
        Song other = o as Song
        return this.albumName <=> other.albumName ?: this.trackNumber <=> other.trackNumber
    }
}