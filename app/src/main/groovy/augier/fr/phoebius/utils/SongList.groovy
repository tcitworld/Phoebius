package augier.fr.phoebius.utils


import android.content.ContentResolver
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore


class SongList
{
	public static final String SONG_TITLE = MediaStore.Audio.Media.TITLE
	public static final String SONG_ID = MediaStore.Audio.Media._ID
	public static final String SONG_ARTIST = MediaStore.Audio.Media.ARTIST
	public static final String ALBUM_ID = MediaStore.Audio.Albums._ID
	public static final String ALBUM_TITLE =MediaStore.Audio.Albums.ALBUM
	public static final String ALBUM_ARTIST = MediaStore.Audio.Albums.ARTIST
	public static final String ALBUM_DATE = MediaStore.Audio.Albums.FIRST_YEAR
	public static final String ALBUM_NB_SONG = MediaStore.Audio.Albums.NUMBER_OF_SONGS
	public static final String ALBUM_COVER = MediaStore.Audio.Albums.ALBUM_ART

	public static final Uri MUSIC_URI = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
	public static final Uri ALBUM_URI = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI
	private final String[] columns = [ALBUM_ID, ALBUM_TITLE, ALBUM_ARTIST,
	                                 ALBUM_DATE, ALBUM_NB_SONG, ALBUM_COVER]

	private ContentResolver musicResolver
	private ArrayList<Song> currSongList = []
	private ArrayList<Album> thisAlbumList = []
	private Cursor musicCursor
	private long currentSongId
	private Closure stopCallback = {}
	private Closure playCallback = {}
	private boolean loop

	public SongList(ContentResolver _musicResolver)
	{
		musicResolver = _musicResolver
		musicCursor = queryCursor
		createAlbumList()
		createSongList()
		currentSongId = songList[0].ID // TODO : Gérer le cas liste vide
		loop = false
	}

	public void createSongList()
	{
		currSongList.clear()
		musicCursor = queryCursor

		if(musicCursor != null && musicCursor.moveToFirst())
		{
			int titleColumn = songTitleColumn
			int idColumn = songIdColumn
			int artistColumn = songArtistColumn

			while(musicCursor.moveToNext())
			{
				long thisId = musicCursor.getLong(idColumn)
				String thisTitle = musicCursor.getString(titleColumn)
				String thisArtist = musicCursor.getString(artistColumn)
				currSongList.add(new Song(thisId, thisTitle, thisArtist))
			}
		}
		currSongList = this.sort()
	}

	public void createAlbumList()
	{
		thisAlbumList.clear()

		musicCursor = albumCursor

		if(musicCursor != null && musicCursor.moveToFirst())
		{
			int idCol = albumIdColumn
			int titleCol = albumTitleColumn
			int artistCol = albumArtistColumn
			int dateCol = albumDateColumn
			int nbSongsCol = albumNbSongsColumn
			int artCol = albumCoverColumn

			while(musicCursor.moveToNext())
			{
				long thisId = musicCursor.getLong(idCol)
				String thisTitle = musicCursor.getString(titleCol)
				String thisArtist = musicCursor.getString(artistCol)
				String thisDate = musicCursor.getString(dateCol)
				String thisNbSongs = musicCursor.getString(nbSongsCol)
				String albumCoverPath = musicCursor.getString(artCol)
				thisAlbumList.add(new Album(
						thisId, thisTitle, thisArtist, thisDate, thisNbSongs, albumCoverPath))
			}


		}

		 thisAlbumList = thisAlbumList.sort({ album1, album2 ->
			int byArtist = album1.albumTitle.compareTo(album2.albumTitle)
			int byYear = album1.albumTitle.compareTo(album2.albumTitle)
			int byTitle = album1.albumTitle.compareTo(album2.albumTitle)
			if(byArtist != 0){ return byArtist }
			if(byYear != 0){ return byYear }
			return byTitle
		})
	}

	public ArrayList<Song> sort()
	{
		currSongList.sort({ song1, song2 -> return song1.artist.compareTo(song2.artist) })
		return currSongList
	}

	public Song getAt(int idx){ return currSongList[idx] }

	public Song getNextSong()
	{
		int _nextSongIdx = nextSongIdx
		if(_nextSongIdx < 0){ return null}
		else{ return currSongList[_nextSongIdx] }
	}

	public Song getPreviousSong()
	{
		int _prevSongIdx = previousSongIdx
		if(_prevSongIdx < 0){ return null }
		else{ return  currSongList[_prevSongIdx] }
	}

	public SongList moveToNextSong()
	{
		Song next = getNextSong()
		if(next == null)
		{
			currentSongId = currSongList[0].ID
			stopCallback()
		}
		else
		{
			currentSongId = next.ID
			playCallback(next)
		}
		return this
	}

	public SongList moveToPreviousSong()
	{
		Song prev = getPreviousSong()
		if(prev == null)
		{
			currentSongId = currSongList[0].ID
			stopCallback()
		}
		else
		{
			currentSongId = prev.ID
			playCallback(prev)
		}
		return this
	}

	private int findIndexById(long id){ return currSongList.findIndexOf{ it.ID == id } }
	private Song findById(long id)
	{
		try{ return currSongList[findIndexById(id)] }
		catch(ArrayIndexOutOfBoundsException e){ return null }
	}
	private int getNextSongIdx()
	{
		int currentSongIdx = findIndexById(currentSongId)
		if(currentSongIdx == this.lenght - 1){ return -1 }
		else{ return currentSongIdx + 1 % this.lenght }
	}
	private int getPreviousSongIdx()
	{
		int currentSongIdx = findIndexById(currentSongId)
		if(currentSongIdx == 0){ return -1 }
		else{ return currentSongIdx + this.lenght - 1 % this.lenght }
	}

	//region GET/SET
	public int getLenght(){ return currSongList.size() }
	public boolean getLoop(){ return loop }
	public void setLoop(boolean loop){ this.loop = loop }
	public ArrayList<Song> getCurrSongList(){ return currSongList }
	public Song getCurrentSong(){ return findById(currentSongId) }
	public void setCurrentSong(Song song){ this.currentSongId = song.ID }
	public void setStopCallback(Closure stopCallback){ this.stopCallback = stopCallback }
	public void setPlayCallback(Closure playCallback){ this.playCallback = playCallback }
	public ArrayList<Album> getAlbumList(){ return thisAlbumList }
	public ArrayList<Song> getSongList(){ return currSongList }
	private int getSongTitleColumn(){ return musicCursor.getColumnIndex(SONG_TITLE) }
	private int getSongIdColumn(){ return musicCursor.getColumnIndex(SONG_ID) }
	private int getSongArtistColumn(){ return musicCursor.getColumnIndex(SONG_ARTIST) }
	private int getAlbumArtistColumn(){ return musicCursor.getColumnIndex(ALBUM_ARTIST) }
	private int getAlbumTitleColumn(){ return musicCursor.getColumnIndex(ALBUM_TITLE) }
	private int getAlbumIdColumn(){ return musicCursor.getColumnIndex(ALBUM_ID) }
	private int getAlbumDateColumn(){ return musicCursor.getColumnIndex(ALBUM_DATE) }
	private int getAlbumNbSongsColumn(){ return musicCursor.getColumnIndex(ALBUM_NB_SONG) }
	private int getAlbumCoverColumn(){ return musicCursor.getColumnIndex(ALBUM_COVER) }
	private Cursor getQueryCursor(){ return musicResolver.query(MUSIC_URI, null, null, null, null) }
	private Cursor getAlbumCursor(){ return musicResolver.query(ALBUM_URI, null, null, null, null) }

//endregion
}
