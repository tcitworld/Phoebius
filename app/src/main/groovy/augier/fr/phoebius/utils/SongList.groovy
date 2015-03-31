package augier.fr.phoebius.utils


import android.content.ContentResolver
import android.content.ContentUris
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore

class SongList
{
	private ContentResolver musicResolver

	private ArrayList<Song> currSongList = []
	private Cursor musicCursor
	private long currentSongId

	private boolean loop

	public SongList(ContentResolver _musicResolver)
	{
		musicResolver = _musicResolver
		musicCursor = queryCursor
		currentSongId = songList[0].ID // TODO : Gérer le cas liste vide
		loop = false
	}

	public ArrayList<Song> getSongList()
	{
		currSongList.empty
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
		return this.sort()
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

	Song getCurrentSong(){ return findById(currentSongId) }

	public SongList moveToNextSong(Closure playCallback, Closure stopCallback)
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
			playCallback(currentSongId)
		}
		return this
	}

	public SongList moveToPreviousSong(Closure playCallback, Closure stopCallback)
	{
		Song prev = getNextSong()
		if(prev == null)
		{
			currentSongId = currSongList[0].ID
			stopCallback()
		}
		else
		{
			currentSongId = prev.ID
			playCallback(currentSongId)
		}
		return this
	}

	private int findIndexById(long id){ return currSongList.findIndexOf{ it.ID == id } }
	private Song findById(long id){ return currSongList[findIndexById(id)] }
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
	private int getSongTitleColumn(){ return musicCursor.getColumnIndex(SONG_TITLE) }
	private int getSongIdColumn(){ return musicCursor.getColumnIndex(SONG_ID) }
	private int getSongArtistColumn(){ return musicCursor.getColumnIndex(SONG_ARTIST) }
	private Cursor getQueryCursor(){ return musicResolver.query(MUSIC_URI, null, null, null, null) }
	//endregion
}
