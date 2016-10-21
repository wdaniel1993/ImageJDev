import java.util.Iterator;

import ue1.utility.Image2D;
import ue1.utility.Point;

/**
 * DifferenceFilter
 * Displays the difference of the original and the filtered image by calculating the the (absolute) difference between the points
 */
public class DifferenceFilter_ extends AbstractCompareFilter{

	public String getFilterName() {
		return "difference filter";
	}


	@Override
	public void compareImage(Image2D inputImage, Image2D pluginImage, Image2D outputImage) {
		Iterator<Point<Integer>> inputIterator = inputImage.pointIterator();
		Iterator<Point<Integer>> pluginIterator = pluginImage.pointIterator();
		
		//Iterate over every point of the pictures
		while (inputIterator.hasNext()) {
			
			Point<Integer> inputPoint = inputIterator.next();
			Point<Integer> pluginPoint = pluginIterator.next();
			
			//Absolute difference between the points in the original and the filtered image
			Integer newValue = Math.abs(inputPoint.getValue() - pluginPoint.getValue());

			outputImage.set(inputPoint.getX(), inputPoint.getY(), newValue);
		}
	}

	

}
