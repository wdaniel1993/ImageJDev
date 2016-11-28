package ue4.utility;

import java.awt.Point;
import java.util.Stack;

public class SegmentationUtility {
	
	private static int BG_VAL = 0;
	private static int FG_VAL = 255;

	public static int[][] segmentRegionGrowing(int width, int height, int[][] inArr, int startX, int startY, boolean n4, int lowerThresh, int upperThresh) {
		int[][] outArr = new int[width][height];
		
		for(int x = 0; x < width; x++){
			for(int y = 0; y <height; y++){
				outArr[x][y] = BG_VAL;
			}
		}
		
		//user defined seed position
		Stack<Point> stack = new Stack<Point>();
		stack.push(new Point(startX, startY));
		outArr[startX][startY] = FG_VAL;
		
		while(!stack.empty()){
			Point curPosition = stack.pop();
			
			
			//search neighbours
			
			if(n4){
				//n4 Adjacency
				for(int xOffset = -1; xOffset <= 1; xOffset++){
					int nbX = curPosition.x + xOffset;
					int nbY = curPosition.y;
					
					//range check
					if(isForegroundPoint(width, height, inArr, outArr, nbX, nbY, lowerThresh, upperThresh)){
						stack.push(new Point(nbX,nbY));
					}
				}
				for(int yOffset = -1; yOffset <= 1; yOffset++){
					int nbX = curPosition.x;
					int nbY = curPosition.y + yOffset;
					
					//range check
					if(isForegroundPoint(width, height, inArr, outArr, nbX, nbY, lowerThresh, upperThresh)){
						stack.push(new Point(nbX,nbY));
					}
				}
			}else{
				//n8 Adjacency
				for(int xOffset = -1; xOffset <= 1; xOffset++){
					for(int yOffset = -1; yOffset <= 1; yOffset++){
						int nbX = curPosition.x + xOffset;
						int nbY = curPosition.y + yOffset;
						
						//range check
						if(isForegroundPoint(width, height, inArr, outArr, nbX, nbY, lowerThresh, upperThresh)){
							stack.push(new Point(nbX,nbY));
						}
					}
				}
			}
			
		}
		return outArr;
		}
		
	private static boolean isForegroundPoint(int width, int height, int[][] inArr, int[][] outArr, int nbX, int nbY, int lowerThresh, int upperThresh) {
		if(nbX >= 0 && nbX < width && nbY >= 0 && nbY < height){
			int nbVal = inArr[nbX][nbY];
			if(nbVal >= lowerThresh && nbVal <= upperThresh && outArr[nbX][nbY] == BG_VAL){
				outArr[nbX][nbY] = FG_VAL;
				return true;
			}
		}
		return false;
	}
}
