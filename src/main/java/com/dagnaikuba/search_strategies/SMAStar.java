package com.dagnaikuba.search_strategies;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.Queue;

import com.dagnaikuba.Board;
import com.dagnaikuba.BoardComparator;
import com.dagnaikuba.Evaluator;
import com.dagnaikuba.Order;
import com.dagnaikuba.SMAComparator;
import com.dagnaikuba.State;

public class SMAStar {
	public static String smaStar(Board b, String heuristic,int maxDepth) {


        PriorityQueue<Board> queue = new PriorityQueue<>(new SMAComparator());
        PriorityQueue<Board> leafList = new PriorityQueue<>(new SMAComparator().reversed());
        
        Map<Board, Board> bmap = new HashMap<>();

        bmap.put(b, null);
        b.setPriority(Evaluator.evaluate(b, heuristic));
        b.setStepsFromOriginal(0);
        queue.add(b);
        int counter = 0;
        b.depth=0;
        while (true) {
            if (queue.isEmpty()) return "";
       
            Board currentNodePrepare = queue.peek();
            Board currentNode = currentNodePrepare.clone();

            //System.out.println("currentNode");
            //currentNode.printBoard();
            if (currentNode.isSolved()) {
                System.out.println("found it!");
                System.out.println("counter " + counter);
                return Board.TraceBack(currentNode, bmap);
            }

            counter++;
            
            Board successor=new Board();                      
            List<Order> orders = new ArrayList<>();
            orders.add(Order.U); 
            orders.add(Order.R); 
            orders.add(Order.D); 
            orders.add(Order.L); 
            int i=0; 
            boolean moveDone=false; 
            while(i<orders.size() && moveDone!=true) //make a move if legal and not yet made
            {
            	if(!currentNode.ordersUsed.contains(orders.get(i))){
                	successor = setSuccessorPriority(orders.get(i), maxDepth, currentNode, bmap, queue, heuristic,leafList);
                	currentNode.ordersUsed.add(orders.get(i));

                	if(successor!=null ){ //move was made
                		moveDone=true;            	
                	}
                }           	
            	i++;
            }
            
            if(!moveDone){ //no more moves possible from this node - remove from queue
            	successor=null;
            	queue.remove(currentNode);
            }
            
            //czy to jest to samo o wyzej?
            if(currentNode.successors.size()==currentNode.calculatePossibleSteps()){
	            if (queue.containsAll(currentNode.successors)){ 
	            	queue.remove(currentNode);
	            	System.out.println("usuwam");
	            }
            }
            
            if(successor==null) { //if no new successor - choose another parent node (omit the rest of the loop)
            	System.out.println("successor to nunll");
            	continue;
            }
            
//          drukowanko successora
//          else{
//             System.out.println("successor");
//             successor.printBoard();
//          }
            

//          if such a board was obtained earlier through some other steps - choose better path to it
            boolean sameExists = false;
            for(Board board : queue){
            	if(board.tilesEqual(successor)) {
            		int boardPriority = Evaluator.evaluate(board, heuristic) + board.getStepsFromOriginal();
            		int successorPriority =Evaluator.evaluate(successor, heuristic) + successor.getStepsFromOriginal(); 
            		if(successorPriority>boardPriority){
            			currentNode.successorsState.put(successor, State.blocked);
            			Board currentNodeParent = bmap.get(currentNode);
            			if(checkIfAllSuccessorsBlocked(currentNodeParent)){
            				currentNodeParent.setPriority(Integer.MAX_VALUE);
            			}
            		}
            		else{
            			queue.add(successor);
            			queue.remove(board);
            			leafList.remove(board);
            			Board boardParent = bmap.get(board);
            			boardParent.successors.remove(board);
            			boardParent.successorsState.put(successor, State.blocked);
            			if(checkIfAllSuccessorsBlocked(boardParent)){
            				boardParent.setPriority(Integer.MAX_VALUE);
            			}
            			
            		}
            		sameExists=true;
            	}
            }
            if(!sameExists){
            	queue.add(successor);
            	leafList.remove(currentNode);
            }
            
           
//			if all the children are known update priority
            if(currentNode.successorsState.size()==currentNode.calculatePossibleSteps()){ ///czy tylko dzieci w drzewie czy wszystkie?
            	if(currentNode.successors.size()==currentNode.successorsState.size()){
            		currentNode.setPriority(chooseLowestCostChild(currentNode).getPriority());
            	}
            	else{
            		currentNode.setPriority(Math.min(currentNode.bestForgottenSuccessorPriority,chooseLowestCostChild(currentNode).getPriority()));
            	}
            }
            
            
            System.out.println("queue.size "+queue.size());
            
            //if number of nodes in a queue reaches the maxdepth value - remove the least promissing leaf
            if(queue.size()==maxDepth){
            	System.out.println("max depth reached");
            	forgetLeaf(leafList,queue,bmap);
            }        
        }
		


    }
	
