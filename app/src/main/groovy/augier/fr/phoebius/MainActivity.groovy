package augier.fr.phoebius


import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.view.Menu
import android.view.MenuItem
import augier.fr.phoebius.UI.MainPageFragment
import groovy.transform.CompileStatic

@CompileStatic
public class MainActivity extends FragmentActivity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// Class init
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


}