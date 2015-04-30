package augier.fr.phoebius.utils


import augier.fr.phoebius.MainActivity

class SongList extends MusicQueryBuilder
{
	private static SongList INSTANCE
	private ArrayList<Song> currSongList = []
	private ArrayList<Album> thisAlbumList = []
	private long currentSongId
	private Closure stopCallback = {}
	private Closure playCallback = {}
	private boolean loop

	private SongList()
	{
		musicResolver = MainActivity.applicationContext.contentResolver
		musicCursor = queryCursor
		createAlbumList()
		createSongList()
		currentSongId = songList[0].ID // TODO : Gérer le cas liste vide
		loop = false
	}

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
			        musicCursor.getLong(songAlbumIdColumn),
			        musicCursor.getString(songAlbumColumn),
			        musicCursor.getInt(songNumberColumn),
			        musicCursor.getInt(songYearColumn))
				)
			}
		}
		currSongList = this.sort()
	}

	private void createAlbumList()
	{
		thisAlbumList.clear()
		musicCursor = albumCursor

		if(musicCursor != null && musicCursor.moveToFirst())
		{
			while(musicCursor.moveToNext())
			{
				thisAlbumList.add(new Album(
					musicCursor.getString(albumTitleColumn),
					musicCursor.getString(albumArtistColumn),
					musicCursor.getString(albumDateColumn),
					musicCursor.getString(albumNbSongsColumn),
					musicCursor.getString(albumCoverColumn))
				)
			}
		}

		 thisAlbumList = thisAlbumList.sort{ album1, album2 ->
			int byArtist = album1.albumTitle.compareTo(album2.albumTitle)
			int byYear = album1.albumTitle.compareTo(album2.albumTitle)
			int byTitle = album1.albumTitle.compareTo(album2.albumTitle)
			if(byArtist != 0){ return byArtist }
			if(byYear != 0){ return byYear }
			return byTitle
		}
	}

	public ArrayList<Song> sort()
	{
		currSongList.sort{ song1, song2 ->
			return song1.artist.compareTo(song2.artist) }
		return currSongList
	}

	public Song getAt(int idx){ return currSongList[idx] }

	public Song getNextSong()
	{
		int idx = nextSongIdx
		if(idx < 0){ return null}
		else{ return currSongList[idx] }
	}

	public Song getPreviousSong()
	{
		int idx = previousSongIdx
		if(idx < 0){ return null }
		else{ return  currSongList[idx] }
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
	public static SongList getInstance()
	{
		if(INSTANCE == null){ INSTANCE = new SongList() }
		return INSTANCE
	}
//endregion
}
