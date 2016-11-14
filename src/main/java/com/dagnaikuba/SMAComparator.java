package com.dagnaikuba;

import java.util.Comparator;


public class SMAComparator implements Comparator<Board> {
	public int compare(Board x, Board y){
		int costX,costY;
		if(x.successorsState.size()==x.calculatePossibleSteps()){
			costX=x.bestForgottenSuccessorPriority;
		}
		else{
			costX=x.getPriority();
		}
		if(y.successorsState.size()==y.calculatePossibleSteps()){
			costY=y.bestForgottenSuccessorPriority;
		}
		else{
			costY=y.getPriority();
		}
		if (costX != costY) return costX - costY; //most cases
		else{
			if(x.depth>y.depth){
				return y.depth-x.depth;
			}
			else{
				return x.depth-y.depth;
			}
		}
    }
	
	
}
