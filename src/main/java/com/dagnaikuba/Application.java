package com.dagnaikuba;

import com.dagnaikuba.search_strategies.*;
import java.util.ArrayList;
import java.util.List;

public class Application {



    public static void main(String[] args) {
        Board board = new Board(3);
        board.initializeSolved();


        board = board.move(Order.U).get();
        board = board.move(Order.L).get();
        board = board.move(Order.L).get();

        board.moveFromPreviousState = null;

        System.out.println("Starting board:");
        board.printBoard();


        List<Order> orders = new ArrayList<>();
        orders.add(Order.D);
        orders.add(Order.R);
        orders.add(Order.U);
        orders.add(Order.L);

        String bfsString = AStar.aStar(board, "MISPLACED_TILES");
        System.out.println(bfsString.length());
        System.out.println(bfsString);


//        String dfsString = com.dagnaikuba.DFSegies.DFS.dfs(board,orders);
//        System.out.println(dfsString.length());
//        System.out.println(dfsString);

        //BoardView.printBoard(board);

    }
}
