package com.dagnaikuba;


import com.dagnaikuba.heuristics.Misplaced_tiles;

public final class Evaluator {

    public static int evaluate(Board b, String heuristic){

        switch(heuristic){
            case "MISPLACED_TILES":
                return Misplaced_tiles.evaluate(b);
        }

        return 0;
    }

}
