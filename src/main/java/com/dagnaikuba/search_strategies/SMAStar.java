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
        //PriorityQueue<Board> leafList = new PriorityQueue<>(new SMAComparator().reversed());
        
        Map<Board, Board> bmap = new HashMap<>();

        bmap.put(b, null);
        b.setPriority(Evaluator.evaluate(b, heuristic));
        b.setStepsFromOriginal(0);
        queue.add(b);
        System.out.println("added 0");
        int counter = 0;
        b.depth=0;
        while (true) {
            if (queue.isEmpty()) return "";
       
            Board currentNodePrepare = queue.peek();
            Board currentNode = currentNodePrepare.clone();

            if (currentNode.isSolved()) {
                System.out.println("found it!");
                System.out.println("counter " + counter);
                return Board.TraceBack(currentNode, bmap);
            }

            counter++;
            /*if(counter>5000000){
            	//break;
            	return "";
            	
            }*/
            
            if(checkIfDone(queue)){
            	return "";
            }
            
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
                	successor = setSuccessorPriority(orders.get(i), maxDepth, currentNode, bmap, queue, heuristic);//,leafList);
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
            
            
            if(successor==null) { //if no new successor - choose another parent node (omit the rest of the loop)
            	System.out.println("successor to null");
            	continue;
            }
                 

//          if such a board was obtained earlier through some other steps - choose better path to it
            boolean sameExists = false;
            for(Board board : queue){
            	if(board.tilesEqual(successor)) {
            		int boardPriority = board.getStepsFromOriginal();
            		int successorPriority =successor.getStepsFromOriginal(); 
            		System.out.println("board priority  " + boardPriority);
            		System.out.println("successor priority  " + successorPriority);
            		if(successorPriority>=boardPriority){
            			currentNode.successorsState.put(successor, State.blocked);
            			currentNode.successors.add(successor);
            			Board currentNodeParent = bmap.get(currentNode);
            			if(currentNodeParent!=null){
	            			System.out.println(currentNodeParent.depth);
	            			System.out.println("children "+ currentNodeParent.successors.size());
	            			if(checkIfAllSuccessorsBlocked(currentNodeParent)){
	            				currentNodeParent.setPriority(Integer.MAX_VALUE);
	            			}
            			}
            		}
            		else{
            			queue.add(successor);
            			System.out.println("added 1");
            			queue.remove(board);
            			//leafList.remove(board);
            			currentNode.successors.add(successor);
            			currentNode.successorsState.put(successor, State.exists);
            			Board boardParent = bmap.get(board);
            			if(boardParent!=null){
	            			System.out.println(board.depth);
	            			boardParent.successors.remove(board);
	            			boardParent.successorsState.put(successor, State.blocked);
	            			if(checkIfAllSuccessorsBlocked(boardParent)){
	            				boardParent.setPriority(Integer.MAX_VALUE);
	            			}
            			}
            			
            		}
            		sameExists=true;
            	}
            }
            if(!sameExists){
            	queue.add(successor);
            	System.out.println("added 2");
            	//leafList.remove(currentNode);
            	currentNode.successors.add(successor);
            	currentNode.successorsState.put(successor, State.exists);
            	//leafList.add(successor);
            }
            
           
//			if all the children are known update priority (if all children are currently in the queue we take lowest cost child, 
            //if not (but they were generated, we have to compare lowest cost child to best forgotten successor
            if(currentNode.successorsState.size()==currentNode.calculatePossibleSteps()){ ///czy tylko dzieci w drzewie czy wszystkie?
            	if(currentNode.successors.size()==currentNode.successorsState.size()){
            		currentNode.setPriority(chooseLowestCostChild(currentNode).getPriority());
            		currentNode.bestForgottenSuccessorPriority=-1; 
            		while(bmap.get(currentNode)!=null){
	            		Board parent = bmap.get(currentNode);
	            		if(parent.getPriority()>currentNode.getPriority()){
	            			parent.setPriority(currentNode.getPriority());
	            		}
	            		currentNode=parent;
            		}
            	}
            	else{
            		currentNode.setPriority(Math.min(currentNode.bestForgottenSuccessorPriority,chooseLowestCostChild(currentNode).getPriority()));
            		while(bmap.get(currentNode)!=null){
	            		Board parent = bmap.get(currentNode);
	            		if(parent.getPriority()>currentNode.getPriority()){
	            			parent.setPriority(currentNode.getPriority());
	            		}
	            		currentNode=parent;
            		}
            	}
            }
            
            
            System.out.println("queue.size "+queue.size());
            
            //if number of nodes in a queue reaches the maxdepth value - remove the least promissing leaf
            if(queue.size()==maxDepth){
            	System.out.println("max depth reached");
            	//forgetLeaf(leafList,queue,bmap);
            	forgetLeaf(queue,bmap);
            	
            }        
        }
        
       
		


    }
	
	public static boolean checkIfDone(Queue<Board> queue){
		for(Board b : queue){
			if(b.successorsState.size()!=b.calculatePossibleSteps())
			{
				return false;
			}
		}
		return true;
	}
	
	public static boolean checkIfAllSuccessorsBlocked(Board boardParent){
		for(State state : boardParent.successorsState.values()){
			if(state!=State.blocked) return false;
		}
		return true;
	}
	
	public static Queue<Board> getLeafs(Queue<Board> queue){
		Queue<Board> leafs=new PriorityQueue<>(new SMAComparator().reversed());
		for(Board b : queue){
			if(b.successors.size()==0){
				leafs.add(b);
			}
		}
		return leafs;
	}
	
	public static void forgetLeaf(Queue<Board> queue, Map<Board,Board> bmap){
		//jak najwiekszy depth, jak najwieksze priority
		//System.out.println("leaflist size" + leafList.size());
//		for(Board b : leafList){
//    		b.printBoard();
//    		System.out.println("priority " + b.getPriority());
//    		System.out.println("successors number in queue " +b.successors.size());
//    		System.out.println("depth " + b.depth);
//    	}
    	Board badNode = getLeafs(queue).remove();
    	queue.remove(badNode);
    	System.out.println("queue.size after remove" + queue.size());
    	Board badNodeParent = bmap.get(badNode);
    	//if not yet set or bigger than new one
    	System.out.println("leafs " + getLeafs(queue).size());
    	System.out.println("bad node depth "+badNode.depth);
    	
    	if(badNodeParent!=null){
	    	System.out.println("badnodeparent depth "+badNodeParent.depth);
	    	System.out.println("badnodeparent best forgotten "+badNodeParent.bestForgottenSuccessorPriority);
	    	if(badNodeParent.bestForgottenSuccessorPriority==-1 || badNodeParent.bestForgottenSuccessorPriority>badNode.getPriority()){
	    		badNodeParent.bestForgottenSuccessorPriority=badNode.getPriority();
	    	}
	    	badNodeParent.successors.remove(badNode);
	    	badNodeParent.successorsState.put(badNode, State.forgotten);
	    	
	    	if(badNodeParent.successors.isEmpty()){
	    		//leafList.add(badNodeParent);
	    	}
	
	    	/*if (!queue.contains(badNodeParent)) {
	    		queue.add(badNodeParent); 
	    		System.out.println("added p");
	    	}*/
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


	 public static Optional<Board> evaluateAndAddIfPresent(Optional<Board> b, Board prev, Map map, Queue<Board> queue, String heuristic){//, Queue<Board> leafList) {
	        if (b.isPresent()) {
	            Board board = b.get();
	            if (!map.containsKey(board)) {
	                board.setPriority(Evaluator.evaluate(board, heuristic) + board.getStepsFromOriginal()); //A star magic happens here
	                map.put(b.get(), prev);
	                //queue.add((b.get()));
	               // prev.successors.add(board);
	                System.out.println("successor added");
	                //leafList.remove(prev);
//	                leafList.add(board);
	                board.depth=prev.depth+1;
	            }    
	        }
	        return b;
	    }
	 
	 public static Board setSuccessorPriority(Order order, int maxDepth, Board parentBoard, Map bmap, Queue<Board> queue, String heuristic){//, Queue<Board> leafList){
		 Optional<Board> successorOpt = evaluateAndAddIfPresent(parentBoard.move(order), parentBoard, bmap, queue, heuristic);//,leafList);
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
