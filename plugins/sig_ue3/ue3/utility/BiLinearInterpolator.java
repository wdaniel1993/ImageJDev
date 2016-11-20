package ue3.utility;

import ue3.utility.Image2D;

public class BiLinearInterpolator extends Interpolator {
	
	public int getInterpolatedValue(Image2D image, double posX, double posY){
		if((posX >= 0) && (posY >= 0) && (posX < image.getWidth()) && (posY < image.getHeight())) {
			//get surrounding indexes of the position
			int ceilX = (int) Math.ceil(posX);
			int ceilY = (int) Math.ceil(posY);
			int floorX = (int) Math.floor(posX);
			int floorY = (int) Math.floor(posY);
			//get sub images surrounding the point
			Image2D valuesForTransform = image.getSubImageByIndizes(floorX, floorY, ceilX, ceilY);
			//get interpolated value by using the surrounding points
			return calculateInterpolatedValue(posX - floorX, posY - floorY, valuesForTransform);
		} else {
			return 255;
		}
	}

	protected int calculateInterpolatedValue(double relativeX, double relativeY, Image2D valuesForTransform) {
		double[] interpolationRows = new double[valuesForTransform.getHeight()];
		
		//Iterate over the rows and interpolate the values
		for(int y = 0; y < valuesForTransform.getHeight(); y ++){
			double interpolatedColumn = 0;
			if(valuesForTransform.getWidth() == 1){
				interpolatedColumn = valuesForTransform.get(0,y);
			}else{
				interpolatedColumn = valuesForTransform.get(0,y) * (1 - relativeX) + valuesForTransform.get(1,y) * relativeX;
			}
			interpolationRows[y] = interpolatedColumn;
		}
		//Interpolate the (max) 2 results for the rows
		if(valuesForTransform.getHeight() == 1){
			return (int) (interpolationRows[0] + 0.5);
		}else{
			return (int) ((interpolationRows[0] * (1 - relativeY) + interpolationRows[1] * relativeY) + 0.5);
		}
	}

}
