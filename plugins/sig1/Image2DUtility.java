import java.util.ArrayList;
import java.util.List;

public class Image2DUtility {
	public static Image2D<Integer> convert1DByteArrToImage2D(byte[] pixels, int width, int height) {

		List<Integer> intList = new ArrayList<Integer>();

		for (byte b : pixels) {
			int pixel = (int) b;
			if (pixel < 0) {
				pixel += 256;
			}
			intList.add(pixel);
		}
		return new Image2D<Integer>(intList, width, height);
	}

	public static byte[] convertFromImage2D(Image2D<Integer> image) {
		byte[] outArray2D = new byte[image.getPointCount()];

		int pixelIdx1D = 0;
		for (ImagePoint<Integer> point: image) {
			int resultVal = point.getValue();
			if (resultVal > 127) {
				resultVal -= 256;
			}
			outArray2D[pixelIdx1D] = (byte) resultVal;
			pixelIdx1D++;
		}

		return outArray2D;
	}
}
