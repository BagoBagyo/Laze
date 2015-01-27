package com.example.cesar.laze;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Cesar on 1/26/2015.
 * LazeGame Model class
 */
public class LazeGame {
    final static String tag = "LAZE: LazeGame";

    private Block[][] blockGrid;
    private ArrayList<Ray> sources;
    private ArrayList<Location> targets;
    private int playfieldWidth;
    private int playfieldHeight;

    public LazeGame(int blockGridWidth, int blockGridHeight, ArrayList<Ray> sources, ArrayList<Location> targets) {
        playfieldWidth = blockGridWidth * 2;
        playfieldHeight = blockGridHeight * 2;

        blockGrid = new Block[blockGridWidth][blockGridHeight];

        for (int i = 0; i < blockGridWidth; i++) {
            for (int j = 0; j < blockGridHeight; j++) {
                ArrayList<Ray> nullRay = new ArrayList<>();
                nullRay.add(new Ray(0, 0, Ray.Type.RED, 0));

                blockGrid[i][j] = new Block(i*2+1, j*2+1, Block.Type.OPEN, nullRay);

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

    public ArrayList<Ray> getSources() {
        return sources;
    }

    public void setSources(ArrayList<Ray> sources) {
        this.sources = sources;
    }

    public ArrayList<Location> getTargets() {
        return targets;
    }

    public void setTargets(ArrayList<Location> targets) {
        this.targets = targets;
    }

    public void update() {
        ArrayList<Ray> currentRays = sources;
        for (Ray ray : currentRays) {
            if (rayInPlay(ray) || !rayHitTarget(ray)) {
                addRayToBlock(ray);
                currentRays.add(propigateRay(ray));

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

    private void addRayToBlock(Ray ray) {
        int rayX = ray.getX();
        int rayY = ray.getY();

        switch (ray.getDirection()) {
            case 45:
                if (rayX % 2 == 0) blockGrid[++rayX/2][rayY/2].getRays().add(ray);
                else if (rayY % 2 == 0) blockGrid[rayX/2][--rayY/2].getRays().add(ray);
                break;
            case 135:
                if (rayX % 2 == 0) blockGrid[++rayX/2][rayY/2].getRays().add(ray);
                else if (rayY % 2 == 0) blockGrid[rayX/2][++rayY/2].getRays().add(ray);
                break;
            case 225:
                if (rayX % 2 == 0) blockGrid[--rayX/2][rayY/2].getRays().add(ray);
                else if (rayY % 2 == 0) blockGrid[rayX/2][++rayY/2].getRays().add(ray);
                break;
            case 315:
                if (rayX % 2 == 0) blockGrid[--rayX/2][rayY/2].getRays().add(ray);
                else if (rayY % 2 == 0) blockGrid[rayX/2][--rayY/2].getRays().add(ray);
                break;
            default:
                Log.e(tag, "Invalid Direction in addRayToBlock()");
        }
    }

    private Ray propigateRay(Ray ray) {
        //Ray newRay;


        return ray;
        //return newRay;
    }
}
