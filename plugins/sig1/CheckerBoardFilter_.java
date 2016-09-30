import java.util.Iterator;

import ij.gui.GenericDialog;
import utility.Image2D;
import utility.Point;

public class CheckerBoardFilter_ extends AbstractCompareFilter{

	private int blockCount = 4;
	
	@Override
	public String getFilterName() {
		return "checker board filter";
	}

	@Override
	public void compareImage(Image2D inputImage, Image2D pluginImage, Image2D outputImage) {
		int blockSize = inputImage.getWidth() / blockCount;
				
		Iterator<Point<Integer>> inputIterator = inputImage.pointIterator();
		Iterator<Point<Integer>> pluginIterator = pluginImage.pointIterator();
		
		while (inputIterator.hasNext()) {
			
			Point<Integer> inputPoint = inputIterator.next();
			Point<Integer> pluginPoint = pluginIterator.next();
			
			Integer newValue;
			
			int x = inputPoint.getX();
			int y = inputPoint.getY();
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
		super.readDialogResult(gd);
		this.blockCount = (int) gd.getNextNumber();		
	}

	@Override
	public void prepareDialog(GenericDialog gd) {
		super.prepareDialog(gd);
		gd.addNumericField("Block Count:", this.blockCount , 0);
	}

}
