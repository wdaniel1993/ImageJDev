package ue3.utility;

import ue3.utility.Image2D;

public abstract class Interpolator {
	public int getInterpolatedValue(Image2D image, double posX, double posY){
		if((posX >= 0) && (posY >= 0) && (posX < image.getWidth()) && (posY < image.getHeight())) {
			int ceilX = (int) Math.ceil(posX);
			int ceilY = (int) Math.ceil(posY);
			int floorX = (int) Math.floor(posX);
			int floorY = (int) Math.floor(posY);
			Image2D valuesForTransform = image.getSubImageByIndizes(floorX, floorY, ceilX, ceilY);
			return calculateInterpolatedValue(posX - floorX, posY - floorY, valuesForTransform);
		} else {
			return 255;
		}
	}
	
	protected abstract int calculateInterpolatedValue(double relativeX, double relativeY, Image2D valuesForTransform);
}
