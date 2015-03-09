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
    private int playfieldColumns;
    private int playfieldRows;
    private int viewWidth;
    private int viewHeight;
    private int playfieldWidthPadding;
    private int playfieldHeightPadding;
    private int blockViewQuadWidth;
    private int playfieldColumnViewWidth;
    private int playfieldRowViewHeight;
    private Block[][] blockGrid;
    private ArrayDeque<Ray> sources;
    private ArrayDeque<Target> targets;
    private Bitmap tempBmp;
    private Block lastBlockTouched;
    private Target lastTargetTouched;
    private Bitmap lastObjectouchedBmp;
    private Bitmap lastTargetTouchedBmp;
    private Bitmap bmpOpen = BitmapFactory.decodeResource(getResources(), R.drawable.open);
    //Bitmap bmpGlass = BitmapFactory.decodeResource(getResources(), R.drawable.glass);
    //Bitmap bmpCrystal= BitmapFactory.decodeResource(getResources(), R.drawable.crystal);
    //Bitmap bmpWormhole = BitmapFactory.decodeResource(getResources(), R.drawable.wormhole);
    //Bitmap bmpBlackhole = BitmapFactory.decodeResource(getResources(), R.drawable.blackhole);
    private Bitmap bmpSource = BitmapFactory.decodeResource(getResources(), R.drawable.source);
    private Bitmap bmpTarget = BitmapFactory.decodeResource(getResources(), R.drawable.target);
    private Bitmap bmpLaser = BitmapFactory.decodeResource(getResources(), R.drawable.laser);
    private Bitmap bmpDead = BitmapFactory.decodeResource(getResources(), R.drawable.dead);

    public LazeView(Context context) {
        super(context);
        blockDroppedObservable = new BlockDroppedObservable();
    }

    public LazeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        blockDroppedObservable = new BlockDroppedObservable();
    }

    public static Bitmap RotateBitmap(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    public int getBlockViewQuadWidth() {
        return blockViewQuadWidth;
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
            playfieldColumns = blockGrid.length * 2;
            playfieldRows = blockGrid[0].length * 2;
            playfieldColumnViewWidth = viewWidth / playfieldColumns;
            playfieldRowViewHeight = viewHeight / playfieldRows;
            blockViewQuadWidth = (playfieldColumnViewWidth <= playfieldRowViewHeight) ? playfieldColumnViewWidth : playfieldRowViewHeight;
            playfieldWidthPadding = (viewWidth % (blockViewQuadWidth * 2)) / 2;
            //playfieldWidthPadding = -50;
            playfieldHeightPadding = (viewHeight % (blockViewQuadWidth * 2)) / 2;
            //playfieldHeightPadding = 0;
            Matrix matrix = new Matrix();
            if (blockViewQuadWidth == 0) {
                Log.e(tag, "onDraw: blockViewQuadWidth=null");
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
                        case DEAD:
                            tempBmp = bmpDead;
                            break;
                        default:

                            break;
                    }
                    int blockX = block.getX() * blockViewQuadWidth;
                    int blockY = block.getY() * blockViewQuadWidth;
                    Bitmap scaledBmp = Bitmap.createScaledBitmap(tempBmp, blockViewQuadWidth * 2, blockViewQuadWidth * 2, true);
                    matrix.reset();
                    matrix.setTranslate(playfieldWidthPadding, playfieldHeightPadding);
                    matrix.postTranslate(blockX - blockViewQuadWidth, blockY - blockViewQuadWidth);
                    canvas.drawBitmap(scaledBmp, matrix, null);
                    //canvas.drawBitmap(scaledBmp, blockX - blockViewQuadWidth, blockY - blockViewQuadWidth, null);
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
                    int bmpX = source.getX() * blockViewQuadWidth;
                    int bmpY = source.getY() * blockViewQuadWidth;
                    Bitmap scaledBmp = Bitmap.createScaledBitmap(tempBmp, blockViewQuadWidth * 2, blockViewQuadWidth * 2, true);
                    matrix.reset();
                    matrix.setTranslate(playfieldWidthPadding, playfieldHeightPadding);
                    matrix.postTranslate(bmpX - blockViewQuadWidth, bmpY - blockViewQuadWidth);
                    canvas.drawBitmap(scaledBmp, matrix, null);
                    //canvas.drawBitmap(scaledBmp, bmpX - blockViewQuadWidth, bmpY - blockViewQuadWidth, null);
                }
            }

            // Draw targets
            if (targets != null) {
                tempBmp = bmpTarget;
                for (Target target : targets) {
                    target.setHit(false);
                    int bmpX = target.getX() * blockViewQuadWidth;
                    int bmpY = target.getY() * blockViewQuadWidth;
                    Bitmap scaledBmp = Bitmap.createScaledBitmap(tempBmp, blockViewQuadWidth * 2, blockViewQuadWidth * 2, true);
                    matrix.reset();
                    matrix.setTranslate(playfieldWidthPadding, playfieldHeightPadding);
                    matrix.postTranslate(bmpX - blockViewQuadWidth, bmpY - blockViewQuadWidth);
                    canvas.drawBitmap(scaledBmp, matrix, null);
                    //canvas.drawBitmap(scaledBmp, bmpX - blockViewQuadWidth, bmpY - blockViewQuadWidth, null);
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
                        case DEAD:
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
                                int bmpX = block.getX() * blockViewQuadWidth;
                                int bmpY = block.getY() * blockViewQuadWidth;
                                Bitmap scaledBmp = Bitmap.createScaledBitmap(RotateBitmap(bmpLaser, rayDir), blockViewQuadWidth * 2, blockViewQuadWidth * 2, true);
                                matrix.reset();
                                matrix.setTranslate(playfieldWidthPadding, playfieldHeightPadding);
                                matrix.postTranslate(bmpX - blockViewQuadWidth, bmpY - blockViewQuadWidth);
                                canvas.drawBitmap(scaledBmp, matrix, null);
                                //canvas.drawBitmap(scaledBmp, bmpX - blockViewQuadWidth, bmpY - blockViewQuadWidth, null);
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
            if (event.getX() < (playfieldColumns * blockViewQuadWidth) && event.getY() < (playfieldRows * blockViewQuadWidth)) {
                lastTargetTouched = null;
                lastTargetTouched = getLastTargetTouched(event.getX(), event.getY());
                if (lastTargetTouched != null) {
                    setLastObjectTouchedBmpAsTarget();
                    startDrag(null, new LazeDragShadowBuilder(this), null, 0);
                    return true;
                }

                lastBlockTouched = null;
                lastBlockTouched = getLastBlockTouched(event.getX(), event.getY());
                if (lastBlockTouched.isDraggable() && lastBlockTouched != null) {
                    setLastObjectTouchedBmpAsBlock(lastBlockTouched);
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

    public Bitmap getLastObjectouchedBmp() {
        return lastObjectouchedBmp;
    }

    private void setLastObjectTouchedBmpAsBlock(Block block) {
        lastObjectouchedBmp = null;
        switch (block.getType()) {
            case BLACKHOLE:
                break;
            case CRYSTAL:
                break;
            case GLASS:
                break;
            case MIRROR:
                lastObjectouchedBmp = bmpMirror;
                break;
            case OPEN:
                lastObjectouchedBmp = bmpOpen;
                break;
            case WORMHOLE:
                break;
            case DEAD:
                lastObjectouchedBmp = bmpDead;
                break;
            default:
                break;
        }
        if (lastObjectouchedBmp == null) {
            Log.e(tag, "setLastObjectTouchedBmpAsBlock: could not find lastBlockTouched Bmp");
        }
    }

    private void setLastObjectTouchedBmpAsTarget() {
        lastObjectouchedBmp = bmpTarget;
    }

    private Block getLastBlockTouched(float fingerX, float fingerY) {
        int blockXStart;
        int blockYStart;
        int blockXEnd;
        int blockYEnd;
        for (Block[] blockArray : blockGrid) {
            for (Block block : blockArray) {
                blockXStart = block.getX() * blockViewQuadWidth - blockViewQuadWidth + playfieldWidthPadding;
                blockYStart = block.getY() * blockViewQuadWidth - blockViewQuadWidth + playfieldHeightPadding;
                blockXEnd = blockXStart + blockViewQuadWidth * 2;
                blockYEnd = blockYStart + blockViewQuadWidth * 2;
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

    private Location getTargetLocationTouched(float fingerX, float fingerY) {
        int targetXStart;
        int targetYStart;
        int targetXEnd;
        int targetYEnd;
        int blockViewHalfQuadLength = blockViewQuadWidth / 2;
        for (int i = 0; i < blockGrid.length * 2 + 1; i++) {
            for (int j = 0; j < blockGrid[0].length * 2 + 1; j++) {
                targetXStart = i * blockViewQuadWidth - blockViewHalfQuadLength / 2 + playfieldWidthPadding;
                targetYStart = j * blockViewQuadWidth - blockViewHalfQuadLength / 2 + playfieldHeightPadding;
                targetXEnd = targetXStart + blockViewHalfQuadLength;
                targetYEnd = targetYStart + blockViewHalfQuadLength;
                if (locationOnBlockFace(i, j) && fingerX >= targetXStart && fingerX <= targetXEnd &&
                        fingerY >= targetYStart && fingerY <= targetYEnd) {
                    Log.d(tag, "getTargetLocationTouched: Touched a Target region.");
                    Log.d(tag, "getTargetLocationTouched: fingerX:" + fingerX);
                    Log.d(tag, "getTargetLocationTouched: fingerY:" + fingerY);
                    return new Location(i, j);
                }

            }
        }
        Log.e(tag, "getTargetLocationTouched: Finger not within a target region");
        Log.e(tag, "getLastTargetTouched: fingerX:" + fingerX);
        Log.e(tag, "getLastTargetTouched: fingerY:" + fingerY);
        Log.e(tag, "getLastTargetTouched: viewWidth:" + viewWidth);
        Log.e(tag, "getLastTargetTouched: viewHeight:" + viewHeight);
        return null;
    }

    // Check whether the given location is on a playfield face.
        private boolean locationOnBlockFace(int x, int y) {
        return (x + y) % 2 != 0 && x < blockGrid.length * 2 + 1 && y < blockGrid[0].length * 2 + 1;
    }

    private Target getLastTargetTouched(float fingerX, float fingerY) {
        Location location = getTargetLocationTouched(fingerX, fingerY);
        if (location != null) {
            for (Target target : targets) {
                if (location.getX() == target.getX() && location.getY() == target.getY())
                    return target;
            }
        }
        Log.e(tag, "getLastTargetTouched: Couldn't find touched Target");
        Log.e(tag, "getLastTargetTouched: fingerX:" + fingerX);
        Log.e(tag, "getLastTargetTouched: fingerY:" + fingerY);
        Log.e(tag, "getLastTargetTouched: viewWidth:" + viewWidth);
        Log.e(tag, "getLastTargetTouched: viewHeight:" + viewHeight);
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
                float x = event.getX();
                float y = event.getY();
                Log.d(tag, "x= " + x);
                Log.d(tag, "y= " + y);

                Target startDragTarget = lastTargetTouched;
                Location endDragTargetLocation = getTargetLocationTouched(x, y);
                if (startDragTarget != null) {
                    if (endDragTargetLocation != null) {
                        startDragTarget.setLocation(endDragTargetLocation.getX(), endDragTargetLocation.getY());
                        blockDroppedObservable.blockDropped();
                        return true;
                    } else return false;
                }

                Block startDragBlock = lastBlockTouched;
                Block endDragBlock = getLastBlockTouched(x, y);
                if (endDragBlock != null && endDragBlock.canBeDroppedOn()) {
                    swapBlockGridBlocks(startDragBlock, endDragBlock);
                    blockDroppedObservable.blockDropped();
                    return true;
                }
        }
        return false;
    }

    public void swapBlockGridTargets(Target targetA, Target targetB) {
        Location tempLocation = new Location(targetA.getX(), targetA.getY());
        targetA.setLocation(targetB.getX(), targetB.getY());
        targetB.setLocation(tempLocation.getX(), tempLocation.getY());
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

