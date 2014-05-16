package us.nineworlds.serenity.widgets;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader.TileMode;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * A general purpose Layout which simply renders a reflection of its contained
 * child Views in the remaining space below them within the bounds of this
 * control. For {@link ReflectingLayout} to work properly, it must be setup to
 * provide sufficient empty space below its children.
 * 
 * Copyright Tom Leach
 */
public class ReflectingLayout extends LinearLayout {

	/** The maximum ratio of the height of the reflection to the source image. */
	private float maxReflectionRatio = 0.9F;

	/** The {@link Paint} object we'll use to create the reflection. */
	private Paint paint;

	private Matrix vFlipMatrix;

	/**
	 * Instantiates a new reflecting layout.
	 * 
	 * @param context
	 *            the context
	 * @param attrs
	 *            the attrs
	 */
	public ReflectingLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	/**
	 * Instantiates a new reflecting layout.
	 * 
	 * @param context
	 *            the context
	 */
	public ReflectingLayout(Context context) {
		super(context);
		init();
	}

	/**
	 * Initialises the layout.
	 */
	private void init() {
		// Ensures that we redraw when our children are redrawn.
		setAddStatesFromChildren(true);

		// Important to ensure onDraw gets called.
		setWillNotDraw(false);
		setDrawingCacheEnabled(true);

		// Create the paint object which we'll use to create the reflection
		// gradient
		paint = new Paint();
		paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));

		// Create a matrix which can flip images vertically
		vFlipMatrix = new Matrix();
		vFlipMatrix.preScale(1, -1);
	}

	/**
	 * @return the mAX_REFLECTION_RATIO
	 */
	public float getMaxReflectionRatio() {
		return maxReflectionRatio;
	}

	/**
	 * Controls how much of the image/view is mirrored. The default is 90%.
	 * Setting it to 0.5f will do 50% of the image being reflected.
	 * 
	 * @param maxReflectionRatio
	 *            The size of the reflection in percent
	 */
	public void setMaxReflextionRatio(float maxReflectionRatio) {
		this.maxReflectionRatio = maxReflectionRatio;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see android.view.View#onDraw(android.graphics.Canvas)
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		// Only actually do anything if there is space to actually draw a
		// reflection
		if (getReflectionHeight() > 0) {

			// Create a bitmap to hold the drawing of child views and pass this
			// to a temp canvas
			Bitmap sourceBitmap = Bitmap.createBitmap(getMeasuredWidth(),
					getMeasuredHeight(), Bitmap.Config.ARGB_8888);
			Canvas tempCanvas = new Canvas(sourceBitmap);

			// Draw the content of this layout onto our temporary canvas.
			super.dispatchDraw(tempCanvas);

			// Calculate the height of the reflection and the bottom position of
			// child views.
			int reflectionHeight = getReflectionHeight();
			int childBottom = getMaxChildBottom();

			// Create a new bitmap from the source image which has been
			// vertically flipped and
			// only includes the region occupied child views.
			Bitmap flippedBitmap = Bitmap.createBitmap(sourceBitmap, 0, 0,
					sourceBitmap.getWidth(), childBottom, vFlipMatrix, false);

			// Create a bitmap to hold just the reflection
			Bitmap fadedBitmap = Bitmap.createBitmap(getMeasuredWidth(),
					reflectionHeight, Bitmap.Config.ARGB_8888);

			Canvas fadeCanvas = new Canvas(fadedBitmap);
			fadeCanvas.drawBitmap(flippedBitmap, 0, 0, null);

			LinearGradient gradient = new LinearGradient(0, 0, 0,
					reflectionHeight, 0x30FFFFFF, 0x00FFFFFF, TileMode.CLAMP);
			paint.setShader(gradient);

			// Now use some clever PorterDuff shading to get the fading effect.
			fadeCanvas.drawRect(0, 0, getMeasuredWidth(), reflectionHeight,
					paint);

			// Draw our image onto the canvas
			canvas.drawBitmap(fadedBitmap, 0, childBottom, null);

		}
	}

	/**
	 * Finds the bottom of the lowest view contained by this layout.
	 * 
	 * @return the bottom of the lowest view
	 */
	private int getMaxChildBottom() {
		int maxBottom = 0;
		for (int i = 0; i < getChildCount(); i++) {
			int bottom = getChildAt(i).getBottom();
			if (bottom > maxBottom) {
				maxBottom = bottom;
			}
		}
		return maxBottom;
	}

	/**
	 * Gets the highest top edge of all contained views.
	 * 
	 * @return the min child top
	 */
	private int getMinChildTop() {
		int minTop = Integer.MAX_VALUE;
		for (int i = 0; i < getChildCount(); i++) {
			int top = getChildAt(i).getTop();
			if (top < minTop) {
				minTop = top;
			}
		}
		return minTop;
	}

	/**
	 * Gets the height of the space covered by all children.
	 * 
	 * @return the total child height
	 */
	private int getTotalChildHeight() {
		// The max value of any child's "bottom" minus the minimum of any "top"
		return getMaxChildBottom() - getMinChildTop();
	}

	/**
	 * Gets the height of the reflection to be drawn.
	 * 
	 * <p>
	 * This is the minimum of either:
	 * <ul>
	 * <li>The remaining height between the bottom of this layout and the
	 * bottom-most child view</li>
	 * <li>The maximum reflection ratio</li>
	 * </p>
	 * 
	 * @return the reflection height
	 */
	private int getReflectionHeight() {
		return (int) Math.min(getMeasuredHeight() - getMaxChildBottom(),
				getTotalChildHeight() * maxReflectionRatio);
	}
}