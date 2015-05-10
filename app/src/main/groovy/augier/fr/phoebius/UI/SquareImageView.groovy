package augier.fr.phoebius.UI


import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import groovy.transform.CompileStatic;


/**
 * Overriding class used to display square images
 */
@CompileStatic
public class SquareImageView extends ImageView
{
	SquareImageView(Context context, AttributeSet attrs){ super(context, attrs) }

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		super.onMeasure(widthMeasureSpec, heightMeasureSpec)
		setMeasuredDimension(measuredHeight, measuredHeight)
	}
}
