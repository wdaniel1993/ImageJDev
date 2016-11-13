package ue3.transform;

import java.util.Iterator;

import ue3.utility.ByteImage2D;
import ue3.utility.Image2D;
import ue3.utility.Interpolator;
import ue3.utility.Point;

public class TransformHelper {
	public static Image2D transformImage(Image2D image, double transX, double transY, double rotAngle, Interpolator interpolator){
		int width = image.getWidth();
		int height = image.getHeight();
		Image2D retImage = new ByteImage2D(width,height);
		Iterator<Point<Integer>> iterator = image.pointIterator();

		// calc center position

		// rotation angle degree ==> radian

		double radAngle = -rotAngle / 180 * Math.PI;
		double cosTheta = Math.cos(radAngle);
		double sinTheta = Math.sin(radAngle);
		
		double midX = width / 2.0;
		double midY = height / 2.0;

		// backprojekt all pixels of result image B
		while(iterator.hasNext()){
			Point<Integer> currentPoint = iterator.next();
			int x = currentPoint.getX();
			int y = currentPoint.getY();
			
			// move coordinates to center
			double posX = x - midX;
			double posY = y - midY;

			// rotate
			double newX = posX * cosTheta + posY * sinTheta;
			double newY = -posX * sinTheta + posY * cosTheta;

			//move coordinates back from center
			newX = newX + midX;
			newY = newY + midY;
			
			// translate
			posX = newX - transX;
			posY = newY - transY;

			int scalarVal = interpolator.getInterpolatedValue(image, posX, posY);
			
			retImage.set(x, y, scalarVal);				
		}

		return retImage;
	}
}
