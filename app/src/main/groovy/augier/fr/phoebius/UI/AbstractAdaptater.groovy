package augier.fr.phoebius.UI


import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.LinearLayout
import groovy.transform.CompileStatic;

@CompileStatic
abstract class AbstractAdaptater extends BaseAdapter
{
	protected LinearLayout songLay

	protected <T> T getView(int idx, Class<T> tClass){ return songLay.findViewById(idx) as T }
	protected static LinearLayout inflate(Activity a, int r, ViewGroup p)
	{
		LayoutInflater li = LayoutInflater.from(a)
		return li.inflate(r, p, false) as LinearLayout
	}
}
