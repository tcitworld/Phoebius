package augier.fr.phoebius.UI


import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.LinearLayout
import groovy.transform.CompileStatic


/**
 * Convenient class
 *
 * This class just provides convenient methods for conceiving adaptaters
 */
@CompileStatic
abstract class AbstractAdaptater extends BaseAdapter
{
	/**
	 * Inflated layout
	 */
	protected LinearLayout layout

	/**
	 * Does nothing
	 * @param arg0
	 * @return null
	 */
	@Override public Object getItem(int arg0){ return null; }

	/**
	 * Does nothing
	 * @param arg0
	 * @return 0
	 */
	@Override public long getItemId(int arg0){ return 0; }

	/**
	 * Shorthand to find a view and return already casted
	 * @param idx Id to find (example: R.id.button1)
	 * @param tClass Class to cast the view to (example: TextView)
	 * @return The view casted in the specified type T
	 */
	protected <T> T getView(int idx, Class<T> tClass){ return layout.findViewById(idx) as T }

	/**
	 * Shorthand to inflate a LinearLayout
	 * @param a Just use getActivity()
	 * @param r Id of the layout to inflate (example R.layout.activity_list_item)
	 * @param p Just use the {@link ViewGroup} parent passed to your {@link BaseAdapter#getView} method
	 * @return The {@link LinearLayout} inflated (just uses {@link LayoutInflater#inflate}
	 */
	protected static LinearLayout inflate(Activity a, int r, ViewGroup p)
	{
		LayoutInflater li = LayoutInflater.from(a)
		return li.inflate(r, p, false) as LinearLayout
	}
}
