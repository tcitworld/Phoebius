package augier.fr.phoebius.model


import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import augier.fr.phoebius.PhoebiusApplication
import groovy.transform.CompileStatic;

@CompileStatic
public abstract class SongDataBase
{
    public static final String SONG_ID = MediaStore.Audio.Media._ID
    public static final String SONG_TITLE = MediaStore.Audio.Media.TITLE
    public static final String SONG_ARTIST = MediaStore.Audio.Media.ARTIST
    public static final String SONG_YEAR = MediaStore.Audio.Media.YEAR
    public static final String SONG_ALBUM = MediaStore.Audio.Media.ALBUM
    public static final String SONG_NUMBER = MediaStore.Audio.Media.TRACK
    public static final String ALBUM_TITLE = MediaStore.Audio.Albums.ALBUM
    public static final String ALBUM_ARTIST = MediaStore.Audio.Albums.ARTIST
    public static final String ALBUM_DATE = MediaStore.Audio.Albums.FIRST_YEAR
    public static final String ALBUM_NB_SONG = MediaStore.Audio.Albums.NUMBER_OF_SONGS
    public static final String ALBUM_COVER = MediaStore.Audio.Albums.ALBUM_ART

    public static final Uri MUSIC_URI = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
    public static final Uri ALBUM_URI = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI
    public static final String[] ALBUM_COLS = [ALBUM_TITLE, ALBUM_ARTIST,
                                               ALBUM_DATE, ALBUM_NB_SONG, ALBUM_COVER]
    protected static Playlist allSongs = []
    protected static AlbumList allAlbums = []

    public static abstract void query();

    protected static Cursor getCursor(Uri a, String[] b)
    { return PhoebiusApplication.context.contentResolver.query(a, b, null, null, null) }
}
