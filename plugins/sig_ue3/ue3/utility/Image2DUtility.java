package ue3.utility;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ij.ImagePlus;
import ij.process.ByteProcessor;
import ij.process.ImageProcessor;
import ue3.utility.ByteImage2D;
import ue3.utility.Point;
import ue3.utility.Image2D;


/**
 * Utility class for Image2D
 * converts the Image2D into a byte array
 */
public class Image2DUtility {

	
	public static byte[] convertFromImage2D(Image2D image) {
		byte[] outArray2D = new byte[image.getPointCount()];

		int pixelIdx1D = 0;
		for (Iterator<Integer> it = image.iterator(); it.hasNext(); ) {
			int resultVal = it.next();
			if (resultVal > 127) {
				resultVal -= 256;
			}
			outArray2D[pixelIdx1D] = (byte) resultVal;
			pixelIdx1D++;
		}

		return outArray2D;
	}
	
	
	public static ImagePlus toImagePlus(Image2D image, String title) {
		ImageProcessor outImgProc = toImageProcessor(image);
		ImagePlus ip = new ImagePlus(title, outImgProc);
		return ip;
	}
	
	public static Image2D fromImagePlus(ImagePlus imp) {
		ImageProcessor ip = imp.getProcessor();
		return fromImageProcessor(ip);
	}
	
	public static ByteProcessor toImageProcessor(Image2D image) {
		byte[] outPixels = Image2DUtility.convertFromImage2D(image);
		ByteProcessor outImgProc = new ByteProcessor(image.getWidth(), image.getHeight());
		outImgProc.setPixels(outPixels);
		return outImgProc;
	}
	
	public static Image2D fromImageProcessor(ImageProcessor ip) {
		byte[] pixels = (byte[]) ip.getPixels();
		int width = ip.getWidth();
		int height = ip.getHeight();

		return new ByteImage2D(pixels, width, height);
	}
	
	public static void showImage2D(Image2D image, String windowMessage) {
		byte[] outPixels = Image2DUtility.convertFromImage2D(image);
		ImageJUtility.showNewImage(outPixels, image.getWidth(), image.getHeight(),windowMessage);	
	}
	
	public static Image2D calculateDifferenceImage(Image2D image1, Image2D image2) {
		Image2D outputImage = new ByteImage2D(image1.getWidth(),image1.getHeight());
		Iterator<Point<Integer>> inputIterator = image1.pointIterator();
		Iterator<Point<Integer>> pluginIterator = image2.pointIterator();
		
		//Iterate over every point of the pictures
		while (inputIterator.hasNext()) {
			
			Point<Integer> inputPoint = inputIterator.next();
			Point<Integer> pluginPoint = pluginIterator.next();
			
			//Absolute difference between the points in the original and the filtered image
			Integer newValue = Math.abs(inputPoint.getValue() - pluginPoint.getValue());

			outputImage.set(inputPoint.getX(), inputPoint.getY(), newValue);
		}
		return outputImage;
	}
	
	public static List<Image2D> splitImageVertical(Image2D image){
		List<Image2D> ret = new ArrayList<Image2D>();
		int width = image.getWidth();
		int height = image.getHeight();
		int halfWidth = image.getWidth() / 2;
		ret.add(image.getSubImageByIndizes(0, 0, halfWidth-1, height-1));
		ret.add(image.getSubImageByIndizes(width - halfWidth, 0, width - 1, height-1));
		return ret;
	}
}
