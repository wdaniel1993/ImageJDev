package ue4.utility;

import java.awt.Point;
import java.util.Stack;

/**
 * SegmentationUtility
 * Helper methods for segmentation
 *
 */
public class SegmentationUtility {

	private static int BG_VAL = 0;
	private static int FG_VAL = 255;

	/*
	 * calculate region growing of input image from the start coordinates
	 * returns a new image (output image)
	 */
	public static int[][] segmentRegionGrowing(int width, int height, int[][] inArr, int startX, int startY, boolean n4,
			int lowerThresh, int upperThresh) {
		int[][] outArr = new int[width][height];

		//set all scalar values of the output image to the background value (binary threshold)
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				outArr[x][y] = BG_VAL;
			}
		}

		// user defined seed position
		Stack<Point> stack = new Stack<Point>();
		stack.push(new Point(startX, startY));
		outArr[startX][startY] = FG_VAL;

		while (!stack.empty()) {
			Point curPosition = stack.pop();

			// search neighbours

			if (n4) {
				// n4 Adjacency
				for (int xOffset = -1; xOffset <= 1; xOffset++) {
					int nbX = curPosition.x + xOffset;
					int nbY = curPosition.y;

					// range check
					if (isForegroundPoint(width, height, inArr, outArr, nbX, nbY, lowerThresh, upperThresh)) {
						stack.push(new Point(nbX, nbY));
					}
				}
				for (int yOffset = -1; yOffset <= 1; yOffset++) {
					int nbX = curPosition.x;
					int nbY = curPosition.y + yOffset;

					// range check
					if (isForegroundPoint(width, height, inArr, outArr, nbX, nbY, lowerThresh, upperThresh)) {
						stack.push(new Point(nbX, nbY));
					}
				}
			} else {
				// n8 Adjacency
				for (int xOffset = -1; xOffset <= 1; xOffset++) {
					for (int yOffset = -1; yOffset <= 1; yOffset++) {
						int nbX = curPosition.x + xOffset;
						int nbY = curPosition.y + yOffset;

						// range check
						if (isForegroundPoint(width, height, inArr, outArr, nbX, nbY, lowerThresh, upperThresh)) {
							stack.push(new Point(nbX, nbY));
						}
					}
				}
			}

		}
		return outArr;
	}

	
	/*
	 * checks if the point of the given coordinate is within the threshold
	 * if the value is within the threshold, the scalar value in output picture is set to the defined foreground value
	 */
	private static boolean isForegroundPoint(int width, int height, int[][] inArr, int[][] outArr, int nbX, int nbY,
			int lowerThresh, int upperThresh) {
		//check if coordinates are within the borders
		if (nbX >= 0 && nbX < width && nbY >= 0 && nbY < height) {
			int nbVal = inArr[nbX][nbY];
			//check if scalar value of input picture is within threshold and if the scalar value of the output image is still unset
			if (nbVal >= lowerThresh && nbVal <= upperThresh && outArr[nbX][nbY] == BG_VAL) {
				//set scalar value of the image to the defined foreground value (binary threshold)
				outArr[nbX][nbY] = FG_VAL;
				return true;
			}
		}
		return false;
	}
}
