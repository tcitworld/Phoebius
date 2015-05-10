package augier.fr.phoebius


import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.view.Menu
import android.view.MenuItem
import augier.fr.phoebius.UI.MainPageFragment
import augier.fr.phoebius.UI.PlayerControlFragment
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
	protected void onStart()
	{
		super.onStart()

		def frag = new MainPageFragment(supportFragmentManager)
		def contr = new PlayerControlFragment()
		supportFragmentManager.beginTransaction().add(R.id.mainFrame, frag).commit()
		supportFragmentManager.beginTransaction().add(R.id.mediaController, contr).commit()
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		menuInflater.inflate(R.menu.main, menu)
		return true
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch(item.itemId)
		{
			case R.id.action_end:
				application.onTerminate()
				this.finish()
				break
			default:
				break
		}
		return super.onOptionsItemSelected(item)
	}
}