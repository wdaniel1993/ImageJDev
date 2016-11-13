package ue3.transform;

import ue3.utility.Image2D;

public class MutualInformationDifference implements ImageDifference{

	@Override
	public double calculateDifference(Image2D image1, Image2D image2) {
		return getEntropyOfImg(image1) + getEntropyOfImg(image2)
		- getEntropyOfImages(image1, image2);
	}

	private int getEntropyOfImages(Image2D image1, Image2D image2) {
		// TODO Auto-generated method stub
		return 0;
	}

	private int getEntropyOfImg(Image2D image1) {
		// TODO Auto-generated method stub
		return 0;
	}

}
