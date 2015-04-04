package com.example.cesar.laze;

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
    private Type type;
    private ArrayDeque<Ray> rays;

    public Block(int x, int y, Type type, ArrayDeque<Ray> rays) {
        super(x, y);
        this.type = type;
        this.rays = rays;
    }

    Type getType() {
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

    public boolean isDraggable() {
        return !(type == Type.OPEN) &&
                !(type == Type.DEADZONE) &&
                !(type == Type.FIXED_MIRROR);
    }

    public boolean canBeDroppedOn() {
        return !(type == Type.DEADZONE) &&
                !(type == Type.FIXED_MIRROR);
    }

    @Override
    public String toString() {
        return "Block{" +
                "x=" + this.getX() +
                "y=" + this.getY() +
                "type=" + type +
                "rays=" + rays +
                '}';
    }

    enum Type {
        OPEN, MIRROR, FIXED_MIRROR, GLASS, CRYSTAL, WORMHOLE, BLACKHOLE, DEADZONE
    }
}