	public static boolean checkIfAllSuccessorsBlocked(Board boardParent){
		for(State state : boardParent.successorsState.values()){
			if(state!=State.blocked) return false;
		}
		return true;
	}
	
	public static void forgetLeaf(Queue<Board> leafList, Queue<Board> queue, Map<Board,Board> bmap){
		//jak najwiekszy depth, jak najwieksze priority
		System.out.println("leaflist size" + leafList.size());
		for(Board b : leafList){
    		b.printBoard();
    		System.out.println("priority " + b.getPriority());
    		System.out.println("successors number in queue " +b.successors.size());
    		System.out.println("depth " + b.depth);
    	}
    	Board badNode = leafList.remove();
    	queue.remove(badNode);
    	System.out.println("queue.size after remove" + queue.size());
    	Board badNodeParent = bmap.get(badNode);
    	//if not yet set or bigger than new one
    	if(badNodeParent.bestForgottenSuccessorPriority==-1 || badNodeParent.bestForgottenSuccessorPriority>badNode.getPriority()){
    		badNodeParent.bestForgottenSuccessorPriority=badNode.getPriority();
    	}
    	badNodeParent.successors.remove(badNode);
    	badNodeParent.successorsState.put(badNode, State.forgotten);
    	
    	if(badNodeParent.successors.isEmpty()){
    		leafList.add(badNodeParent);
    	}

    	if (!queue.contains(badNodeParent)) {
    		queue.add(badNodeParent); 
    	}
	}
	
	public static Board chooseLowestCostChild(Board parent){
		int lowestCost = Integer.MAX_VALUE;
		Board lowestCostChild=new Board();
		for(Board b : parent.successors){
			if(b.getPriority()<lowestCost){
				lowestCostChild = b;
				lowestCost = b.getPriority();
			}
		}
		return lowestCostChild;
	}


	 public static Optional<Board> evaluateAndAddIfPresent(Optional<Board> b, Board prev, Map map, Queue<Board> queue, String heuristic, Queue<Board> leafList) {
	        if (b.isPresent()) {
	            Board board = b.get();
	            if (!map.containsKey(board)) {
	                board.setPriority(Evaluator.evaluate(board, heuristic) + board.getStepsFromOriginal()); //A star magic happens here
	                map.put(b.get(), prev);
	                //queue.add((b.get()));
	                prev.successors.add(board);
	                System.out.println("successor added");
	                leafList.remove(prev);
	                leafList.add(board);
	                board.depth=prev.depth+1;
	            }    
	        }
	        return b;
	    }
	 
	 public static Board setSuccessorPriority(Order order, int maxDepth, Board parentBoard, Map bmap, Queue<Board> queue, String heuristic, Queue<Board> leafList){
		 Optional<Board> successorOpt = evaluateAndAddIfPresent(parentBoard.move(order), parentBoard, bmap, queue, heuristic,leafList);
		 if (successorOpt.isPresent()) {
             Board successor = successorOpt.get();
             if(!successor.isSolved() && queue.size()==maxDepth){ 
             	successor.setPriority(Integer.MAX_VALUE);
             }
             else{
             	successor.setPriority(Math.max(parentBoard.getPriority(),Evaluator.evaluate(successor, heuristic) + successor.getStepsFromOriginal()));
             }
             return successor;
         }
		 return null;
		 
	 }
}
