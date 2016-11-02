package com.dagnaikuba.search_strategies;

import com.dagnaikuba.*;

import java.util.*;

public final class BFS {


    public static String bfs(Board b, List<Order> order) {


        String movesDone;
        Order next;

        Queue<Board> queue = new LinkedList<>();
        Map<Board, Board> bmap = new HashMap<>();

        bmap.put(b,null);
        queue.add(b);


        int counter = 0;
        while (true) {



            if (queue.isEmpty()) return "";

            Board removePrepare = queue.remove();

            Board remove = new Board(Board.cloneArray(removePrepare.tiles), removePrepare.emptyX, removePrepare.emptyY, removePrepare.moveFromPreviousState);
            if (remove.isSolved()) {
                System.out.println("found it! in tries: " );
                System.out.println(counter);
                //  BoardView.printBoard(remove);
               return  Board.TraceBack(remove,bmap);
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
                        addIfPresent(remove.move(Order.U),remove,bmap, queue);
                        break;
                    case R:
                        addIfPresent(remove.move(Order.R),remove, bmap, queue);
                        break;
                    case D:
                        addIfPresent(remove.move(Order.D), remove,bmap, queue);
                        break;
                    case L:
                        addIfPresent(remove.move(Order.L),remove, bmap, queue);
                        break;


                }
            }
        }

    }

    public static void addIfPresent(Optional<Board> b, Board prev, Map map, Queue<Board> queue) {
        if (b.isPresent() && !map.containsKey(b.get())){
            map.put(b.get(),prev);
            queue.add((b.get()));
        }
    }
}
