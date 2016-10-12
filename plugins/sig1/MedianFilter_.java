import java.util.Arrays;

import utility.Image2D;

/**
 * Median Filter
 * Calculates the median of all values in the mask and returns it as new value for the given x,y position
 */
public class MedianFilter_ extends AbstractMaskFilter{

	@Override
	protected int transformImagePoint(int x, int y, Image2D mask) {
		int result = 0;
		
		int[] pixels = mask.asArray();
		Arrays.sort(pixels);
	
		int size = pixels.length;
		
		//If the pixel count is an even number, the median is defined as the mean average of two middle values
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
