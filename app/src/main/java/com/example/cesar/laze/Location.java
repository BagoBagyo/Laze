package com.example.cesar.laze;

/**
 * Created by Cesar on 1/26/2015.
 * Location class
 * Defines a location object.
 */
public class Location {
    private int x;
    private int y;

    public int getX() {
        return x;
    }

    /**
     * @param x X position on playfield grid.
     * @param y Y position on playfield grid.
     */
    public Location(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * @param x X position on playfield grid.
     * @param y Y position on playfield grid.
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * @return int Y position on playfield grid.
     */
    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setLocation(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
