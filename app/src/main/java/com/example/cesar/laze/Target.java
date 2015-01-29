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

/*    @Override
    public String toString() {
        super.toString();
        StringBuilder result = new StringBuilder();
        String NEW_LINE = System.getProperty("line.separator");

        result.append(this.getClass().getName() + " Object {" + NEW_LINE);
        result.append(" hit: " + this.hit + NEW_LINE);
        return result.toString();
    }*/

    @Override
    public String toString() {
        return "Target{" +
                "x=" + this.getX()+
                "y=" + this.getY() +
                "hit=" + hit +
                '}';
    }
}
