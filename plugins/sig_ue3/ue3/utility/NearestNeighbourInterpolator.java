package ue3.utility;

import ue3.utility.Image2D;

public class NearestNeighbourInterpolator extends Interpolator {

	@Override
	public int getInterpolatedValue(Image2D image, double posX, double posY){
		if((posX >= 0) && (posY >= 0) && (posX < image.getWidth()) && (posY < image.getHeight())) {
			int indexX = (int) Math.round(posX);
			int indexY = (int) Math.round(posY);
			
			return image.get(indexX, indexY);
		} else {
			return 255;
		}
	}
}
