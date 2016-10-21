import ij.IJ;
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
			indexX = (int) Math.round(relativeY);
		}
		try{
			return valuesForTransform.get(indexX, indexY);
		}catch(RuntimeException ex){
			IJ.showMessage(relativeX + " -  " + relativeY + " -  " + indexX + " -  " + indexY);
			throw ex;
		}
		
	}

	@Override
	protected String getFilterName() {
		return "nearest neighbour interpolation";
	}

}
