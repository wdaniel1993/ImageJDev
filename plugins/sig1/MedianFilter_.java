import java.util.Arrays;

import filter.AbstractMaskFilter;
import utility.Image2D;

public class MedianFilter_ extends AbstractMaskFilter{

	@Override
	protected int transformImagePoint(int x, int y, Image2D mask) {
		int result = 0;
		
		int[] pixels = mask.asArray();
		Arrays.sort(pixels);
	
		int size = pixels.length;
		
		if(size % 2 == 0){
			result = (int)(((pixels[size/2 - 1] + pixels[(size/2)]) / 2.0) + 0.5);
		}else{
			result = pixels[(size-1)/2];
		}
		
		return result;
	}

	@Override
	public String getFilterName() {
		return "median filter";
	}

}
