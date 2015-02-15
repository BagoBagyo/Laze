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
import android.widget.ImageView;

import java.util.ArrayDeque;

/**
 * Created by Cesar on 2/5/2015.
 */
public class LazeView extends View {
    final static String tag = "LAZE";
    static ImageView myShadow;
    Bitmap bmpMirror = BitmapFactory.decodeResource(getResources(), R.drawable.mirror);
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
    private Bitmap bmpOpen = BitmapFactory.decodeResource(getResources(), R.drawable.open);
    private Bitmap tempBmp = null;
	private Block lastBlockTouched;
    private Bitmap lastTouchedBmp = null;
    //Bitmap bmpGlass = BitmapFactory.decodeResource(getResources(), R.drawable.glass);
    //Bitmap bmpCrystal= BitmapFactory.decodeResource(getResources(), R.drawable.crystal);
    //Bitmap bmpWormhole = BitmapFactory.decodeResource(getResources(), R.drawable.wormhole);
    //Bitmap bmpBlackhole = BitmapFactory.decodeResource(getResources(), R.drawable.blackhole);
    private Bitmap bmpSource = BitmapFactory.decodeResource(getResources(), R.drawable.source);
    private Bitmap bmpTarget = BitmapFactory.decodeResource(getResources(), R.drawable.target);
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

    public void initLazeView(Block[][] blockGrid, ArrayDeque<Ray> sources, ArrayDeque<Target> targets) {
        this.blockGrid = blockGrid;
        this.sources = sources;
        this.targets = targets;
    }

    @Override
    protected void onDraw(Canvas canvas) {
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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d(tag, "onTouchEvent");

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (event.getX() < (playfieldWidth * blockQuadLength) && event.getY() < (playfieldHeight * blockQuadLength)) {
                setLastTouchedBmp(event.getX(), event.getY());
                if (lastTouchedBmp != null) {
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

    public Bitmap getLastTouchedBmp() {
        return lastTouchedBmp;
    }

    private void setLastTouchedBmp(float fingerX, float fingerY) {
        int blockXStart = 0;
        int blockYStart = 0;
        int blockXEnd = 0;
        int blockYEnd = 0;
        for (Block[] blockArray : blockGrid) {
            for (Block block : blockArray) {
                blockXStart = block.getX() * blockQuadLength - blockQuadLength;
                blockYStart = block.getY() * blockQuadLength - blockQuadLength;
                blockXEnd = blockXStart + blockQuadLength * 2;
                blockYEnd = blockYStart + blockQuadLength * 2;

                lastTouchedBmp = null;
				lastBlockTouched = null;
                if ((fingerX >= blockXStart) && (fingerX <= blockXEnd) &&
                        (fingerY >= blockYStart) && (fingerY <= blockYEnd)) {
                    lastBlockTouched = block;
					switch (block.getType()) {
                        case BLACKHOLE:
                            break;
                        case CRYSTAL:
                            break;
                        case GLASS:
                            break;
                        case MIRROR:
                            lastTouchedBmp = bmpMirror;
                            break;
                        case OPEN:
                            lastTouchedBmp = bmpOpen;
                            break;
                        case WORMHOLE:
                            break;
                        default:
                            break;
                    }
                    if (lastTouchedBmp == null) {
                        Log.e(tag, "setLastTouchBmp: lastTouchBmp == null");
                    }
                    return;
                }
            }
        }
        Log.e(tag, "setLastTouchBmp: Couldn't find touched block");
        Log.e(tag, "onTouchEvent: fingerX:" + fingerX);
        Log.e(tag, "onTouchEvent: fingerY:" + fingerY);
        Log.e(tag, "onTouchEvent: viewWidth:" + viewWidth);
        Log.e(tag, "onTouchEvent: viewHeight:" + viewHeight);
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
				Block block = blockGrid[(int) event.getX() / blockQuadLength][(int) event.getY() / blockQuadLength];
				swapBlocks(lastBlockTouched, block);

        }
        return true;
    }
	
	public void swapBlocks(Block blockA, Block blockB) {
		Block tempBlock = blockA;
		blockA = blockB;
		blockB = tempBlock;
	}
}

