package com.dagnaikuba.heuristics;

import com.dagnaikuba.Board;

public class Manhattan_distance {
	public static int evaluate(Board b){
		int wantedPositionX;
		int wantedPositionY;
		int distanceX;
		int distanceY;
		int manhattanDistanceSum = 0;

        int height = b.getHeight();
        int width = b.getWidth();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
            	int value = b.tiles[i][j];
                if(value!=0){
                	wantedPositionX = (value-1)/height;
                	wantedPositionY = (value-1)%width;
                	distanceX = i - wantedPositionX;
                	distanceY = j - wantedPositionY;
                	manhattanDistanceSum += Math.abs(distanceX) + Math.abs(distanceY); 
                }
                
            }
        }
        return manhattanDistanceSum;
    }
}
