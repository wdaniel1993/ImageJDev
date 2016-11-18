package ue3.utility;

import java.util.Iterator;

public class ThresholdUtility {
	public static Image2D binaryThreshold(Image2D image, int intensitySplit, boolean bgWhite) {
		Image2D outputImage = new ByteImage2D(image.getWidth(), image.getHeight());
		
		for(Iterator<Point<Integer>> it = image.pointIterator();it.hasNext();){
			Point<Integer> point = it.next();
			if(point.getValue() < intensitySplit){
				outputImage.set(point.getX(), point.getY(), bgWhite ? 255 : 0);
			}else {
				outputImage.set(point.getX(), point.getY(), bgWhite ? 0 : 255);
			}
		}
		
		return outputImage;
		
	}
}
