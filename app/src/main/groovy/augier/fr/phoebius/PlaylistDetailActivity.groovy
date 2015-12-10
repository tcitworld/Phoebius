package augier.fr.phoebius


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.support.v4.app.NavUtils
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
        Intent intent = getIntent()
        songs = intent.getParcelableArrayListExtra("songs")
        setContentView(R.layout.activity_playlist_detail)
    }

    @Override void onBackPressed(){ NavUtils.navigateUpFromSameTask(this) }

    public ArrayList<Song> getSongs(){ return songs }
}
