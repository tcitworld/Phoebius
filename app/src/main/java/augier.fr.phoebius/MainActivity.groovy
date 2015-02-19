package augier.fr.phoebius
import android.app.Activity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import org.androidannotations.annotations.EActivity;
import augier.fr.phoebius.core.ConfigManager


@EActivity
public class MainActivity extends Activity {
	public static final String APP_NAME ="Phoebius";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
	    
	    ConfigManager config = new ConfigManager();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
