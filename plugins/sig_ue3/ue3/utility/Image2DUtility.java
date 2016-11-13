package ue3.utility;
import java.util.Iterator;

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
}
