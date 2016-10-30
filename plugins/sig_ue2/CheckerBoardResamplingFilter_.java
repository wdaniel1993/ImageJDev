import java.util.Iterator;

import ue2.utility.ByteImage2D;
import ue2.utility.Image2D;
import ue2.utility.Point;

/**
 * CheckerBoardFilter
 * Views the original and the filtered image in a checkerboard presentation next to each other
 */
public class CheckerBoardResamplingFilter_ extends AbstractComparerFilter{

	private int blockCount = 4;
	
	@Override
	public String getFilterName() {
		return "checker board filter";
	}

	@Override
	public Image2D compareImage(Image2D image1, Image2D image2) {
		Image2D outputImage = new ByteImage2D(image1.getWidth(),image1.getHeight());
		int blockSize = image1.getWidth() / blockCount;
	
		/*
		 * Iterators for the pictures, must be the same size
		 */
		Iterator<Point<Integer>> inputIterator = image1.pointIterator();
		Iterator<Point<Integer>> pluginIterator = image2.pointIterator();
		
		while (inputIterator.hasNext()) {
			
			Point<Integer> inputPoint = inputIterator.next();
			Point<Integer> pluginPoint = pluginIterator.next();
			
			Integer newValue;
			
			int x = inputPoint.getX();
			int y = inputPoint.getY();
			
			/*
			 * Depending on the x,y position, views a point from the filtered or the unfiltered picture
			 */
			if(((x+1) % blockSize) == 0){
				newValue = 0;
			}else if(((y+1) % blockSize) == 0){
				newValue = 0;
			}else if(((x / blockSize) + (y / blockSize)) % 2 == 0 ){
				newValue = inputPoint.getValue();
			}else{
				newValue = pluginPoint.getValue();
			}

			outputImage.set(inputPoint.getX(), inputPoint.getY(), newValue);
		}
		return outputImage;
	}
}
