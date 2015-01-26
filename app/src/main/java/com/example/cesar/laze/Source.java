package com.example.cesar.laze;

/**
 * Created by Cesar on 1/24/2015.
 * Basic Ray object
 */
public class Source {
    enum Type {
        RED, GREEN
    }

    private Type type;
    private int x;
    private int y;
    private int direction;

    public Source(Type type, int x, int y, int direction) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.direction = direction;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public int getX() {
        return x;
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

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }
}

