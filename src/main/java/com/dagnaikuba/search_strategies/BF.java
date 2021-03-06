package com.dagnaikuba.search_strategies;

import com.dagnaikuba.Board;
import com.dagnaikuba.BoardComparator;
import com.dagnaikuba.Evaluator;
import com.dagnaikuba.Order;

import java.util.*;

public class BF {

    public static String bestFirst(Board b, String heuristic) {


        PriorityQueue<Board> queue = new PriorityQueue<>(new BoardComparator());
        Map<Board, Board> bmap = new HashMap<>();

        bmap.put(b, null);
        b.setPriority(0);
        queue.add(b);
        int counter = 0;
        while (true) {


            if (queue.isEmpty()) return "";

            Board removePrepare = queue.remove();

            Board remove = new Board(Board.cloneArray(removePrepare.tiles), removePrepare.emptyX, removePrepare.emptyY, removePrepare.moveFromPreviousState);
            if (remove.isSolved()) {

                System.out.println("found it!");
                System.out.println(counter);
                //  BoardView.printBoard(remove);

                return Board.TraceBack(remove, bmap);


            }
            counter++;

            evaluateAndAddIfPresent(remove.move(Order.U), remove, bmap, queue, heuristic);
            evaluateAndAddIfPresent(remove.move(Order.R), remove, bmap, queue, heuristic);
            evaluateAndAddIfPresent(remove.move(Order.D), remove, bmap, queue, heuristic);
            evaluateAndAddIfPresent(remove.move(Order.L), remove, bmap, queue, heuristic);
        }


    }

    public static void evaluateAndAddIfPresent(Optional<Board> b, Board prev, Map map, Queue<Board> queue, String heuristic) {
        if (b.isPresent() && !map.containsKey(b.get())) {
            b.get().setPriority(Evaluator.evaluate(b.get(), heuristic));
            map.put(b.get(), prev);
            queue.add((b.get()));
        }
    }
}
