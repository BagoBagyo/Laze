package com.example.cesar.laze;

import java.util.List;

/**
 * Created by Cesar on 1/24/2015.
 * Basic Block class.
 */
public class Block {
    enum Type {
        OPEN, MIRROR, GLASS, CRYSTAL, WORMHOLE, BLACKHOLE
    }

    private Type type;
    private List<Integer> northFace;
    private List<Integer> eastFace;
    private List<Integer> southFace;
    private List<Integer> westFace;

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public List<Integer> getNorthFace() {
        return northFace;
    }

    public void setNorthFace(List<Integer> northFace) {
        this.northFace = northFace;
    }

    public List<Integer> getEastFace() {
        return eastFace;
    }

    public void setEastFace(List<Integer> eastFace) {
        this.eastFace = eastFace;
    }

    public List<Integer> getSouthFace() {
        return southFace;
    }

    public void setSouthFace(List<Integer> southFace) {
        this.southFace = southFace;
    }

    public List<Integer> getWestFace() {
        return westFace;
    }

    public void setWestFace(List<Integer> westFace) {
        this.westFace = westFace;
    }

    public Block(Type type, List<Integer> northFace, List<Integer> eastFace, List<Integer> southFace, List<Integer> westFace) {
        this.type = type;
        this.northFace = northFace;
        this.eastFace = eastFace;
        this.southFace = southFace;
        this.westFace = westFace;
    }
}
