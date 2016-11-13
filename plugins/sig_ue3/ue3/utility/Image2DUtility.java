package ue3.utility;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
	
	public static void showImage2D(Image2D image, String windowMessage) {
		byte[] outPixels = Image2DUtility.convertFromImage2D(image);
		ImageJUtility.showNewImage(outPixels, image.getWidth(), image.getHeight(),windowMessage);	
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
