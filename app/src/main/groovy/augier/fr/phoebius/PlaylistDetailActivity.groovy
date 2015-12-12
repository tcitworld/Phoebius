package augier.fr.phoebius


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.support.v4.app.NavUtils
import android.support.v7.app.AppCompatActivity
import augier.fr.phoebius.model.Playlist
import groovy.transform.CompileStatic

@CompileStatic
public class PlaylistDetailActivity extends AppCompatActivity
{
    Playlist songs

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState)
        Intent intent = getIntent()
        songs = intent.getParcelableArrayListExtra("songs") as Playlist
        setContentView(R.layout.activity_playlist_detail)
    }

    @Override void onBackPressed(){ NavUtils.navigateUpFromSameTask(this) }

    public Playlist getSongs(){ return songs }
}
