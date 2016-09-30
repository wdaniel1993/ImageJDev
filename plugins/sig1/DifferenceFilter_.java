import java.util.Iterator;
import utility.Image2D;
import utility.Point;

public class DifferenceFilter_ extends AbstractCompareFilter{

	public String getFilterName() {
		return "difference filter";
	}


	@Override
	public void compareImage(Image2D inputImage, Image2D pluginImage, Image2D outputImage) {
		Iterator<Point<Integer>> inputIterator = inputImage.pointIterator();
		Iterator<Point<Integer>> pluginIterator = pluginImage.pointIterator();
		
		while (inputIterator.hasNext()) {
			
			Point<Integer> inputPoint = inputIterator.next();
			Point<Integer> pluginPoint = pluginIterator.next();
			
			Integer newValue = Math.abs(inputPoint.getValue() - pluginPoint.getValue());

			outputImage.set(inputPoint.getX(), inputPoint.getY(), newValue);
		}
	}

	

}
