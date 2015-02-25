package augier.fr.phoebius.utils


import android.content.ContentResolver
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import groovy.transform.CompileStatic

class SongList
{	
	private ContentResolver musicResolver
	private ArrayList<Song> songList = []

	public  SongList(ContentResolver _musicResolver){ musicResolver = _musicResolver }
	
	public ArrayList<Song> getSongList()
	{
		songList.empty
		
		Uri musicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
		Cursor musicCursor = musicResolver.query(musicUri, null, null, null, null)

		if(musicCursor!=null && musicCursor.moveToFirst())
		{
			int titleColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.TITLE)
			int idColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media._ID)
			int artistColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)
			
			
			long thisId = musicCursor.getLong(idColumn)
			String thisTitle = musicCursor.getString(titleColumn)
			String thisArtist = musicCursor.getString(artistColumn)
			songList.add(new Song(thisId, thisTitle, thisArtist))
			
			while(musicCursor.moveToNext())
			{
				thisId = musicCursor.getLong(idColumn)
				thisTitle = musicCursor.getString(titleColumn)
				thisArtist = musicCursor.getString(artistColumn)
				songList.add(new Song(thisId, thisTitle, thisArtist))
			}

		}
		return songList
	}
	
	public ArrayList<Song> sort()
	{
		songList.sort(new SongComparator())
		return songList
	}

	public Song getAt(int idx){ return songList[idx] }
	
	private class SongComparator implements Comparator<Song>
	{
		@Override
		int compare(Song a, Song b){ return a.getTitle().compareTo(b.getTitle()) }
	}
}
