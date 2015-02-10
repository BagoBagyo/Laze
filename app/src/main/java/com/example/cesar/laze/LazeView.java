package com.example.cesar.laze;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayDeque;

/**
 * Created by Cesar on 2/5/2015.
 */
public class LazeView extends View {
    final static String tag = "LAZE";
    static ImageView myShadow;
    private int playfieldWidth;
    private int playfieldHeight;
    private Block[][] blockGrid;
    private ArrayDeque<Ray> sources;
    private ArrayDeque<Target> targets;
    private Bitmap bmpOpen = BitmapFactory.decodeResource(getResources(), R.drawable.open);
    Bitmap bmp = bmpOpen;
    //Bitmap bmpGlass = BitmapFactory.decodeResource(getResources(), R.drawable.glass);
    //Bitmap bmpCrystal= BitmapFactory.decodeResource(getResources(), R.drawable.crystal);
    //Bitmap bmpWormhole = BitmapFactory.decodeResource(getResources(), R.drawable.wormhole);
    //Bitmap bmpBlackhole = BitmapFactory.decodeResource(getResources(), R.drawable.blackhole);
    private Bitmap bmpSource = BitmapFactory.decodeResource(getResources(), R.drawable.source);
    private Bitmap bmpTarget = BitmapFactory.decodeResource(getResources(), R.drawable.target);
    private Bitmap bmpMirror = BitmapFactory.decodeResource(getResources(), R.drawable.mirror);
    private Bitmap bmpLaser = BitmapFactory.decodeResource(getResources(), R.drawable.laser);


    public LazeView(Context context) {
        super(context);
    }

    public LazeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        AttributeSet myAttrs = attrs;
    }

    public static Bitmap RotateBitmap(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    public void setLazeViewParams(Block[][] blockGrid, ArrayDeque<Ray> sources, ArrayDeque<Target> targets) {
        this.blockGrid = blockGrid;
        this.sources = sources;
        this.targets = targets;

        playfieldWidth = blockGrid.length * 2;
        playfieldHeight = blockGrid[0].length * 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.e(tag, "entering onDraw");

        int x = getWidth();
        int y = getHeight();

        Paint paint = new Paint();

        for (int i = 0; i < playfieldWidth / 2; i++) {
            for (int j = 0; j < playfieldHeight / 2; j++) {
                Block block = blockGrid[i][j];
                switch (block.getType()) {
                    case OPEN:
                        bmp = bmpOpen;
                        break;
                    case MIRROR:
                        bmp = bmpMirror;
                        break;
                    case GLASS:
                        break;
                    case CRYSTAL:
                        break;
                    case WORMHOLE:
                        break;
                    case BLACKHOLE:
                        break;
                    default:

                        break;
                }

                int scaledWidth = x / playfieldWidth;
                int scaledHeight = y / playfieldHeight;
                int shortScale = (scaledWidth <= scaledHeight) ? scaledWidth : scaledHeight;
                int bmpX = block.getX() * shortScale;
                int bmpY = block.getY() * shortScale;
                Bitmap scaledBmp = Bitmap.createScaledBitmap(bmp, shortScale * 2, shortScale * 2, true);
                canvas.drawBitmap(scaledBmp, bmpX - shortScale, bmpY - shortScale, paint);


            }
        }
        // Draw sources
        if (sources != null) {
            for (Ray source : sources) {
                switch (source.getType()) {
                    case RED:
                        bmp = bmpSource;
                        break;
                    case GREEN:
                        bmp = bmpSource;
                        break;
                    default:
                        break;
                }
                int scaledWidth = x / playfieldWidth;
                int scaledHeight = y / playfieldHeight;
                int shortScale = (scaledWidth <= scaledHeight) ? scaledWidth : scaledHeight;
                int bmpX = source.getX() * shortScale;
                int bmpY = source.getY() * shortScale;
                Bitmap scaledBmp = Bitmap.createScaledBitmap(bmp, shortScale * 2, shortScale * 2, true);
                canvas.drawBitmap(scaledBmp, bmpX - (scaledBmp.getWidth() / 2), bmpY - (scaledBmp.getHeight() / 2), paint);


            }
        }

        // Draw targets
        if (targets != null) {
            bmp = bmpTarget;
            for (Target target : targets) {
                int scaledWidth = x / playfieldWidth;
                int scaledHeight = y / playfieldHeight;
                int shortScale = (scaledWidth <= scaledHeight) ? scaledWidth : scaledHeight;
                int bmpX = target.getX() * shortScale;
                int bmpY = target.getY() * shortScale;
                Bitmap scaledBmp = Bitmap.createScaledBitmap(bmp, shortScale * 2, shortScale * 2, true);
                canvas.drawBitmap(scaledBmp, bmpX - (scaledBmp.getWidth() / 2), bmpY - (scaledBmp.getHeight() / 2), paint);


            }
        }
        // Draw laser path
        for (int i = 0; i < playfieldWidth / 2; i++) {
            for (int j = 0; j < playfieldHeight / 2; j++) {
                Block block = blockGrid[i][j];

                int rayDir = 0;
                switch (block.getType()) {
                    case BLACKHOLE:
                        break;
                    case CRYSTAL:
                        break;
                    case GLASS:
                        break;
                    case MIRROR:
                        break;
                    case OPEN:
                        for (Ray ray : block.getRays()) {
                            int rayX = ray.getX();
                            switch (ray.getDirection()) {
                                case 45:
                                    rayDir = (rayX % 2 == 0) ? 0 : 180;
                                    break;
                                case 135:
                                    rayDir = (rayX % 2 == 0) ? 270 : 90;
                                    break;
                                case 225:
                                    rayDir = (rayX % 2 == 0) ? 180 : 0;
                                    break;
                                case 315:
                                    rayDir = (rayX % 2 == 0) ? 90 : 270;
                                    break;
                                default:
                                    Log.e(tag, "Invalid Direction in onDraw() Draw laser path. (OPEN)");
                                    break;
                            }

                            int scaledWidth = x / playfieldWidth;
                            int scaledHeight = y / playfieldHeight;
                            int shortScale = (scaledWidth <= scaledHeight) ? scaledWidth : scaledHeight;
                            int bmpX = block.getX() * shortScale;
                            int bmpY = block.getY() * shortScale;
                            Bitmap scaledBmp = Bitmap.createScaledBitmap(RotateBitmap(bmpLaser, rayDir), shortScale * 2, shortScale * 2, true);
                            canvas.drawBitmap(scaledBmp, bmpX - shortScale, bmpY - shortScale, paint);

                        }
                        break;
                    case WORMHOLE:
                        break;
                    default:
                        break;
                }

            }
        }
        super.onDraw(canvas);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //View myShadow = (View) findViewById(R.id.view);
        //ImageView imageView = (ImageView) findViewById(R.id.imageViewBmp);
        //imageView.setImageBitmap(bmpMirror);
        //ImageView imageView = new ImageView(getContext());
        //imageView.setImageBitmap(bmpMirror);
        //DragShadowBuilder dragShadowBuilder = new DragShadowBuilder(myShadow);
        //startDrag(null, new LazeDragShadowBuilder(imageView), null, 0);
        //View icon = findViewById(R.id.icon);
        startDrag(null, new LazeDragShadowBuilder(this), null, 0);
        Log.e(tag, "onTouchEvent");
        return true;
    }

    @Override
    public boolean onDragEvent(DragEvent event) {
        final int action = event.getAction();
        Log.e(tag, "onDragEvent:" + event.toString());
        switch (action) {
            case DragEvent.ACTION_DRAG_STARTED:
                return true;

        }
        return true;
    }

}

