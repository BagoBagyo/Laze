package com.example.cesar.laze;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayDeque;
import java.util.Observable;

/**
 * Created by Cesar on 2/5/2015.
 * LazeView custom view class
 */
public class LazeView extends View {
    final static String tag = "LAZE";
    Bitmap bmpMirror = BitmapFactory.decodeResource(getResources(), R.drawable.mirror);
    BlockDroppedObservable blockDroppedObservable;
    private int playfieldWidth;
    private int playfieldHeight;
    private int viewWidth;
    private int viewHeight;
    private int blockQuadLength;
    private int columnWidth;
    private int rowHeight;
    private Block[][] blockGrid;
    private ArrayDeque<Ray> sources;
    private ArrayDeque<Target> targets;
    private Bitmap tempBmp;
    private Block lastBlockTouched;
    private Bitmap lastBlockTouchedBmp;
    private Bitmap bmpOpen = BitmapFactory.decodeResource(getResources(), R.drawable.open);
    //Bitmap bmpGlass = BitmapFactory.decodeResource(getResources(), R.drawable.glass);
    //Bitmap bmpCrystal= BitmapFactory.decodeResource(getResources(), R.drawable.crystal);
    //Bitmap bmpWormhole = BitmapFactory.decodeResource(getResources(), R.drawable.wormhole);
    //Bitmap bmpBlackhole = BitmapFactory.decodeResource(getResources(), R.drawable.blackhole);
    private Bitmap bmpSource = BitmapFactory.decodeResource(getResources(), R.drawable.source);
    private Bitmap bmpTarget = BitmapFactory.decodeResource(getResources(), R.drawable.target);
    private Bitmap bmpLaser = BitmapFactory.decodeResource(getResources(), R.drawable.laser);

    public LazeView(Context context) {
        super(context);
        blockDroppedObservable = new BlockDroppedObservable();
    }

