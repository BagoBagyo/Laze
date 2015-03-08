package com.example.cesar.laze;

/**
 * Created by Cesar on 1/28/2015.
 */
public class Target extends Location {
    private boolean hit;

    public Target(int x, int y, boolean hit) {
        super(x, y);
        this.hit = hit;
    }

    public Target(Target toClone) {
        super(toClone);
        hit = toClone.isHit();
    }

    public boolean isHit() {
        return hit;
    }

    public void setHit(boolean hit) {
        this.hit = hit;
    }

    @Override
    public String toString() {
        return "Target{" +
                "x=" + this.getX() +
                "y=" + this.getY() +
                "hit=" + hit +
                '}';
    }
}
