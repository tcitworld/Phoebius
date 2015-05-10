package augier.fr.phoebius


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.view.MenuItem
import android.support.v4.app.NavUtils
import augier.fr.phoebius.UI.PlaylistDetailFragment
import augier.fr.phoebius.utils.Song
import groovy.transform.CompileStatic

@CompileStatic
public class PlaylistDetailActivity extends FragmentActivity
{
	ArrayList<Song> songs

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_playlist_detail)
		Intent intent = getIntent()
		songs = intent.getParcelableArrayListExtra("songs")
		def playlistDetail = new PlaylistDetailFragment(songs)
		supportFragmentManager.beginTransaction()
		                      .add(R.id.playlistDetailFrame, playlistDetail)
		                      .commit()
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		int id = item.itemId
		if(id == android.R.id.home)
		{
			NavUtils.navigateUpFromSameTask(this)
			return true
		}
		return super.onOptionsItemSelected(item)
	}
}
