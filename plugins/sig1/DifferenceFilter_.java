import java.util.Iterator;
import java.util.NoSuchElementException;

import filter.AbstractBaseFilter;
import filter.AbstractMaskFilter;
import ij.IJ;
import ij.ImagePlus;
import ij.gui.GenericDialog;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;
import utility.ByteImage2D;
import utility.Image2D;
import utility.Image2DUtility;
import utility.ImageJUtility;
import utility.Point;

public class DifferenceFilter_ implements PlugInFilter{

	@Override
	public void run(ImageProcessor ip) {
		byte[] pixels = (byte[]) ip.getPixels();
		int width = ip.getWidth();
		int height = ip.getHeight();

		final Image2D inputImage = new ByteImage2D(pixels, width, height);
		final Image2D outputImage = new ByteImage2D(pixels, width, height);
		final Image2D differenceImage = new ByteImage2D(pixels, width, height);
		
		AbstractBaseFilter plugin = new GaussFilter_();
		GenericDialog gd = new GenericDialog("User Input");
		plugin.prepareDialog(gd);
		gd.showDialog();
		if (gd.wasCanceled()) {
			return;
		}
		plugin.readDialogResult(gd);
		plugin.processImage(inputImage, outputImage);
		
		Iterator<Point<Integer>> inputIterator = inputImage.pointIterator();
		Iterator<Point<Integer>> outputIterator = outputImage.pointIterator();
		
		while (inputIterator.hasNext()) {
			
			Point<Integer> inputPoint = inputIterator.next();
			Point<Integer> outputPoint = outputIterator.next();
			
			Integer newValue = Math.abs(inputPoint.getValue() - outputPoint.getValue());

			differenceImage.set(inputPoint.getX(), inputPoint.getY(), newValue);
		}
		
		byte[] outPixels = Image2DUtility.convertFromImage2D(outputImage);
		ImageJUtility.showNewImage(outPixels, width, height,plugin.getFilterName());
		
		byte[] diffPixels = Image2DUtility.convertFromImage2D(differenceImage);
		ImageJUtility.showNewImage(diffPixels, width, height,"difference image");
	}

	@Override
	public int setup(String arg, ImagePlus imp) {
		if (arg.equals("about"))
			{showAbout(); return DONE;}
		return DOES_8G+SUPPORTS_MASKING;
	}
	
	private void showAbout() {
		IJ.showMessage("About difference filter...", "this is a PluginFilter template\n");
	}

}
