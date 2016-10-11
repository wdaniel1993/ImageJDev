import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import ij.gui.GenericDialog;
import utility.Image2D;
import utility.Point;

/**
 * Implementation of a history equalization filter
 *
 */
public class HistogramEqualizationFilter_ extends AbstractBaseFilter {

	@Override
	public void processImage(Image2D inputImage, Image2D outputImage) {
		final Iterator<Point<Integer>> pointIterator = inputImage.pointIterator();
		final Iterator<Integer> intIterator = inputImage.iterator();
	
		int max = 255;
		int min = 0;
		int count = 0;
		Map<Integer,Integer> mapFrequency = new HashMap<Integer,Integer>();
		Map<Integer,Integer> mapValueTransform = new HashMap<Integer,Integer>();
		
		//get count for each pixel value by writing them in a map
		while (intIterator.hasNext()){
			Integer val = intIterator.next();
			if(!mapFrequency.containsKey(val)){
				mapFrequency.put(val, 1);
			}else{
				mapFrequency.put(val, mapFrequency.get(val) + 1);
			}
			count++;
		}
		
		/*
		 * calculate mapping old values <-> new values
		 */
		int factor = max - min + 1;
		double sum = 0;
		int lastPoint = 0;
		
		for(int i = min; i <= max; i++){
			for(;lastPoint<=i;lastPoint++){
				if(mapFrequency.containsKey(lastPoint)){
					sum += (mapFrequency.get(lastPoint) / (double) count);
				}
			}
			int transformVal = ((int) (sum * factor)) + min;
			mapValueTransform.put(i, transformVal);
		}
		
		
		
		/*
		 * Iterate over the image and transform each value by considering the frequency of the value
		 */
		while (pointIterator.hasNext()) {
			Point<Integer> current = pointIterator.next();

			int newVal = mapValueTransform.get(current.getValue());
			
			outputImage.set(current.getX(), current.getY(), newVal);
		}
	}

	@Override
	public String getFilterName() {
		return "histogram equalization filter";
	}

	@Override
	public void readDialogResult(GenericDialog gd) {	}

	@Override
	public void prepareDialog(GenericDialog gd) {	}
	
	public void inputDialog(){}

}
