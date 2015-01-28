package com.example.cesar.laze;

/**
 * Created by Cesar on 1/26/2015.
 * Location class
 * Defines a location object
 *
 * x : x position on playfield grid.
 * y : y position on playfield grid.
 */
public class Location {
    private int x;
    private int y;

    public int getX() {
        return x;
    }

    public Location(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setX(int x) {
        this.x = x;
    }

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
