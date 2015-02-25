package augier.fr.phoebius.UI


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.LinearLayout
import android.widget.TextView
import augier.fr.phoebius.R
import augier.fr.phoebius.utils.Song

public class SongAdapter extends BaseAdapter
{
	private ArrayList<Song> songs
	private LayoutInflater songInf
	
	public SongAdapter(Context c, ArrayList<Song> _songs)
	{
		songs = _songs
		songInf = LayoutInflater.from(c)
	}

	@Override
	public int getCount(){ return songs.size() }

	@Override
	public Object getItem(int arg0) {
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	public View getView(int position, View convertView, ViewGroup parent)
	{
		LinearLayout songLay = songInf.inflate(R.layout.song_list, parent, false) as LinearLayout
		Song currSong = songs.get(position)
		TextView songTitle = songLay.findViewById(R.id.songTitle) as TextView
		TextView songArtist = songLay.findViewById(R.id.songArtist) as TextView
		songTitle.setText(currSong.getTitle())
		songArtist.setText(currSong.getArtist())
		songLay.setTag(position)
		return songLay
	}
}
