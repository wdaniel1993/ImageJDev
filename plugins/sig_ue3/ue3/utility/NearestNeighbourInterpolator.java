package ue3.utility;

import ue2.utility.Image2D;

public class NearestNeighbourInterpolator extends Interpolator {

	@Override
	protected int calculateInterpolatedValue(double relativeX, double relativeY, Image2D valuesForTransform) {
		//Round relative position to get the nearest coordinate in valuesForTransform and return the value
		int indexX = 0;
		int indexY = 0;
		if(!(valuesForTransform.getWidth() == 1)){
			indexX = (int) Math.round(relativeX);
		}
		if(!(valuesForTransform.getHeight() == 1)){
			indexY = (int) Math.round(relativeY);
		}
		return valuesForTransform.get(indexX, indexY);
	}

}
