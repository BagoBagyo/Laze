package com.example.cesar.laze;

import java.util.ArrayDeque;

/**
 * Created by Cesar on 1/24/2015.
 * Block class
 * Defines a Block object on the playfield.
 *
 * location : location on playfield grid.
 * type : kind of block  (open, mirror, glass, etc).
 * rays : Array list of rays incident on the block.
 */
public class Block extends Location {
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
        return rays;
    }

    public void setRays(ArrayDeque<Ray> rays) {
        this.rays = rays;
    }
}
