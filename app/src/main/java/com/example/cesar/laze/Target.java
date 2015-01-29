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

    public boolean isHit() {
        return hit;
    }

    public void setHit(boolean hit) {
        this.hit = hit;
    }
}
