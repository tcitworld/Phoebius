package augier.fr.phoebius.utils


import android.content.ContentResolver
import android.content.ContentUris
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore

class SongList implements ISongList
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

			/* StackOverflow
			musicCursor.collect{
				long thisId = it.getLong(idColumn)
				String thisTitle = it.getString(titleColumn)
				String thisArtist = it.getString(artistColumn)
				currSongList.add(new Song(thisId, thisTitle, thisArtist))
			}
			*/

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

	@Override
	public Song getNextSong()
	{
		int _nextSongIdx = nextSongIdx
		if(_nextSongIdx < 0){ return null}
		else{ return currSongList[_nextSongIdx] }
	}

	@Override
	public Song getPreviousSong()
	{
		int _prevSongIdx = previousSongIdx
		if(_prevSongIdx < 0){ return null }
		else{ return  currSongList[_prevSongIdx] }
	}

	@Override
	Song getCurrentSong(){ return findById(currentSongId) }

	@Override
	Uri getNextSongUri(){ return uriFromId(getPreviousSong().ID) }

	@Override
	Uri getPreviousSongUri(){ return uriFromId(getNextSong().ID) }

	@Override
	Uri getCurrentSongUri(){ return uriFromId(currentSongId) }

	@Override
	public ISongList moveToNextSong()
	{
		Song next = getNextSong()
		if(next == null){ currentSongId = currSongList[0].ID }
		else{ currentSongId = next.ID }
		currentSongId = getNextSong().ID
		return this
	}

	@Override
	public ISongList moveToPreviousSong()
	{
		Song prev = getNextSong()
		if(prev == null){ currentSongId = currSongList[0].ID }
		else{ currentSongId = prev.ID }
		return this
	}

	private int findIndexById(long id){ return currSongList.findIndexOf{ it.ID == id } }
	private Song findById(long id){ return currSongList[findIndexById(id)] }
	private int getNextSongIdx()
	{
		int currentSongIdx = findIndexById(currentSongId)
		if(currentSongIdx == currSongList.size() - 1){ return -1 }
		else{ return currentSongIdx + 1 % currSongList.size() }
	}
	private int getPreviousSongIdx()
	{
		int currentSongIdx = findIndexById(currentSongId)
		if(currentSongIdx == 0){ return -1 }
		else{ return currentSongIdx + currSongList.size() - 1 % currSongList.size() }
	}

	public static uriFromId(long id){ return ContentUris.withAppendedId(MUSIC_URI, id)}
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
