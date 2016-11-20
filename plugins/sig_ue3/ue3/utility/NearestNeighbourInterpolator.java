package ue3.utility;

import ue3.utility.Image2D;

public class NearestNeighbourInterpolator extends Interpolator {

	@Override
	public int getInterpolatedValue(Image2D image, double posX, double posY){
		int indexX = (int) Math.round(posX);
		int indexY = (int) Math.round(posY);
		if((indexX >= 0) && (indexY >= 0) && (indexX < image.getWidth()) && (indexY < image.getHeight())) {
			return image.get(indexX, indexY);
		} else {
			return 255;
		}
	}
}
