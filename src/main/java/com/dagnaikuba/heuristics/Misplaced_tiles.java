package com.dagnaikuba.heuristics;

import com.dagnaikuba.Board;

public final class Misplaced_tiles {

    public static int evaluate(Board b){

        int evaluation = -1; //cause it will add empty spot as 'misplaced', so starting at -1 evens it out to 0.
        int height = b.getHeight();
        int width = b.getWidth();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (b.tiles[i][j] != i * height + j + 1) evaluation++;
            }
        }
        return evaluation;
    }

}
