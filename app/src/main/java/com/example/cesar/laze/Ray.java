package com.example.cesar.laze;

/**
 * Created by Cesar on 1/24/2015.
 * Ray class
 * Defines a ray object (block incident ray)
 * <p/>
 * location : location on playfield grid.
 * type : kind of laser ray (red, green, etc).
 * direction : compass direction ray is emitting (45, 135, 225, 315).
 */

public class Ray extends Location {
    private Type type;
    private int direction;

    public Ray(int x, int y, Type type, int direction) {
        super(x, y);
        this.type = type;
        this.direction = direction;
    }

    public Ray(Ray ray) {
        super(ray);
        type = ray.type;
        direction = ray.direction;
    }

    @Override
    public boolean equals(Object o) {
        return this.getX() == ((Ray) o).getX() &&
                this.getY() == ((Ray) o).getY() &&
                this.getType() == ((Ray) o).getType() &&
                this.getDirection() == ((Ray) o).getDirection();
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

    @Override
    public String toString() {
        return "Ray{" +
                "x=" + this.getX() +
                "y=" + this.getY() +
                "type=" + type +
                "direction=" + direction +
                '}';
    }

    enum Type {
        RED, GREEN
    }
}

