package augier.fr.phoebius.UI


import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.LinearLayout
import groovy.transform.CompileStatic

@CompileStatic
abstract class AbstractAdaptater extends BaseAdapter
{
	protected LinearLayout layout

	@Override public int getCount(){ return 0 }
	@Override public Object getItem(int arg0){ return null; }
	@Override public long getItemId(int arg0){ return 0; }

	protected <T> T getView(int idx, Class<T> tClass){ return layout.findViewById(idx) as T }
	protected static LinearLayout inflate(Activity a, int r, ViewGroup p)
	{
		LayoutInflater li = LayoutInflater.from(a)
		return li.inflate(r, p, false) as LinearLayout
	}
}
