package com.example.cesar.laze;

import java.util.ArrayList;

/**
 * Created by Cesar on 1/26/2015.
 * LazeGame Model class
 */
public class LazeGame {

    static final int GRID_WIDTH = 4;
    static final int GRID_HEIGHT = 4;

    public LazeGame(int gridWidth, int gridHeight) {

        Block[][] playGrid = new Block[gridWidth][gridHeight];

        for (int i = 0; i < gridWidth; i++) {
            for (int j = 0; j < gridHeight; j++) {
                playGrid[i][j] = new Block(Block.Type.OPEN, new ArrayList<Integer>(0), new ArrayList<Integer>(0), new ArrayList<Integer>(0), new ArrayList<Integer>(0));

            }
        }
    }


}
