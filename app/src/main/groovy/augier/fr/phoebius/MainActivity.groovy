package augier.fr.phoebius


import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import groovy.transform.CompileStatic

@CompileStatic
public class MainActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState)
        contentView = R.layout.activity_main
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    @Override protected void onResume(){ super.onResume() }

    @Override protected void onPause()
    {
        PhoebiusApplication.save()
        super.onPause()
    }
}
