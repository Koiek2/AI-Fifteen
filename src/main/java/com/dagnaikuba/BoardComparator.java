package com.dagnaikuba;

import java.util.Comparator;

public class BoardComparator implements Comparator<Board>{

    public int compare(Board x, Board y){
        return (x.getPriority()-y.getPriority()); //only matters if it is negative, positive or 0; the difference is redundant
    }

}
