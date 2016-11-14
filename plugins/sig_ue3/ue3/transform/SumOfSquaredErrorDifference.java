package ue3.transform;

import java.util.Iterator;

import ue3.utility.Image2D;
import ue3.utility.Point;

public class SumOfSquaredErrorDifference implements ImageDifference{

	@Override
	public double calculateDifference(Image2D image1, Image2D image2) {
		double errorVal = 0.0;
		Iterator<Point<Integer>> iterator = image1.pointIterator();
		
		while(iterator.hasNext()){
			Point<Integer> currentPoint = iterator.next();
			int x = currentPoint.getX();
			int y = currentPoint.getY();
			if(image1.get(x, y) !=  image2.get(x, y)) {
				errorVal++;
			}
		}

		return errorVal;
	}
	
	@Override
	public String getName() {
		return "Sum Of Squared Error Difference";
	}
}
