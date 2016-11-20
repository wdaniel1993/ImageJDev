package ue3.transform;

import ue3.utility.Image2D;

/**
 * ImageDifference
 * Interface for difference calculation of two images
 */
public interface ImageDifference {
	double calculateDifference(Image2D image1, Image2D image2);
	String getName();
}