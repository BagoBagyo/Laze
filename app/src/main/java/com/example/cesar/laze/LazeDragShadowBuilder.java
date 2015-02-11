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
        view = v;
    }

    // Defines a callback that sends the drag shadow dimensions and touch point back to the
    // system.
    @Override
    public void onProvideShadowMetrics(Point size, Point touch) {
        Log.e(tag, "entering onProvideShadowMetrics");
        // Defines local variables
        int width, height;

        // Sets the width of the shadow to half the width of the original View
        width = getView().getWidth() / 2;
        width = 180;
        // Sets the height of the shadow to half the height of the original View
        height = getView().getHeight() / 2;
        height = 180;
        // The drag shadow is a ColorDrawable. This sets its dimensions to be the same as the
        // Canvas that the system will provide. As a result, the drag shadow will fill the
        // Canvas.
        //shadow.setBounds(0, 0, width, height);

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
        Log.e(tag, "entering onDrawShadow");

        // Draws the ColorDrawable in the Canvas passed in from the system.
        //Paint paint = new Paint();
        canvas.drawBitmap(((LazeView) getView()).bmpMirror, 0, 0, null);
        //lazeView.onDraw(canvas);
    }
}
