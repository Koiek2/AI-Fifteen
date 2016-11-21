package com.dagnaikuba;

import com.dagnaikuba.search_strategies.*;
import java.util.ArrayList;
import java.util.List;

public class Application {



    public static void main(String[] args) {
        //input: 3 3 2 4 3 1 0 5 7 8 6
    	if(args.length==0){
        	System.out.println("No input!");
        	return;
        }
    	
    	else if(args.length!=(Integer.parseInt(args[0])*Integer.parseInt(args[1]))+2){
        	System.out.println("wrong input");
        	return;
        }
           
        int boardRows = Integer.parseInt(args[0]);
        int boardCols = Integer.parseInt(args[1]);
        int[][] tiles = new int[boardRows][boardCols];
        
        int k=2;
        for(int i=0;i<boardRows;i++){
        	for(int j=0;j<boardCols;j++){
        		tiles[i][j]=Integer.parseInt(args[k]);
        		k++;
        	}
        }
        
        Board board = new Board(tiles);

        board.moveFromPreviousState = null;

        System.out.println("Starting board:");
        board.printBoard();


        List<Order> orders = new ArrayList<>();
        orders.add(Order.D);
        orders.add(Order.R);
        orders.add(Order.U);
        orders.add(Order.L);

//        String bfsString = AStar.aStar(board, "MISPLACED_TILES");
//        System.out.println(bfsString.length());
//        System.out.println(bfsString);
        
        String smaString = SMAStar.smaStar(board, "MISPLACED_TILES", 4);
        if(smaString.length()!=0){
        	System.out.println("solution length " + smaString.length());        
        	System.out.println(smaString);
        }
        else{
        	System.out.println("no solution was found");
        }
        
        /*String bfsString = IDFS.idfs(board, orders);
        System.out.println(bfsString.length());
        System.out.println(bfsString);*/
        


//        String dfsString = com.dagnaikuba.DFSegies.DFS.dfs(board,orders);
//        System.out.println(dfsString.length());
//        System.out.println(dfsString);

        //BoardView.printBoard(board);

    }
}
