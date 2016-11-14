package com.dagnaikuba.search_strategies;

import com.dagnaikuba.Board;
import com.dagnaikuba.Order;

import java.util.*;

public final class DFS {


    public static String dfs(Board b, List<Order> order) {

        Order next;

        Stack<Board> stack = new Stack<>();
        Map<Board, Board> bmap = new HashMap<>();

        bmap.put(b, null);
        stack.push(b);


        int counter = 0;
        while (true) { //while(stack.size()>0)

            if (stack.isEmpty() || counter > 5000000) return new String(""); //bez stack.isEmpty()

            Board popPrepare = stack.pop();

            Board pop = new Board(Board.cloneArray(popPrepare.tiles), popPrepare.emptyX, popPrepare.emptyY, popPrepare.moveFromPreviousState);
            if (pop.isSolved()) {
                System.out.println("found it!");
                System.out.println(counter);
                //  BoardView.printBoard(pop);

                return Board.TraceBack(pop, bmap);

            }

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


            for (int j = 3; j >= 0; j--) {

                next = currentOrder.get(j);
                switch (next) {
                    case U:
                        pushIfPresent(pop.move(Order.U), pop, bmap, stack);
                        break;
                    case R:
                        pushIfPresent(pop.move(Order.R), pop, bmap, stack);
                        break;
                    case D:
                        pushIfPresent(pop.move(Order.D), pop, bmap, stack);
                        break;
                    case L:
                        pushIfPresent(pop.move(Order.L), pop, bmap, stack);
                        break;


                }
            }

        }

    }

    public static void pushIfPresent(Optional<Board> b, Board prev, Map map, Stack<Board> stack) { //po co?
        if (b.isPresent() && !map.containsKey(b.get())) {
            map.put(b.get(), prev);
            stack.push((b.get()));
        }
    }
}
