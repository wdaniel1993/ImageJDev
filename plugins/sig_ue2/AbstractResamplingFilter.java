import java.util.Iterator;

import ij.IJ;
import ij.ImagePlus;
import ij.gui.GenericDialog;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;
import ue2.utility.ByteImage2D;
import ue2.utility.Image2D;
import ue2.utility.Image2DUtility;
import ue2.utility.ImageJUtility;
import ue2.utility.Point;

/**
 * Base filter for 
 *
 */
public abstract class AbstractResamplingFilter implements PlugInFilter {

	@Override
	public void run(ImageProcessor ip) {
		byte[] pixels = (byte[]) ip.getPixels();
		int width = ip.getWidth();
		int height = ip.getHeight();

		//read resize factor
		GenericDialog gd = new GenericDialog("User Input");
		gd.addNumericField("Resize factor", 2, 1);
		gd.showDialog();
		if (gd.wasCanceled()) {
			return;
		}
		double resizeFactor = gd.getNextNumber();
		
		// abort if factor below or equal to 0 or bigger then 1
		if(resizeFactor <= 0 || resizeFactor > 10){
			return;
		}
		
		final Image2D inputImage = new ByteImage2D(pixels, width, height);

		//Calls abstract method, which processes the input image and writes changes into the output image
		Image2D outputImage = resizeImage(inputImage, resizeFactor);
		
		byte[] outPixels = Image2DUtility.convertFromImage2D(outputImage);
		ImageJUtility.showNewImage(outPixels, outputImage.getWidth(), outputImage.getHeight(),getFilterName());
		
	}
	
	

	/*
	 * Method which resizes the image by the given resize factor
	 */
	public Image2D resizeImage(Image2D inputImage, double resizeFactor) {
		final Image2D outputImage = new ByteImage2D((int) (inputImage.getWidth() * resizeFactor), (int) (inputImage.getHeight() * resizeFactor));
		final Iterator<Point<Integer>> outputIterator = outputImage.pointIterator();
		
		//iterate over output image
		while(outputIterator.hasNext()){
			Point<Integer> currentPoint = outputIterator.next();
			//Calculate coordinate of point in new image
			double transformedX = transformCoordinate(currentPoint.getX(),inputImage.getWidth(),outputImage.getWidth());
			double transformedY = transformCoordinate(currentPoint.getY(),inputImage.getHeight(),outputImage.getHeight());
			
			//surround indexes
			int ceilX = (int) Math.ceil(transformedX);
			int ceilY = (int) Math.ceil(transformedY);
			int floorX = (int) Math.floor(transformedX);
			int floorY = (int) Math.floor(transformedY);
			Image2D valuesForTransform = inputImage.getSubImageByIndizes(floorX, floorY, ceilX, ceilY);
			
			//calculate the value for the given coordinate
			int newVal = resamplePoint(transformedX - floorX, transformedY - floorY, valuesForTransform);
			outputImage.set(currentPoint.getX(),currentPoint.getY(), newVal);	
		}
		
		return outputImage;
	}


	protected abstract int resamplePoint(double relativeX, double relativeY, Image2D valuesForTransform);


	/*
	 * Transformation of the coordinate - full overlapping of the pixel representation
	 */
	private double transformCoordinate(int coordinate, int lengthOriginal, int lengthNew){
		double r = 1.0 / (2*lengthOriginal);
		double rb = (2* (double)coordinate +1)/(2*lengthNew);
		return (rb -r) * lengthOriginal;
	}


	@Override
	public int setup(String arg, ImagePlus imp) {
		if (arg.equals("about")) {
			showAbout();
			return DONE;
		}
		return DOES_8G + SUPPORTS_MASKING;
	}
	
	private void showAbout() {
		IJ.showMessage("About " + getFilterName() + "...", "this is a PluginFilter template\n");
	}

	protected abstract String getFilterName();

}
