import ue2.utility.Image2D;

public class BiLinearFilter_ extends AbstractResamplingFilter {

	@Override
	protected int resamplePoint(double relativeX, double relativeY, Image2D valuesForTransform) {
		double[] interpolationRows = new double[valuesForTransform.getHeight()];
		for(int y = 0; y < valuesForTransform.getHeight(); y ++){
			double interpolatedColumn = 0;
			if(valuesForTransform.getWidth() == 1){
				interpolatedColumn = valuesForTransform.get(0,y);
			}else{
				interpolatedColumn = valuesForTransform.get(0,y) * (1 - relativeX) + valuesForTransform.get(1,y) * relativeX;
			}
			interpolationRows[y] = interpolatedColumn;
		}
		if(valuesForTransform.getHeight() == 1){
			return (int) (interpolationRows[0] + 0.5);
		}else{
			return (int) ((interpolationRows[0] * (1 - relativeY) + interpolationRows[1] * relativeY) + 0.5);
		}
	}

	@Override
	protected String getFilterName() {
		return "bi linear interpolation";
	}

}
