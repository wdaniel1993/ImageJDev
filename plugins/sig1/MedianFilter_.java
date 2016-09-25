import java.util.Collections;
import java.util.List;


public class MedianFilter_ extends AbstractMaskFilter{

	@Override
	protected Integer transformImagePoint(Integer pixel, Image2D mask) {
		int result = 0;
		
		List<Integer> pixels = mask.asList();
		Collections.sort(pixels);
	
		int size = pixels.size();
		
		if(size % 2 == 0){
			result = (int)(((pixels.get(size/2) + pixels.get((size/2)+1)) / 2.0) + 0.5);
		}else{
			result = pixels.get((size+1)/2);
		}
		
		return result;
	}

	@Override
	protected String getFilterName() {
		return "median filter";
	}

}
