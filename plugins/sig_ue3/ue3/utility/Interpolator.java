package ue3.utility;

import ue3.utility.Image2D;

/**
 * Interpolator
 * Base class for the different interpolators
 */
public abstract class Interpolator {
	public abstract int getInterpolatedValue(Image2D image, double posX, double posY);
}
