package augier.fr.phoebius

import android.app.Activity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ListView
import augier.fr.phoebius.utils.SongList
import augier.fr.phoebius.UI.SongAdapter
import org.androidannotations.annotations.EActivity

@EActivity
public class MainActivity extends Activity {
	public static final String APP_NAME ="Phoebius";

	private ListView songView;
	private SongList songList;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		songView = findViewById(R.id.songView) as ListView
		songList = new SongList(getContentResolver());
		def _songList = songList.songList;

		SongAdapter songAdt = new SongAdapter(this, _songList);
		songView.setAdapter(songAdt);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		int id = item.getItemId();
		if(id == R.id.action_settings){ return true; }
		return super.onOptionsItemSelected(item);
    }
}
