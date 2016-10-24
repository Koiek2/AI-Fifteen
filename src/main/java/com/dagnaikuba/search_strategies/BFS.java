package com.dagnaikuba.search_strategies;

import com.dagnaikuba.*;

import java.util.*;

public final class BFS {


    public static String bfs(Board b, List<Order> order) {


        StringBuilder movesDone = new StringBuilder();
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
                System.out.println("found it!");
                System.out.println(counter);
                //  BoardView.printBoard(remove);


                Board backwardsBoard = remove;
                while(bmap.get(backwardsBoard)!=null) {

                    switch (backwardsBoard.moveFromPreviousState) {
                        case U:
                            movesDone.append("U");
                            backwardsBoard = bmap.get(backwardsBoard);
                            break;
                        case R:
                            movesDone.append("R");
                            backwardsBoard = bmap.get(backwardsBoard);
                            break;
                        case D:
                            movesDone.append("D");
                            backwardsBoard = bmap.get(backwardsBoard);
                            break;
                        case L:
                            movesDone.append("L");
                            backwardsBoard = bmap.get(backwardsBoard);
                            break;

                    }
                }

                break;

            }
            counter++;

            Optional<Board> up = remove.move(Order.U);
            Optional<Board> right = remove.move(Order.R);
            Optional<Board> down = remove.move(Order.D);
            Optional<Board> left = remove.move(Order.L);

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
                        addIfPresent(up,remove,bmap, queue);
                        break;
                    case R:
                        addIfPresent(right,remove, bmap, queue);
                        break;
                    case D:
                        addIfPresent(down, remove,bmap, queue);
                        break;
                    case L:
                        addIfPresent(left,remove, bmap, queue);
                        break;


                }
            }
        }
        movesDone.reverse();
        return movesDone.toString();

    }

    public static void addIfPresent(Optional<Board> b, Board prev, Map map, Queue<Board> queue) {
        if (b.isPresent() && !map.containsKey(b.get())){
            map.put(b.get(),prev);
            queue.add((b.get()));
        }
    }
}
