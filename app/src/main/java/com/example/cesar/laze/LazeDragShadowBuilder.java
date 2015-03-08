package com.example.cesar.laze;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.util.Log;
import android.view.View;

/**
 * Created by Cesar on 2/8/2015.
 */
public class LazeDragShadowBuilder extends View.DragShadowBuilder {
    final static String tag = "LAZE";
    private static Bitmap shadow;
    // The drag shadow image, defined as a drawable thing
    View view;

    // Defines the constructor for myDragShadowBuilder
    public LazeDragShadowBuilder(View v) {

        // Stores the View parameter passed to myDragShadowBuilder.
        super(v);

        // Creates a draggable image that will fill the Canvas provided by the system.
        //shadow = new ColorDrawable(Color.LTGRAY);
        //shadow = ((LazeView) v).bmpMirror;
        //view = v;
    }

    // Defines a callback that sends the drag shadow dimensions and touch point back to the
    // system.
    @Override
    public void onProvideShadowMetrics(Point size, Point touch) {
        Log.d(tag, "entering onProvideShadowMetrics");
        // Defines local variables
        int width, height;

        // Sets the width of the shadow to that of the last touched Bmp's
        width = ((LazeView) getView()).getBlockViewQuadWidth() * 2;
        // Sets the height of the shadow to that of the last touched Bmp's
        height = ((LazeView) getView()).getBlockViewQuadWidth() * 2;
        // Sets the size parameter's width and height values. These get back to the system
        // through the size parameter.
        size.set(width, height);
        // Sets the touch point's position to be in the middle of the drag shadow
        touch.set(width / 2, height / 2);
    }

    // Defines a callback that draws the drag shadow in a Canvas that the system constructs
    // from the dimensions passed in onProvideShadowMetrics().
    @Override
    public void onDrawShadow(Canvas canvas) {
        Log.d(tag, "entering onDrawShadow");
        Bitmap scaledBmp = Bitmap.createScaledBitmap(((LazeView) getView()).getLastObjectouchedBmp(), ((LazeView) getView()).getBlockViewQuadWidth() * 2, ((LazeView) getView()).getBlockViewQuadWidth() * 2, true);
        canvas.drawBitmap(scaledBmp, 0, 0, null);
    }
}
