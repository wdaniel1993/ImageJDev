import ue2.utility.Image2D;

public class NearestNeighbourFilter_ extends AbstractResamplingFilter{

	@Override
	protected int resamplePoint(double relativeX, double relativeY, Image2D valuesForTransform) {
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
