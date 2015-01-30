package com.example.cesar.laze;

import android.util.Log;

import java.util.ArrayDeque;

/**
 * Created by Cesar on 1/24/2015.
 * Block class
 * Defines a Block object on the playfield.
 * <p/>
 * location : location on playfield grid.
 * type : kind of block  (open, mirror, glass, etc).
 * rays : Array list of rays incident on the block.
 */
public class Block extends Location {
    final static String tag = "LAZE";
	enum Type {
        OPEN, MIRROR, GLASS, CRYSTAL, WORMHOLE, BLACKHOLE
    }

    private Type type;
    private ArrayDeque<Ray> rays;

    public Block(int x, int y, Type type, ArrayDeque<Ray> rays) {
        super(x, y);
        this.type = type;
        this.rays = rays;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public ArrayDeque<Ray> getRays() {
        Log.d(tag, "getRays: " + rays.toString());
        return rays;
    }

    public void setRays(ArrayDeque<Ray> rays) {
        this.rays = rays;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        ArrayDeque<Ray> raysClone = new ArrayDeque<>();
        for (Ray ray : rays) raysClone.push(new Ray(ray));

        return new Block(getX(), getY(), type, raysClone);
    }

    @Override
    public String toString() {
        return "Block{" +
                "x=" + this.getX()+
                "y=" + this.getY() +
                "type=" + type +
                "rays=" + rays +
                '}';
    }
}
