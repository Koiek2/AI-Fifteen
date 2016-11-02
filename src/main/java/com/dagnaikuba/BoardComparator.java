package com.dagnaikuba;

import java.util.Comparator;

public class BoardComparator implements Comparator<Board> {

    public int compare(Board x, Board y) {
        if (x.getPriority() != y.getPriority()) return x.getPriority() - y.getPriority(); //most cases

        else if (x.emptyX != y.emptyX) {
            return x.emptyX - y.emptyX;
        }
        else return x.emptyY - y.emptyY;


    }

}
