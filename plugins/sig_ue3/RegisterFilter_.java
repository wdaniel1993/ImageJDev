import java.util.List;

import ij.*;
import ue3.utility.ImageJUtility;
import ij.plugin.filter.PlugInFilter;
import ij.process.*;
import ue3.utility.Image2DUtility;
import ue3.utility.ByteImage2D;
import ue3.utility.Image2D;
import ij.gui.GenericDialog;

public class RegisterFilter_ implements PlugInFilter {

	int BG_VAL = 255;

	public int setup(String arg, ImagePlus imp) {
		if (arg.equals("about")) {
			showAbout();
			return DONE;
		} // if
		return DOES_8G + DOES_STACKS + SUPPORTS_MASKING;
	} // setup

	// TODO
	public double getEntropyOfImg(int[][] imgData, int width, int height) {

		return -1;
	} // getEntropyOfImg

	// TODO
	public double getEntropyOfImages(int[][] imgA, int[][] imgB, int width, int height) {
		return -1;
	}

	// TODO
	public int getBilinearInterpolatedValue(int[][] imgData, int width, int height, double idxX, double idxY) {
		return -1;
	} // getBilinearInterpolatedValue

	public int getNNInterpolatedValue(int[][] imgData, int width, int height, double idxX, double idxY) {
		int x1 = (int) (idxX + 0.5);
		int y1 = (int) (idxY + 0.5);

		if ((x1 >= 0) && (y1 >= 0) && (x1 < width) && (y1 < height)) {
			int actVal = imgData[x1][y1];
			return actVal;
		} // if
		else {
			return 255;
		} // else ==> outside mask
	} // getBilinearInterpolatedValue

	public int[][] getDiffImg(int[][] imgA, int[][] imgB, int width, int height) {
		int[][] returnImg = new int[width][height];

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				returnImg[x][y] = Math.abs(imgA[x][y] - imgB[x][y]);
			}
		}
		return returnImg;
	} // getDiffImg

	public double getImgDiffMI(int[][] imgData1, int[][] imgData2, int width, int height) {
		return getEntropyOfImg(imgData1, width, height) + getEntropyOfImg(imgData2, width, height)
				- getEntropyOfImages(imgData1, imgData2, width, height);
	} // getImgDiffMI

	public double getImgDiffSSE(int[][] imgData1, int[][] imgData2, int width, int height) {
		double errorVal = 0.0;
		
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				if(imgData1[x][y] !=  imgData2[x][y]) {
					int diffVal = imgData1[x][y] - imgData2[x][y];
					errorVal += diffVal * diffVal;
				}
			}
		}

		return errorVal;
	} // getImageDiffSS
	
	public double getImgDiffBinary(int[][] imgData1, int[][] imgData2, int width, int height) {
		double errorVal = 0.0;
		
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				if(imgData1[x][y] !=  imgData2[x][y]) {
					errorVal++;
				}
			}
		}

		return errorVal;
	} // getImageDiffSS

	public int[][] transformImg(int[][] inImg, int width, int height, double transX, double transY, double rotAngle) {
		int[][] retArr = new int[width][height];

		// calc center position

		// rotation angle degree ==> radian

		double radAngle = -rotAngle / 180 * Math.PI;
		double cosTheta = Math.cos(radAngle);
		double sinTheta = Math.sin(radAngle);
		
		double midX = width / 2.0;
		double midY = height / 2.0;

		// backprojekt all pixels of result image B
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				
				// move coordinates to center
				double posX = x - midX;
				double posY = y - midY;

				// rotate
				double newX = posX * cosTheta + posY * sinTheta;
				double newY = -posX * sinTheta + posY * cosTheta;

				//move coordinates back from center
				newX = newX + midX;
				newY = newY + midY;
				
				// translate
				posX = newX - transX;
				posY = newY - transY;

				int scalarVal = getNNInterpolatedValue(inImg, width, height, posX, posY);
				
				retArr [x][y] = scalarVal;
			}
		}

		return retArr;
	} // transformImg

	public void run(ImageProcessor ip) {
		byte[] pixels = (byte[]) ip.getPixels();
		int width = ip.getWidth();
		int height = ip.getHeight();

		int[][] inDataArr = ImageJUtility.convertFrom1DByteArr(pixels, width, height);
		
		final Image2D inputImage = new ByteImage2D(pixels, width, height);
		
		List<Image2D> images = Image2DUtility.splitImageVertical(inputImage);
		Image2DUtility.showImage2D(images.get(0), "first image");
		Image2DUtility.showImage2D(images.get(1), "second image");
		
		return;
/*
		// ... do something
		double transX = 0;
		double transY = 0;
		double rotAngle = 0;

		
		 GenericDialog gd = new GenericDialog("Registration Parameters");
		 gd.addNumericField("translation X  ", 0.0, 1);
		 gd.addNumericField("translation Y  ", 0.0, 1);
		 gd.addNumericField("rotation  ", 0.0, 1); gd.showDialog();
		 if(gd.wasCanceled()) { 
			 return; 
		 }
		  
		  
		 transX = gd.getNextNumber();
		 transY = gd.getNextNumber();
		 rotAngle = gd.getNextNumber();

		int[][] transformImgArr = transformImg(inDataArr, width, height, transX, transY, rotAngle);
		
		int[][] diffImgAB = getDiffImg(inDataArr, transformImgArr, width, height);
		double binaryMatchError = getImgDiffBinary(inDataArr, transformImgArr, width, height);
		double sumOfSquaredError = getImgDiffSSE(inDataArr, transformImgArr, width, height);

		byte[] transImgArr1D = ImageJUtility.convertFrom2DIntArr(transformImgArr, width, height);
		ImageJUtility.showNewImage(transImgArr1D, width, height, "transformed image");
		
		byte[] diffImgArr1D = ImageJUtility.convertFrom2DIntArr(diffImgAB, width, height);
		ImageJUtility.showNewImage(diffImgArr1D, width, height, "diff image, SSE= " + sumOfSquaredError + ", Binary = " + binaryMatchError);
*/
	} // run

	void showAbout() {
		IJ.showMessage("About Register_ ...", "core registration functionality ");
	} // showAbout

} // class Register_
