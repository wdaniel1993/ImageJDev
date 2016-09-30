import utility.Image2D;

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
