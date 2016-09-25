import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Image2DUtility {
	public static Image2D convert1DByteArrToImage2D(byte[] pixels, int width, int height) {

		List<Integer> intList = new ArrayList<Integer>();

		for (byte b : pixels) {
			int pixel = (int) b;
			if (pixel < 0) {
				pixel += 256;
			}
			intList.add(pixel);
		}
		return new Image2D(intList, width, height);
	}

	public static byte[] convertFromImage2D(Image2D image) {
		byte[] outArray2D = new byte[image.getPointCount()];

		int pixelIdx1D = 0;
		for (Iterator<Integer> it = image.rotatedIterator(); it.hasNext(); ) {
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
