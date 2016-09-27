
public class MeanFilter_ extends AbstractMaskFilter {

	@Override
	protected String getFilterName() {
		// TODO Auto-generated method stub
		return null;
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
