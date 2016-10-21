import java.util.Iterator;

import ij.gui.GenericDialog;
import ue1.utility.Image2D;
import ue1.utility.Point;

/**
 * CheckerBoardFilter
 * Views the original and the filtered image in a checkerboard presentation next to each other
 */
public class CheckerBoardFilter_ extends AbstractCompareFilter{

	private int blockCount = 4;
	
	@Override
	public String getFilterName() {
		return "checker board filter";
	}

	@Override
	public void compareImage(Image2D inputImage, Image2D pluginImage, Image2D outputImage) {
		int blockSize = inputImage.getWidth() / blockCount;
	
		/*
		 * Iterators for the pictures, must be the same size
		 */
		Iterator<Point<Integer>> inputIterator = inputImage.pointIterator();
		Iterator<Point<Integer>> pluginIterator = pluginImage.pointIterator();
		
		while (inputIterator.hasNext()) {
			
			Point<Integer> inputPoint = inputIterator.next();
			Point<Integer> pluginPoint = pluginIterator.next();
			
			Integer newValue;
			
			int x = inputPoint.getX();
			int y = inputPoint.getY();
			
			/*
			 * Depending on the x,y position, views a point from the filtered or the unfiltered picture
			 */
			if(((x / blockSize) + (y / blockSize)) % 2 == 0 ){
				newValue = inputPoint.getValue();
			}else{
				newValue = pluginPoint.getValue();
			}

			outputImage.set(inputPoint.getX(), inputPoint.getY(), newValue);
		}
	}
	
	@Override
	public void readDialogResult(GenericDialog gd) {
		this.blockCount = (int) gd.getNextNumber();	
		super.readDialogResult(gd);
	}

	@Override
	public void prepareDialog(GenericDialog gd) {
		gd.addNumericField("Block Count:", this.blockCount , 0);
		super.prepareDialog(gd);
	}

}
