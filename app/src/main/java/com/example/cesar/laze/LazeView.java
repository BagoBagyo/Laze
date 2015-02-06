package com.example.cesar.laze;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayDeque;

/**
 * Created by Cesar on 2/5/2015.
 */
public class LazeView extends View {

    private int playfieldWidth;
    private int playfieldHeight;

    private Block[][] blockGrid;
    private ArrayDeque sources;
    private ArrayDeque targets;

    private Bitmap bmpOpen = BitmapFactory.decodeResource(getResources(), R.drawable.open);
    //Bitmap bmpGlass = BitmapFactory.decodeResource(getResources(), R.drawable.glass);
    //Bitmap bmpCrystal= BitmapFactory.decodeResource(getResources(), R.drawable.crystal);
    //Bitmap bmpWormhole = BitmapFactory.decodeResource(getResources(), R.drawable.wormhole);
    //Bitmap bmpBlackhole = BitmapFactory.decodeResource(getResources(), R.drawable.blackhole);
    Bitmap bmp = bmpOpen;
    private Bitmap bmpMirror = BitmapFactory.decodeResource(getResources(), R.drawable.mirror);


    public LazeView(Context context) {
        super(context);
    }

    public LazeView(Context context, AttributeSet attrs) {
        super(context, attrs);
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
                int bmpX = block.getX() * shortScale - shortScale;
                int bmpY = block.getY() * shortScale - shortScale;
                Bitmap scaledBmp = Bitmap.createScaledBitmap(bmp, shortScale * 2, shortScale * 2, true);
                canvas.drawBitmap(scaledBmp, bmpX, bmpY, paint);
            }
        }


        super.onDraw(canvas);
    }


}
