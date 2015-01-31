package com.example.cesar.laze;

import android.util.Log;

import java.util.ArrayDeque;

/**
 * Created by Cesar on 1/26/2015.
 * LazeGame Model class
 */
public class LazeGame {
    final static String tag = "LAZE";

    private Block[][] blockGrid;
    private ArrayDeque<Ray> sources;
    private ArrayDeque<Target> targets;
    private ArrayDeque<Ray> outOfPlay = new ArrayDeque<>();
    private ArrayDeque<Ray> newRays;
    private int playfieldWidth;
    private int playfieldHeight;

    public LazeGame(int blockGridWidth, int blockGridHeight, ArrayDeque<Ray> sources, ArrayDeque<Target> targets) {
        playfieldWidth = blockGridWidth * 2;
        playfieldHeight = blockGridHeight * 2;

        blockGrid = new Block[blockGridWidth][blockGridHeight];

        for (int i = 0; i < blockGridWidth; i++) {
            for (int j = 0; j < blockGridHeight; j++) {
                blockGrid[i][j] = new Block(i * 2 + 1, j * 2 + 1, Block.Type.OPEN, new ArrayDeque<Ray>());
            }
        }
        blockGrid[3][3] = new Block(7, 7, Block.Type.MIRROR, new ArrayDeque<Ray>());

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

    public ArrayDeque<Target> getTargets() {
        return targets;
    }

    public void setTargets(ArrayDeque<Target> targets) {
        this.targets = targets;
    }

    public void update() {
        ArrayDeque<Ray> currentRays = new ArrayDeque<>();
        for (Ray ray : sources) currentRays.push(new Ray(ray));
        while (!currentRays.isEmpty()) {
            Ray ray = currentRays.pop();
            checkRayTargetHit(ray);
            if (rayInPlay(ray)) {
                newRays = propagateRay(ray);
                for (Ray newRay : newRays) {
                    //currentRays.push(new Ray(newRay));
                    currentRays.push(newRay);
                }
            } else {
                // Ray is either about to go out of bounds or has hit a target
            }
        }
        Log.d(tag, "propagateRay(): OutOfPlay: " + outOfPlay);
    }

    public boolean allTargetsHit() {
        boolean allTargetsHit = true;

        for (Target target : targets) {
            if (!target.isHit()) {
                allTargetsHit = false;
            }
        }
        return allTargetsHit;
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
            outOfPlay.push(new Ray(ray));
            //Log.d(tag, "rayInPlay(): ray out of play" + outOfPlay.toString());
            return false;
        } else {
            return true;
        }
    }

    private boolean checkRayTargetHit(Ray ray) {
        for (Target target : targets) {
            //Log.d(tag, "checkRayTargetHit(): in ray: " + ray.toString());
            //Log.d(tag, "checkRayTargetHit(): target: " + target.toString());
            if ((ray.getX() == target.getX()) && (ray.getY() == target.getY())) {
                target.setHit(true);
                //Log.d(tag, "checkRayTargetHit(): target hit: " + target.toString());
                return true;
            }
        }
        //Log.d(tag, "checkRayTargetHit(): no targets hit.");
        return false;
    }

    private ArrayDeque propagateRay(Ray ray) {
        Ray rayCopy = new Ray(ray);
        int rayX = rayCopy.getX();
        int rayY = rayCopy.getY();
        int blockX = -1;
        int blockY = -1;
        Block block;
        ArrayDeque newRays = new ArrayDeque();

        Log.d(tag, "propagateRay(): input ray: " + ray.toString());

        switch (rayCopy.getDirection()) {
            case 45:
                if (rayX % 2 == 0) {
                    blockX = rayX + 1;
                    blockY = rayY;
                } else {
                    blockX = rayX;
                    blockY = rayY - 1;
                }
                break;
            case 135:
                if (rayX % 2 == 0) {
                    blockX = rayX + 1;
                    blockY = rayY;
                } else {
                    blockX = rayX;
                    blockY = rayY + 1;
                }
                break;
            case 225:
                if (rayX % 2 == 0) {
                    blockX = rayX - 1;
                    blockY = rayY;
                } else {
                    blockX = rayX;
                    blockY = rayY + 1;
                }
                break;
            case 315:
                if (rayX % 2 == 0) {
                    blockX = rayX - 1;
                    blockY = rayY;
                } else {
                    blockX = rayX;
                    blockY = rayY - 1;
                }
                break;
            default:
                Log.e(tag, "Invalid Direction in propagate Ray()1");
                break;
        }
        blockX = blockX / 2;
        blockY = blockY / 2;

        block = blockGrid[blockX][blockY];
        block.getRays().push(new Ray(rayCopy));

        Log.d(tag, "propagateRay(): ray assigned to block: " + block.toString());

        // generate exit Rays
        switch (block.getType()) {
            case BLACKHOLE:
                break;
            case CRYSTAL:
                break;
            case GLASS:
                break;
            case MIRROR:
                switch (rayCopy.getDirection()) {
                    case 45:
                        if (rayX % 2 == 0) {
                            rayCopy.setDirection(315);
                        } else {
                            rayCopy.setDirection(135);
                        }
                        newRays.push(rayCopy);
                        break;
                    case 135:
                        if (rayX % 2 == 0) {
                            rayCopy.setDirection(225);
                        } else {
                            rayCopy.setDirection(45);
                        }
                        newRays.push(rayCopy);
                        break;
                    case 225:
                        if (rayX % 2 == 0) {
                            rayCopy.setDirection(135);
                        } else {
                            rayCopy.setDirection(315);
                        }
                        newRays.push(rayCopy);
                        break;
                    case 315:
                        if (rayX % 2 == 0) {
                            rayCopy.setDirection(45);
                        } else {
                            rayCopy.setDirection(225);
                        }
                        newRays.push(rayCopy);
                        break;
                    default:
                        Log.e(tag, "Invalid Direction in propagateRay()2");
                        break;
                }
                break;
            case OPEN:
                switch (rayCopy.getDirection()) {
                    case 45:
                        rayCopy.setX(rayCopy.getX() + 1);
                        rayCopy.setY(rayCopy.getY() - 1);
                        newRays.push(rayCopy);
                        break;
                    case 135:
                        rayCopy.setX(rayCopy.getX() + 1);
                        rayCopy.setY(rayCopy.getY() + 1);
                        newRays.push(rayCopy);
                        break;
                    case 225:
                        rayCopy.setX(rayCopy.getX() - 1);
                        rayCopy.setY(rayCopy.getY() + 1);
                        newRays.push(rayCopy);
                        break;
                    case 315:
                        rayCopy.setX(rayCopy.getX() - 1);
                        rayCopy.setY(rayCopy.getY() - 1);
                        newRays.push(rayCopy);
                        break;
                    default:
                        Log.e(tag, "Invalid direction in propagateRay().3");
                        break;
                }
                break;
            case WORMHOLE:
                break;
            default:
                break;
        }
        Log.d(tag, "propagateRay(): newRays: " + newRays.toString());
        return newRays;
    }

    public String toString() {
        StringBuilder result = new StringBuilder();
        String NEW_LINE = System.getProperty("line.separator");

        result.append(this.getClass().getName() + " Object {" + NEW_LINE);

        for (int i = 0; i < playfieldWidth / 2; i++) {
            for (int j = 0; j < playfieldHeight / 2; j++) {
                result.append(" block[" + i + "][" + j + "]: " + blockGrid[i][j].toString() + NEW_LINE);
            }
        }

        return result.toString();
    }
}
