import java.util.Iterator;

import ue1.utility.Image2D;
import ue1.utility.Point;

/**
 * InvertFilter
 * Low values become high values and vice versa
 *
 */
public class InvertFilter_ extends AbstractBaseFilter {

	@Override
	public void processImage(Image2D inputImage, Image2D outputImage) {
		final Iterator<Point<Integer>> pointIterator = inputImage.pointIterator();
		
		/*
		 * Iterate over the image and transform each value by considering the frequency of the value
		 */
		while (pointIterator.hasNext()) {
			Point<Integer> current = pointIterator.next();

			int newVal = 255 - current.getValue();
			
			outputImage.set(current.getX(), current.getY(), newVal);
		}
	}

	@Override
	public String getFilterName() {
		return "invert filter";
	}
}