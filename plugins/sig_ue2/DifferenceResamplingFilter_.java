import java.util.Iterator;

import ue2.utility.ByteImage2D;
import ue2.utility.Image2D;
import ue2.utility.Point;

/**
 * DifferenceFilter
 * Displays the difference of the original and the filtered image by calculating the the (absolute) difference between the points
 */
public class DifferenceResamplingFilter_ extends AbstractComparerFilter{

	public String getFilterName() {
		return "difference filter";
	}


	@Override
	public Image2D compareImage(Image2D image1, Image2D image2) {
		Image2D outputImage = new ByteImage2D(image1.getWidth(),image1.getHeight());
		Iterator<Point<Integer>> inputIterator = image1.pointIterator();
		Iterator<Point<Integer>> pluginIterator = image2.pointIterator();
		
		//Iterate over every point of the pictures
		while (inputIterator.hasNext()) {
			
			Point<Integer> inputPoint = inputIterator.next();
			Point<Integer> pluginPoint = pluginIterator.next();
			
			//Absolute difference between the points in the original and the filtered image
			Integer newValue = Math.abs(inputPoint.getValue() - pluginPoint.getValue());

			outputImage.set(inputPoint.getX(), inputPoint.getY(), newValue);
		}
		return outputImage;
	}
}
