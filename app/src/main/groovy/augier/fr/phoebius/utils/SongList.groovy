package augier.fr.phoebius.utils


import android.content.ContentResolver
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.util.Log

class SongList extends MusicQueryBuilder
{
	private ArrayList<Song> currSongList = []
	private ArrayList<Album> thisAlbumList = []
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

			while(musicCursor.moveToNext())
			{
				long thisId = musicCursor.getLong(songIdColumn)
				String thisTitle = musicCursor.getString(songTitleColumn)
				String thisArtist = musicCursor.getString(songArtistColumn)
				long thisAlbumId = musicCursor.getLong(songAlbumIdColumn)
				String thisAlbum = musicCursor.getString(songAlbumColumn)
				int thisSongNumber = musicCursor.getInt(songNumberColumn)
				int thisSongYear = musicCursor.getInt(songYearColumn)
				currSongList.add(
					new Song(thisId, thisTitle, thisArtist, thisAlbumId,
					         thisAlbum, thisSongNumber, thisSongYear))
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
			while(musicCursor.moveToNext())
			{
				String thisTitle = musicCursor.getString(albumTitleColumn)
				String thisArtist = musicCursor.getString(albumArtistColumn)
				String thisDate = musicCursor.getString(albumDateColumn)
				String thisNbSongs = musicCursor.getString(albumNbSongsColumn)
				String albumCoverPath = musicCursor.getString(albumCoverColumn)
				thisAlbumList.add(new Album(thisTitle, thisArtist, thisDate,
				                            thisNbSongs, albumCoverPath))
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
//endregion
}
