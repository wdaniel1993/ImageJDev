import ue2.utility.Image2D;

/**
 * NearestNeighbourResamplingFilter
 * Resamples the image acccording to a resize factor with nearest neighbour interpolation as base for the calculation of new values
 *
 */
public class NearestNeighbourFilter_ extends AbstractResamplingFilter{

	/*
	 * Calculate the value with the surrounding values and the relative position to the surrounding values
	 */
	@Override
	protected int resamplePoint(double relativeX, double relativeY, Image2D valuesForTransform) {
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

	@Override
	protected String getFilterName() {
		return "nearest neighbour interpolation";
	}

}
