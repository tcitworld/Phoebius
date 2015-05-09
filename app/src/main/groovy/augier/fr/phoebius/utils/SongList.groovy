package augier.fr.phoebius.utils


import android.graphics.Bitmap
import android.util.Log
import augier.fr.phoebius.MainActivity


/**
 * This class manages the creation, listing and iteration through the musics
 *
 * This class is a singleton to easier it's use everywhere in the code.
 * It is an abstraction of Andrdoid FS
 */
class SongList extends MusicQueryBuilder
{
	private static SongList INSTANCE
	private ArrayList<Song> currSongList = []
	private ArrayList<Album> thisAlbumList = []
	private LinkedHashMap<String, Bitmap> covers = [:]
	private LinkedHashMap<String, ArrayList<Song>> playlists = ["Test":[]]
	private Long currentSongId
	private Closure stopCallback = {}
	private Closure playCallback = {}
	private boolean loop

	private SongList()
	{
		musicResolver = MainActivity.applicationContext.contentResolver
		musicCursor = queryCursor
		createAlbumList()
		createSongList()
		currentSongId = songList[0]?.ID ?: -1
		loop = false
	}

	/**
	 * Query the DB to retrieve the list of songs
	 *
	 * Creates an {@link ArrayList} of {@link Song} from the query an sorts the list.
	 *
	 * @see {@link android.content.ContentResolver#query} and {@link android.database.Cursor}
	 */
	private void createSongList()
	{
		currSongList.clear()
		musicCursor = queryCursor

		if(musicCursor != null && musicCursor.moveToFirst())
		{

			while(musicCursor.moveToNext())
			{
				currSongList.add(new Song(
					musicCursor.getLong(songIdColumn),
			        musicCursor.getString(songTitleColumn),
			        musicCursor.getString(songArtistColumn),
			        musicCursor.getString(songAlbumColumn),
			        musicCursor.getInt(songNumberColumn),
			        musicCursor.getInt(songYearColumn))
				)
			}
		}
		currSongList.sort()
	}

	/**
	 * Query the DB to retrieve the list of albums
	 *
	 * Creates an {@link ArrayList} of {@link Album} from the query an sorts the list.
	 *
	 * @see {@link android.content.ContentResolver#query} and {@link android.database.Cursor}
	 */
	private void createAlbumList()
	{
		thisAlbumList.clear()
		musicCursor = albumCursor

		if(musicCursor != null && musicCursor.moveToFirst())
		{
			while(musicCursor.moveToNext())
			{
				def album = new Album(
						musicCursor.getString(albumTitleColumn),
						musicCursor.getString(albumArtistColumn),
						musicCursor.getString(albumDateColumn),
						musicCursor.getString(albumNbSongsColumn),
						musicCursor.getString(albumCoverColumn))

				thisAlbumList.add(album)
				covers[album.albumTitle] = album.cover
			}
		}
		thisAlbumList.sort()
	}

	public boolean createPlaylist(String name)
	{
		if(playlists.containsKey(name)){ return false }
		playlists[name] = new ArrayList<>()
		return true
	}

	public void addToPlaylist(String name, Song song){ playlists[name].add(song) }
	private String playlistsAsJson()
	{
		String result = "{"
		playlists.each{
			result += "${it.key}:["
			it.value.each{ result += "{${it.toJson()}}, "}
			result += "]"
		}
		return result + "}"
	}

	/** Overriding of [] operator */
	public Song getAt(int idx){ return currSongList[idx] }

	/**
	 * Retrives the next song to be played
	 *
	 * @return The next song in the list or null if last song and
	 * the list doesn't loop
	 */
	public Song getNextSong()
	{
		int idx = nextSongIdx
		if(idx < 0) return null
		else{ return currSongList[idx] }
	}

	/**
	 * Retrives the previous song to be played
	 *
	 * @return The next song in the list or null if first song and
	 * the list doesn't loop
	 */
	public Song getPreviousSong()
	{
		int idx = previousSongIdx
		if(idx < 0) return null
		else return  currSongList[idx]
	}

	/**
	 * Change the current song to the next one and play it
	 *
	 * If the playback isn't looped and there is no next song, then stops.
	 *
	 * @return this
	 *
	 * @see {@link #setPlayCallback} and {@link #setStopCallback}
	 */
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

	/**
	 * Change the current song to the previous one and play it
	 *
	 * If the playback isn't loopeda nd there is no prevous song, then stops.
	 *
	 * @return this
	 *
	 * @see {@link #setPlayCallback} and {@link #setStopCallback}
	 */
	public SongList moveToPreviousSong()
	{
		Song prev = getPreviousSong()
		Log.e(this.class.toString(), "Previous song: ${prev}")
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

	/**
	 * Retrives the index of a Song by its {@link Song#getID() ID}
	 * @param id Song ID
	 * @return Index in the current list or -1 if not found
	 */
	private int findIndexById(Long id){ return currSongList.findIndexOf{ it.ID == id } }

	/**
	 * Retrieve a Song by its {@link Song#getID() ID}
	 * @param id Song ID
	 * @return Song in the current list or null if not found
	 */
	private Song findById(Long id)
	{
		int idx = findIndexById(id)
		return idx > 0 && idx < currSongList.size() ? currSongList[idx] : null
	}

	/** @return Index of the next Song or -1 if no next song */
	private int getNextSongIdx()
	{
		int currentSongIdx = findIndexById(currentSongId)
		if(currentSongIdx == this.lenght - 1){ return -1 }
		else{ return (currentSongIdx + 1) % this.lenght }
	}

	/** @return Index of the previous Song or -1 if no previous song */
	private int getPreviousSongIdx()
	{
		int currentSongIdx = findIndexById(currentSongId)
		if(currentSongIdx == 0){ return -1 }
		else{ return (currentSongIdx + this.lenght - 1) % this.lenght }
	}

	//region GET/SET
	/** @return The length of the current playing playlist */
	public int getLenght(){ return currSongList.size() }
	/** @return Whether the playback is looped on the list or not */
	public boolean getLoop(){ return loop }
	/** Set the playback loop */
	public void setLoop(boolean loop){ this.loop = loop }
	/** @return The current playing list */
	public ArrayList<Song> getCurrSongList(){ return currSongList }
	/** @return The current playing song */
	public Song getCurrentSong(){ return findById(currentSongId) }
	/** Sets the current playing song */
	public void setCurrentSong(Song song){ this.currentSongId = song.ID }
	/** Sets the callback to execute when the playback has to stop (e.g. playlist is finished) */
	public void setStopCallback(Closure stopCallback){ this.stopCallback = stopCallback }
	/** Sets the callback to execute when the playback has to start */
	public void setPlayCallback(Closure playCallback){ this.playCallback = playCallback }
	/** @return The album list*/
	public ArrayList<Album> getAlbumList(){ return thisAlbumList }
	/** @return The complete song list */
	public ArrayList<Song> getSongList(){ return currSongList }
	/** @return The singleton instance */
	public static SongList getInstance()
	{
		if(INSTANCE == null){ INSTANCE = new SongList() }
		return INSTANCE
	}
	/** @return Cover for album title or default cover */
	public Bitmap getCoverFor(String albumTitle){ return covers[albumTitle] ?: Album.defaultCover }
	public ArrayList<String> getAllPlaylists(){ return playlists.keySet() }
	public ArrayList<Song> getPlaylist(String name){ return playlists[name] ?: [] }

//endregion
}
