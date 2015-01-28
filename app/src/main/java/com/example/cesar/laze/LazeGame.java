package com.example.cesar.laze;

import android.util.Log;

import java.util.ArrayDeque;

import static com.example.cesar.laze.Block.Type.*;

/**
 * Created by Cesar on 1/26/2015.
 * LazeGame Model class
 */
public class LazeGame {
    final static String tag = "LAZE: LazeGame";

    private Block[][] blockGrid;
    private ArrayDeque<Ray> sources;
    private ArrayDeque<Location> targets;
    private ArrayDeque<Ray> newRays;
    private int playfieldWidth;
    private int playfieldHeight;

    public LazeGame(int blockGridWidth, int blockGridHeight, ArrayDeque<Ray> sources, ArrayDeque<Location> targets) {
        playfieldWidth = blockGridWidth * 2;
        playfieldHeight = blockGridHeight * 2;

        blockGrid = new Block[blockGridWidth][blockGridHeight];

        for (int i = 0; i < blockGridWidth; i++) {
            for (int j = 0; j < blockGridHeight; j++) {
                //ArrayDeque<Ray> nullRay = new ArrayDeque();
                //nullRay.add(new Ray(0, 0, Ray.Type.RED, 0));

                blockGrid[i][j] = new Block(i*2+1, j*2+1, OPEN, new ArrayDeque<Ray>());

            }
        }
        this.sources = sources;
        this.targets = targets;
    }

    public Block[][] getBlockGrid() {
        return blockGrid;
    }

    public void setBlockGrid(Block[][] blockGrid) {
        this.blockGrid = blockGrid;
    }

    public ArrayDeque<Ray> getSources() {
        return sources;
    }

    public void setSources(ArrayDeque<Ray> sources) {
        this.sources = sources;
    }

    public ArrayDeque<Location> getTargets() {
        return targets;
    }

    public void setTargets(ArrayDeque<Location> targets) {
        this.targets = targets;
    }

    public void update() {
        ArrayDeque<Ray> currentRays = sources;
        while (currentRays.isEmpty() == false) {
            Ray ray = currentRays.pop();
            if (rayInPlay(ray) || !rayHitTarget(ray)) {
                newRays = propigateRay(ray);
                for (Ray newRay : newRays) {
                    currentRays.push(newRay);
                }

            } else {
                // Ray is either about to go out of bounds or has hit a target
            }
        }
    }

    private boolean rayInPlay(Ray ray) {
        if ((ray.getX() == 0 && (ray.getDirection() == 225 || ray.getDirection() == 315)) ||
                (ray.getY() == 0 && (ray.getDirection() == 315 || ray.getDirection() == 45)) ||
                (ray.getX() == playfieldWidth && (ray.getDirection() == 45 || ray.getDirection() == 135)) ||
                (ray.getY() == playfieldHeight && (ray.getDirection() == 135 || ray.getDirection() == 225)) ||
                (ray.getX() == 0 && ray.getY() == 0 && ray.getDirection() != 135) ||
                (ray.getX() == 0 && ray.getY() == playfieldHeight && ray.getDirection() != 45) ||
                (ray.getX() == playfieldWidth && ray.getY() == 0 && ray.getDirection() != 225) ||
                (ray.getX() == playfieldWidth && ray.getY() == playfieldHeight && ray.getDirection() != 315)) {
            return false;
        } else {
            return true;
        }
    }

    private boolean rayHitTarget(Ray ray) {
        for (Location targetLocation : targets) {
            if ((ray.getX() == targetLocation.getX()) && (ray.getY() == targetLocation.getY())) {
                return true;
            }
        }
        return false;
    }

    private ArrayDeque propigateRay(Ray ray) {
        int rayX = ray.getX();
        int rayY = ray.getY();
        int blockX = -1;
        int blockY = -1;
        Block block;
        ArrayDeque newRays = new ArrayDeque();

        switch (ray.getDirection()) {
            case 45:
                if (rayX % 2 == 0) {
                    blockX = ++rayX;
                    blockY= rayY;
                } else {
                    blockX = rayX;
                    blockY = --rayY;
                }
                break;
            case 135:
                if (rayX % 2 == 0) {
                    blockX = ++rayX;
                    blockY = rayY;
                } else {
                    blockX = rayX;
                    blockY = ++rayY;
                }
                break;
            case 225:
                if (rayX % 2 == 0) {
                    blockX = --rayX;
                    blockY = rayY;
                } else {
                    blockX = rayX;
                    blockY = ++rayY;
                }
                break;
            case 315:
                if (rayX % 2 == 0) {
                    blockX = --rayX;
                    blockY= rayY;
                } else {
                    blockX = rayX;
                    blockY = --rayY;
                }
                break;
            default:
                Log.e(tag, "Invalid Direction in propigateRay()");
                break;
        }
        blockX = blockX/2;
        blockY = blockY/2;

        block = blockGrid[blockX][blockY];
        block.getRays().push(ray);

        // generate exit Rays
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
                switch (ray.getDirection()) {
                    case 45:
                        ray.setY(ray.getY()-1);
                        break;
                    case 135:
                        ray.setY(ray.getY()+1);
                        break;
                    case 225:
                        ray.setX(ray.getX()-1);
                        ray.setY(ray.getY()+1);
                        break;
                    case 315:
                        ray.setX(ray.getX()+1);
                        break;
                    default:
                        Log.e(tag,"Invalid direction in propigateRay().");
                        break;
                }
                newRays.push(ray);
                break;
            case WORMHOLE:
                break;
            default:
                break;
        }
        return newRays;
    }

}
