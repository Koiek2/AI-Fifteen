package com.dagnaikuba.search_strategies;

import com.dagnaikuba.Board;
import com.dagnaikuba.Order;

import java.util.*;

public final class IDFS {

    public static int counter;

    public static String idfs(Board b, List<Order> order) {
        counter = 0;

        Map<Board, Board> bmap = new HashMap<>();


        Board solution = null;
        for (int i = 0; i < Integer.MAX_VALUE; i++) {

            bmap.clear();
            bmap.put(b, null);
            solution = dls(b, order, bmap, i);
            if (solution != null)
                break;

        }
        System.out.println("found it!");
        System.out.println(counter);
        //  BoardView.printBoard(pop);


        return Board.TraceBack(solution,bmap);

    }

    private static Board dls(Board board, List<Order> order, Map<Board, Board> bmap, int depth) {

        if (board.isSolved()) return board;
        else if (depth > 0) {
            counter++;

            boolean isRandom = order.get(0).equals(Order.Rand);
            List<Order> currentOrder;

            if (isRandom) {
                List<Order> randOrder = new ArrayList<>();
                randOrder.add(Order.U);
                randOrder.add(Order.R);
                randOrder.add(Order.D);
                randOrder.add(Order.L);
                Collections.shuffle(randOrder);

                currentOrder = randOrder;
            } else {
                currentOrder = order;
            }

            Optional<Board> up = board.move(Order.U);
            Optional<Board> right = board.move(Order.R);
            Optional<Board> down = board.move(Order.D);
            Optional<Board> left = board.move(Order.L);

            for (int j = 3; j >= 0; j--) {

                Board tmp = null;
                switch (currentOrder.get(j)) {
                    case U:
                        tmp = recurseIfPresent(up, board, order, bmap, depth);
                        break;
                    case R:
                        tmp =recurseIfPresent(right, board, order, bmap, depth);
                        break;
                    case D:
                        tmp =recurseIfPresent(down, board, order, bmap, depth);
                        break;
                    case L:
                        tmp = recurseIfPresent(left, board, order, bmap, depth);
                        break;


                }
                if(tmp!=null) {
                    return tmp;
                }

            }
        }


        return null;
    }

    private static Board recurseIfPresent(Optional<Board> b, Board prev, List<Order> order, Map map, int depth) {
        if (b.isPresent() && !map.containsKey(b.get())) {
            map.put(b.get(), prev);
            return dls(b.get(), order, map, depth - 1);
        }
        return null;
    }

}