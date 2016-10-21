import ue1.utility.Image2D;

/**
 * Mean Filter
 * Calculates the mean of all values in the mask and returns it as new value for the given x,y position
 */
public class MeanFilter_ extends AbstractMaskFilter {

	@Override
	public String getFilterName() {
		// TODO Auto-generated method stub
		return "mean filter";
	}

	@Override
	protected int transformImagePoint(int x, int y, Image2D mask) {
		double sum = 0;
		for(Integer val : mask){
			sum += val.doubleValue() / mask.getPointCount();
		}
		return (int) (sum + 0.5);
	}

}
