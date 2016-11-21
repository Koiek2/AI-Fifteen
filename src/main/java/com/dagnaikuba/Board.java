package com.dagnaikuba;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Board{

    public int[][] tiles;
    private int height;

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    private int width;
    public int emptyX;
    public int emptyY;
    public Order moveFromPreviousState = null;
    private int priority;
    public int bestForgottenSuccessorPriority=-1;
    private int stepsFromOriginal;
    public int depth;
    public List<Board> successors = new ArrayList<>();
    public List<Order> ordersUsed;
    public Map<Board,State> successorsState = new HashMap<>();
   // public State state;
    
    public Board(){}

    public Board(int size) {
        height = size;
        width = size;
        tiles = new int[size][size];
        successors = new ArrayList<>();
        ordersUsed = new ArrayList<>();
    }

    public Board(int height, int width) {
        this.height = height;
        this.width = width;
        tiles = new int[height][width];
        successors = new ArrayList<>();
        ordersUsed = new ArrayList<>();
    }

    public Board(int[][] _tiles) {
        tiles = _tiles;
        height = tiles.length;
        width = tiles[0].length;
        findAndSetEmpty();
        successors = new ArrayList<>();
        ordersUsed = new ArrayList<>();
    }

    public Board(int[][] _tiles, int emptyX, int emptyY, Order moveFromPreviousState) {
        tiles = _tiles;
        height = tiles.length;
        width = tiles[0].length;
        this.emptyX = emptyX;
        this.emptyY = emptyY;
        this.moveFromPreviousState = moveFromPreviousState;
        successors = new ArrayList<>();
        ordersUsed = new ArrayList<>();
    }

    public Board(int[][] _tiles, int emptyX, int emptyY, Order moveFromPreviousState, int priority) {
        tiles = _tiles;
        height = tiles.length;
        width = tiles[0].length;
        this.emptyX = emptyX;
        this.emptyY = emptyY;
        this.moveFromPreviousState = moveFromPreviousState;
        this.priority = priority;
        successors = new ArrayList<>();
        ordersUsed = new ArrayList<>();
    }
    public void initializeSolved() {
        int res[][] = new int[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                res[i][j] = i * height + j + 1;
            }
        }
        res[height - 1][width - 1] = 0;
        tiles = res;
        findAndSetEmpty();
    }

    public static String TraceBack(Board backwardsBoard, Map<Board,Board> bmap){
        StringBuilder movesDone = new StringBuilder();
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
        return movesDone.reverse().toString();
    }
    public  void printBoard(){

        // System.out.println("\nPrinting board:\n");
        for (int i = 0; i < height; i++) {
            System.out.println("");
            for (int j = 0; j < width; j++) {
                System.out.format("%4d",tiles[i][j]);
            }
        }
        System.out.println("");
    }

    public static int[][] cloneArray(int[][] src) {
        int length = src.length;
        int[][] target = new int[length][src[0].length];
        for (int i = 0; i < length; i++) {
            System.arraycopy(src[i], 0, target[i], 0, src[i].length);
        }
        return target;
    }
    
    public Board clone() {
		Board clonedNode = new Board(height,width);
		for (int i = 0 ; i < height ; ++i) {
			for (int j = 0 ; j < width ; ++j) {
				clonedNode.tiles[i][j] = tiles[i][j];
			}
		}
		clonedNode.emptyX = emptyX;
		clonedNode.emptyY = emptyY;		
		clonedNode.depth=depth;
		clonedNode.moveFromPreviousState=moveFromPreviousState;
		clonedNode.bestForgottenSuccessorPriority=bestForgottenSuccessorPriority;
		clonedNode.priority=priority;
		clonedNode.stepsFromOriginal = stepsFromOriginal;
		clonedNode.successors=successors;
		clonedNode.ordersUsed=ordersUsed;
		clonedNode.successorsState=successorsState;

		
		return clonedNode;
	}
    
    public void findAndSetEmpty() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (tiles[i][j] == 0) {
                    emptyX = i;
                    emptyY = j;
                }
            }
        }
    }
    public Optional<Board> move(Order dir){

        Board board = new Board(Board.cloneArray(tiles),emptyX,emptyY,Order.Rand);
        switch(dir){
            case U:
                if (emptyX == 0) return Optional.empty();
                board.tiles[emptyX][emptyY] = tiles[emptyX - 1][emptyY];
                board.tiles[emptyX - 1][emptyY] = 0;
                board.emptyX--;
                board.moveFromPreviousState = Order.U;
                break;

            case R:
                if (emptyY == width - 1) return Optional.empty();
                board.tiles[emptyX][emptyY] = tiles[emptyX][emptyY + 1];
                board.tiles[emptyX][emptyY + 1] = 0;
                board.emptyY++;
                board.moveFromPreviousState = Order.R;
                break;

            case D:
                if (emptyX == height - 1) return Optional.empty();
                board.tiles[emptyX][emptyY] = tiles[emptyX + 1][emptyY];
                board.tiles[emptyX + 1][emptyY] = 0;
                board.emptyX++;
                board.moveFromPreviousState=Order.D;
                break;

            case L:
                if (emptyY == 0) return Optional.empty();
                board.tiles[emptyX][emptyY] = tiles[emptyX][emptyY - 1];
                board.tiles[emptyX][emptyY - 1] = 0;
                board.emptyY--;
                board.moveFromPreviousState = Order.L;
                break;

            default:
                return Optional.empty();
        }
        board.setStepsFromOriginal(stepsFromOriginal+1);
        return Optional.of(board);
    }

    public int calculatePossibleSteps(){
    	if((emptyX == 0 & emptyY == 0) || (emptyX == 0 & emptyY == width - 1) || (emptyX == height - 1 & emptyY == 0) || (emptyX == height - 1 & emptyY == width - 1)){
    		return 2;
    	}
    	else if(emptyX == 0 || emptyY == 0 || emptyX == height - 1 || emptyY == width - 1){
    		return 3;
    	}
    	else{
    		return 4;
    	}
    }
    
    public boolean isSolved() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (tiles[i][j] != i * height + j + 1) {
                    if(i== (height-1)&&j==(width -1)) return true;
                    else return false;
                }
            }
        }
        return true;
    }
    public int getPriority() {
        return priority;
    }


    public void setPriority(int priority) {
        this.priority = priority;
    }

    public void setBoard(int[][] _tiles) {
        this.tiles = _tiles;

    }

    public int getStepsFromOriginal() {
        return stepsFromOriginal;
    }
    public void setStepsFromOriginal(int stepsFromOriginal) {
        this.stepsFromOriginal = stepsFromOriginal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Board board = (Board) o;

        return Arrays.deepEquals(tiles, board.tiles);

    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(tiles);
    }
    
    public boolean tilesEqual(Board b){
    	for(int i=0;i<height;i++){
    		for(int j=0;j<width;j++){
    			if(this.tiles[i][j]!=b.tiles[i][j]) return false;
    		}
    	}
    	return true;
    }
}