    public LazeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        blockDroppedObservable = new BlockDroppedObservable();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // maximum width we should use
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
       if (width <= height) {
           setMeasuredDimension(width, width);
       } else {
           setMeasuredDimension(height, height);
       }
    }

    public static Bitmap RotateBitmap(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    public void initLazeView(Block[][] blockGrid, ArrayDeque<Ray> sources, ArrayDeque<Target> targets) {
        this.blockGrid = blockGrid;
        this.sources = sources;
        this.targets = targets;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (!isInEditMode()) {
            Log.d(tag, "entering onDraw");
            viewWidth = getWidth();
            viewHeight = getHeight();
            playfieldWidth = blockGrid.length * 2;
            playfieldHeight = blockGrid[0].length * 2;
            columnWidth = viewWidth / playfieldWidth;
            rowHeight = viewHeight / playfieldHeight;
            blockQuadLength = (columnWidth <= rowHeight) ? columnWidth : rowHeight;
            if (blockQuadLength == 0) {
                Log.e(tag, "onDraw: blockQuadLength=null");
            }
            // Draw blocks
            for (Block[] blockArray : blockGrid) {
                for (Block block : blockArray) {
                    switch (block.getType()) {
                        case OPEN:
                            tempBmp = bmpOpen;
                            break;
                        case MIRROR:
                            tempBmp = bmpMirror;
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
                    int blockX = block.getX() * blockQuadLength;
                    int blockY = block.getY() * blockQuadLength;
                    Bitmap scaledBmp = Bitmap.createScaledBitmap(tempBmp, blockQuadLength * 2, blockQuadLength * 2, true);
                    canvas.drawBitmap(scaledBmp, blockX - blockQuadLength, blockY - blockQuadLength, null);
                }
            }
            // Draw sources
            if (sources != null) {
                for (Ray source : sources) {
                    switch (source.getType()) {
                        case RED:
                            tempBmp = bmpSource;
                            break;
                        case GREEN:
                            tempBmp = bmpSource;
                            break;
                        default:
                            break;
                    }
                    int bmpX = source.getX() * blockQuadLength;
                    int bmpY = source.getY() * blockQuadLength;
                    Bitmap scaledBmp = Bitmap.createScaledBitmap(tempBmp, blockQuadLength * 2, blockQuadLength * 2, true);
                    canvas.drawBitmap(scaledBmp, bmpX - blockQuadLength, bmpY - blockQuadLength, null);
                }
            }

            // Draw targets
            if (targets != null) {
                tempBmp = bmpTarget;
                for (Target target : targets) {
                    target.setHit(false);
                    int bmpX = target.getX() * blockQuadLength;
                    int bmpY = target.getY() * blockQuadLength;
                    Bitmap scaledBmp = Bitmap.createScaledBitmap(tempBmp, blockQuadLength * 2, blockQuadLength * 2, true);
                    canvas.drawBitmap(scaledBmp, bmpX - blockQuadLength, bmpY - blockQuadLength, null);
                }
            }

            // Draw laser path
            for (Block[] blockArray : blockGrid) {
                for (Block block : blockArray) {
                    int rayDir = 0;
                    switch (block.getType()) {
                        case BLACKHOLE:
                            break;
                        case CRYSTAL:
                            break;
                        case GLASS:
                            break;
                        case MIRROR:
                            // Mirrors reflect lasers, so there should not be any rays attached to glass blocks.
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
                                int bmpX = block.getX() * blockQuadLength;
                                int bmpY = block.getY() * blockQuadLength;
                                Bitmap scaledBmp = Bitmap.createScaledBitmap(RotateBitmap(bmpLaser, rayDir), blockQuadLength * 2, blockQuadLength * 2, true);
                                canvas.drawBitmap(scaledBmp, bmpX - blockQuadLength, bmpY - blockQuadLength, null);
                            }
                            break;
                        case WORMHOLE:
                            break;
                        default:
                            break;
                    }
                }
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d(tag, "onTouchEvent");

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (event.getX() < (playfieldWidth * blockQuadLength) && event.getY() < (playfieldHeight * blockQuadLength)) {
                lastBlockTouched = null;
                lastBlockTouched = getLastBlockTouched(event.getX(), event.getY());
                if (lastBlockTouched != null) {
                    setLastBlockTouchedBmp(lastBlockTouched);
                    startDrag(null, new LazeDragShadowBuilder(this), null, 0);
                    return true;
                } else {
                    Log.e(tag, "onTouchEvent: could not find touched block");
                    return false;
                }
            }
        }
        return false;
    }

    public Bitmap getLastBlockTouchedBmp() {
        return lastBlockTouchedBmp;
    }

    private void setLastBlockTouchedBmp(Block block) {
        lastBlockTouchedBmp = null;
        switch (block.getType()) {
            case BLACKHOLE:
                break;
            case CRYSTAL:
                break;
            case GLASS:
                break;
            case MIRROR:
                lastBlockTouchedBmp = bmpMirror;
                break;
            case OPEN:
                lastBlockTouchedBmp = bmpOpen;
                break;
            case WORMHOLE:
                break;
            default:
                break;
        }
        if (lastBlockTouchedBmp == null) {
            Log.e(tag, "setLastBlockTouchedBmp: could not find lastBlockTouched Bmp");
        }
    }

    private Block getLastBlockTouched(float fingerX, float fingerY) {
        int blockXStart;
        int blockYStart;
        int blockXEnd;
        int blockYEnd;
        for (Block[] blockArray : blockGrid) {
            for (Block block : blockArray) {
                blockXStart = block.getX() * blockQuadLength - blockQuadLength;
                blockYStart = block.getY() * blockQuadLength - blockQuadLength;
                blockXEnd = blockXStart + blockQuadLength * 2;
                blockYEnd = blockYStart + blockQuadLength * 2;
                if ((fingerX >= blockXStart) && (fingerX <= blockXEnd) &&
                        (fingerY >= blockYStart) && (fingerY <= blockYEnd)) {
                    return block;
                }
            }
        }
        Log.e(tag, "getLastBlockTouched: Couldn't find touched block");
        Log.e(tag, "getLastBlockTouched: fingerX:" + fingerX);
        Log.e(tag, "getLastBlockTouched: fingerY:" + fingerY);
        Log.e(tag, "getLastBlockTouched: viewWidth:" + viewWidth);
        Log.e(tag, "getLastBlockTouched: viewHeight:" + viewHeight);
        return null;
    }

    @Override
    public boolean onDragEvent(DragEvent event) {
        final int action = event.getAction();
        //Log.d(tag, "onDragEvent:" + event.toString());
        switch (action) {
            case DragEvent.ACTION_DRAG_STARTED:
                return true;
            case DragEvent.ACTION_DROP:
                Block startDragBlock = lastBlockTouched;
                float x = event.getX();
                float y = event.getY();
                Log.d(tag, "x= " + x);
                Log.d(tag, "y= " + y);
                Block endDragBlock = getLastBlockTouched(x, y);
                if (endDragBlock != null) {
                    swapBlockGridBlocks(startDragBlock, endDragBlock);
                    blockDroppedObservable.blockDropped();
                    return true;
                }
        }
        return false;
    }

    public void swapBlockGridBlocks(Block blockA, Block blockB) {
        blockGrid[blockA.getX() / 2][blockA.getY() / 2] = blockB;
        blockGrid[blockB.getX() / 2][blockB.getY() / 2] = blockA;
        Location tempLocation = new Location(blockA.getX(), blockA.getY());
        blockA.setLocation(blockB.getX(), blockB.getY());
        blockB.setLocation(tempLocation.getX(), tempLocation.getY());
    }

    public class BlockDroppedObservable extends Observable {
        public void blockDropped() {
            setChanged();
            notifyObservers();
            invalidate();
        }
    }
}

