package com.dagnaikuba.search_strategies;

import com.dagnaikuba.Board;
import com.dagnaikuba.Order;

import java.util.*;

public final class DFS {


    public static String dfs(Board b, List<Order> order) {


        StringBuilder movesDone = new StringBuilder();
        Order next;

        Stack<Board> stack = new Stack<>();
        Map<Board, Board> bmap = new HashMap<>();

        bmap.put(b,null);
        stack.push(b);


        int counter = 0;
        while (true) {

            if (stack.isEmpty()||counter>5000000) return new String("");

            Board popPrepare = stack.pop();

            Board pop = new Board(Board.cloneArray(popPrepare.tiles), popPrepare.emptyX, popPrepare.emptyY, popPrepare.moveFromPreviousState);
            if (pop.isSolved()) {
                            System.out.println("found it!");
                            System.out.println(counter);
                            //  BoardView.printBoard(pop);


                            Board backwardsBoard = pop;
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

            Optional<Board> up = pop.move(Order.U);
            Optional<Board> right = pop.move(Order.R);
            Optional<Board> down = pop.move(Order.D);
            Optional<Board> left = pop.move(Order.L);

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
                        pushIfPresent(up,pop,bmap, stack);
                        break;
                    case R:
                        pushIfPresent(right,pop, bmap, stack);
                        break;
                    case D:
                        pushIfPresent(down, pop,bmap, stack);
                        break;
                    case L:
                        pushIfPresent(left,pop, bmap, stack);
                        break;


                }
            }
        }
        movesDone.reverse();
        return movesDone.toString();

    }

    public static void pushIfPresent(Optional<Board> b, Board prev, Map map, Stack<Board> stack) {
        if (b.isPresent() && !map.containsKey(b.get())){
            map.put(b.get(),prev);
            stack.push((b.get()));
        }
    }
}
