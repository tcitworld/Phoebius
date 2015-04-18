package augier.fr.phoebius.utils


import android.content.ContentResolver
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore;

public abstract class MusicQueryBuilder
{
	public static final String SONG_ID = MediaStore.Audio.Media._ID
	public static final String SONG_TITLE = MediaStore.Audio.Media.TITLE
	public static final String SONG_ARTIST = MediaStore.Audio.Media.ARTIST
	public static final String SONG_YEAR = MediaStore.Audio.Media.YEAR
	public static final String SONG_ALBUM_ID = MediaStore.Audio.Media.ALBUM_ID
	public static final String SONG_ALBUM = MediaStore.Audio.Media.ALBUM
	public static final String SONG_NUMBER = MediaStore.Audio.Media.TRACK
	public static final String ALBUM_TITLE =MediaStore.Audio.Albums.ALBUM
	public static final String ALBUM_ARTIST = MediaStore.Audio.Albums.ARTIST
	public static final String ALBUM_DATE = MediaStore.Audio.Albums.FIRST_YEAR
	public static final String ALBUM_NB_SONG = MediaStore.Audio.Albums.NUMBER_OF_SONGS
	public static final String ALBUM_COVER = MediaStore.Audio.Albums.ALBUM_ART

	public static final Uri MUSIC_URI = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
	public static final Uri ALBUM_URI = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI
	protected final String[] columns = [ALBUM_TITLE, ALBUM_ARTIST,
	                                  ALBUM_DATE, ALBUM_NB_SONG, ALBUM_COVER]

	protected Cursor musicCursor
	protected ContentResolver musicResolver

	protected int getSongTitleColumn(){ return musicCursor.getColumnIndex(SONG_TITLE) }
	protected int getSongIdColumn(){ return musicCursor.getColumnIndex(SONG_ID) }
	protected int getSongArtistColumn(){ return musicCursor.getColumnIndex(SONG_ARTIST) }
	protected int getSongAlbumColumn(){ return musicCursor.getColumnIndex(SONG_ALBUM) }
	protected int getSongAlbumIdColumn(){ return musicCursor.getColumnIndex(SONG_ALBUM_ID) }
	protected int getSongYearColumn(){ return musicCursor.getColumnIndex(SONG_YEAR) }
	protected int getSongNumberColumn(){ return musicCursor.getColumnIndex(SONG_NUMBER) }
	protected int getAlbumArtistColumn(){ return musicCursor.getColumnIndex(ALBUM_ARTIST) }
	protected int getAlbumTitleColumn(){ return musicCursor.getColumnIndex(ALBUM_TITLE) }
	protected int getAlbumDateColumn(){ return musicCursor.getColumnIndex(ALBUM_DATE) }
	protected int getAlbumNbSongsColumn(){ return musicCursor.getColumnIndex(ALBUM_NB_SONG) }
	protected int getAlbumCoverColumn(){ return musicCursor.getColumnIndex(ALBUM_COVER) }
	protected Cursor getQueryCursor(){ return musicResolver.query(MUSIC_URI, null, null, null, null) }
	protected Cursor getAlbumCursor(){ return musicResolver.query(ALBUM_URI, columns, null, null, null) }
}
