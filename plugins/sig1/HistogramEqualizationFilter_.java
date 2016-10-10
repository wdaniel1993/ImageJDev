import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import ij.gui.GenericDialog;
import utility.Image2D;
import utility.Point;

public class HistogramEqualizationFilter_ extends AbstractBaseFilter {

	@Override
	public void processImage(Image2D inputImage, Image2D outputImage) {
		final Iterator<Point<Integer>> pointIterator = inputImage.pointIterator();
		final Iterator<Integer> intIterator = inputImage.iterator();
	
		int max = 255;
		int min = 0;
		int count = 0;
		Map<Integer,Integer> map = new HashMap<Integer,Integer>();
		
		while (intIterator.hasNext()){
			Integer val = intIterator.next();
			if(!map.containsKey(val)){
				map.put(val, 1);
			}else{
				map.put(val, map.get(val) + 1);
			}
			count++;
		}
		
		int factor = max - min + 1;
		
		while (pointIterator.hasNext()) {
			Point<Integer> current = pointIterator.next();
			double sum = 0;
			
			for(int i=0;i<=current.getValue();i++){
				if(map.containsKey(i)){
					sum += (map.get(i) / (double) count);
				}
			}
			int newVal = ((int) (sum * factor)) + min;
			
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
