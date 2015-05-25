package augier.fr.phoebius.utils


import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import augier.fr.phoebius.PhoebiusApplication
import augier.fr.phoebius.core.ConfigManager
import groovy.json.JsonBuilder
import groovy.transform.CompileStatic


/**
 * This class manages the creation, listing and iteration through the musics
 *
 * This class is a singleton to easier it's use everywhere in the code.
 * It is an abstraction of Andrdoid FS
 */
@CompileStatic
class SongList extends MusicQueryBuilder
{
	private static SongList INSTANCE
	private ArrayList<Song> currSongList = []
	private ArrayList<Album> thisAlbumList = []
	private LinkedHashMap<String, Bitmap> covers = [:]
	private LinkedHashMap<String, ArrayList<Song>> playlists = [Test:new ArrayList<>()]
	private Long currentSongId
	private boolean loop

	private SongList()
	{
		musicResolver = context.contentResolver
		musicCursor = queryCursor
		createAlbumList()
		createSongList()
		createPlaylists()
		currentSongId = getFirstId()
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
		musicCursor.close()
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
		musicCursor.close()
		thisAlbumList.sort()
	}

	public boolean newPlaylist(String name)
	{
		if(playlists.containsKey(name)){ return false }
		playlists[name] = new ArrayList<>()
		return true
	}

	public void addToPlaylist(String name, Song song){ playlists[name].add(song) }

	private void createPlaylists()
	{
		def res = configManager[ConfigManager.WKK_PLAYLIST] as Map
		res.each{
			String name = it.key as String
			def songs = it.value as List
			playlists[name] = new ArrayList<>()
			songs.each{
				def song = it as Map<String, String>
				playlists[name].add(Song.fromMap(song)) }
		}
	}

	public void finalize()
	{
		def bckpPl = [:]
		playlists.each{
			def plName = []
			it.value.each{ plName.add(it.toMap()) }
			bckpPl[it.key] = plName
		}
		configManager.addValue(ConfigManager.WKK_PLAYLIST, bckpPl)
	}

	/** Overriding of [] operator */
	public Song getAt(int idx){ return currSongList[idx] }

	public SongList next()
	{
		currentSongId = nextSong?.ID ?: firstId
		return this
	}

	public SongList previous()
	{
		currentSongId = previousSong?.ID ?: firstId
		return this
	}

	/**
	 * Retrives the index of a Song by its {@link Song#getID() ID}
	 * @param id Song ID
	 * @return Index in the current list or -1 if not found
	 */
	private int findIndexById(Long id)
	{
		return currSongList.findIndexOf{
			Song it -> return it.ID == id }
	}

	/**
	 * Retrieve a Song by its {@link Song#getID() ID}
	 * @param id Song ID
	 * @return Song in the current list or null if not found
	 */
	private Song findById(Long id)
	{
		int idx = findIndexById(id)
		return idx >= 0 && idx < currSongList.size() ? currSongList[idx] : null
	}

	//region GET/SET
	/** @return Index of the next Song or -1 if no next song */
	private int getNextSongIdx()
	{
		int currentSongIdx = findIndexById(currentSongId)
		if(currentSongIdx == this.lenght - 1 && !loop){ return -1 }
		else{ return (currentSongIdx + 1) % this.lenght }
	}

	/** @return Index of the previous Song or -1 if no previous song */
	private int getPreviousSongIdx()
	{
		int currentSongIdx = findIndexById(currentSongId)
		if(currentSongIdx == 0 && !loop){ return -1 }
		else{ return (currentSongIdx + this.lenght - 1) % this.lenght }
	}

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
	/** @return The context of the application */
	private Context getContext(){ return PhoebiusApplication.context }
	private ConfigManager getConfigManager(){ return PhoebiusApplication.configManager }
	private Long getFirstId(){ return songList[0]?.ID ?: -1 }
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
	public void setCurrentSong(Song song){ this.currentSongId = song?.ID ?: getFirstId() }
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
	public Bitmap getCoverFor(String albumTitle){
		return covers[albumTitle] ?: Album.defaultCover }
	public ArrayList<String> getAllPlaylists(){
		return playlists.keySet() as ArrayList<String> }
	public ArrayList<Song> getPlaylist(String name){
		return playlists[name] ?: [] as ArrayList<Song> }
	//endregion
}
