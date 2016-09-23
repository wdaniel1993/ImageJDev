import ij.ImagePlus;
import ij.process.ByteProcessor;
import ij.process.ImageProcessor;

public class ImageJUtility {

	public static int[][] convertFrom1DByteArr(byte[] pixels, int width, int height) {
		
		int[][] inArray2D = new int[width][height];
				
		int pixelIdx1D = 0;
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				inArray2D[x][y] = (int) pixels[pixelIdx1D];
				if (inArray2D[x][y] < 0) {
					inArray2D[x][y] += 256;
				} // if
				pixelIdx1D++;
			}
		}
		
		return inArray2D;
		
	}
	
	public static byte[] convertFrom2DIntArr(int[][] inArr, int width, int height) {
	  int pixelIdx1D = 0;
	  byte[] outArray2D = new byte[width * height];
	  
	  for (int y = 0; y < height; y++) {
		for (int x = 0; x < width; x++) {
			int resultVal = inArr[x][y];
			if (resultVal > 127) {
		      resultVal -= 256;
			}				
			outArray2D[pixelIdx1D] = (byte) resultVal;
			pixelIdx1D++;
			}
		}
	  
	  return outArray2D;
	}
	
	public static void showNewImage(int[][] inArr, int width, int height, String title) {
		byte[] byteArr = ImageJUtility.convertFrom2DIntArr(inArr, width, height);
		ImageJUtility.showNewImage(byteArr, width, height, title);
	}
	
	public static void showNewImage(byte[] inByteArr, int width, int height, String title) {
	  ImageProcessor outImgProc = new ByteProcessor(width, height);
	  outImgProc.setPixels(inByteArr);
	  
	  ImagePlus ip = new ImagePlus(title, outImgProc);
	  ip.show();
	}
			
	
}
