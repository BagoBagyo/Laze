package com.example.cesar.laze;

/**
 * Created by Cesar on 1/24/2015.
 * Ray class
 * Defines a ray object (block incident ray)
 *
 * location : location on playfield grid.
 * type : kind of laser ray (red, green, etc).
 * direction : compass direction ray is emitting (45, 135, 225, 315).
*/

public class Ray extends Location {
    enum Type {
        RED, GREEN
    }
    private Type type;
    private int direction;

    public Ray(int x, int y, Type type, int direction) {
        super(x, y);
        this.type = type;
        this.direction = direction;
    }

    public Ray(Ray ray) {
        super(ray.getX(), ray.getY());
        this.type = ray.getType();
        this.direction = ray.getDirection();
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }
}

