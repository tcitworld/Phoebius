package augier.fr.phoebius.utils


import android.content.ContentResolver
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore

class SongList
{
	private final String SONG_TITLE = MediaStore.Audio.Media.TITLE
	private final String SONG_ID = MediaStore.Audio.Media._ID
	private final String SONG_ARTIST = MediaStore.Audio.Media.ARTIST
	private final Uri MUSIC_URI = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI

	private ContentResolver musicResolver
	private ArrayList<Song> songList = []
	private int currentSong
	private Cursor musicCursor

	public SongList(ContentResolver _musicResolver)
	{
		musicResolver = _musicResolver
		currentSong = 0
		musicCursor = queryCursor
	}

	public ArrayList<Song> getSongList()
	{
		songList.empty
		musicCursor = queryCursor

		if(musicCursor!=null && musicCursor.moveToFirst())
		{
			int titleColumn = songTitleColumn
			int idColumn = songIdColumn
			int artistColumn = songArtistColumn

			/* StackOverflow
			musicCursor.collect{
				long thisId = it.getLong(idColumn)
				String thisTitle = it.getString(titleColumn)
				String thisArtist = it.getString(artistColumn)
				songList.add(new Song(thisId, thisTitle, thisArtist))
			}
			*/


			while(musicCursor.moveToNext())
			{
				long thisId = musicCursor.getLong(idColumn)
				String thisTitle = musicCursor.getString(titleColumn)
				String thisArtist = musicCursor.getString(artistColumn)
				songList.add(new Song(thisId, thisTitle, thisArtist))
			}
			


		}
		return this.sort()
	}

	public ArrayList<Song> sort()
	{
		songList.sort({ song1, song2 ->  return song1.artist.compareTo(song2.artist) })
		return songList
	}

	public Song getAt(int idx){ return songList[idx] }

	//region GET/SET
	public int getLenght(){ return songList.size() }
	private int getSongTitleColumn(){ return musicCursor.getColumnIndex(SONG_TITLE) }
	private int getSongIdColumn(){ return musicCursor.getColumnIndex(SONG_ID) }
	private int getSongArtistColumn(){ return musicCursor.getColumnIndex(SONG_ARTIST) }
	private Cursor getQueryCursor(){ return musicResolver.query(MUSIC_URI, null, null, null, null) }
	//endregion
}
