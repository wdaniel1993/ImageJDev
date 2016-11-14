import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import ij.*;
import ue3.utility.ImageJUtility;
import ij.plugin.filter.PlugInFilter;
import ij.process.*;
import ue3.utility.Image2DUtility;
import ue3.transform.BinaryDifference;
import ue3.transform.ImageDifference;
import ue3.transform.MutualInformationDifference;
import ue3.transform.SumOfSquaredErrorDifference;
import ue3.utility.ByteImage2D;
import ue3.utility.Image2D;
import ij.gui.GenericDialog;

public class RegisterFilter_ implements PlugInFilter {

	public int BG_VAL = 255;
	private Dictionary<String,ImageDifference> choices = new Hashtable<String,ImageDifference>();
	
	
	public RegisterFilter_(){
		addImageDifferenceToChoices(new BinaryDifference());
		addImageDifferenceToChoices(new MutualInformationDifference());
		addImageDifferenceToChoices(new SumOfSquaredErrorDifference());
	}

	public int setup(String arg, ImagePlus imp) {
		if (arg.equals("about")) {
			showAbout();
			return DONE;
		} // if
		return DOES_8G + DOES_STACKS + SUPPORTS_MASKING;
	} // setup

	public int[][] getDiffImg(int[][] imgA, int[][] imgB, int width, int height) {
		int[][] returnImg = new int[width][height];

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				returnImg[x][y] = Math.abs(imgA[x][y] - imgB[x][y]);
			}
		}
		return returnImg;
	} // getDiffImg


	public void run(ImageProcessor ip) {
		byte[] pixels = (byte[]) ip.getPixels();
		int width = ip.getWidth();
		int height = ip.getHeight();

		int[][] inDataArr = ImageJUtility.convertFrom1DByteArr(pixels, width, height);
		
		final Image2D inputImage = new ByteImage2D(pixels, width, height);
		
		List<Image2D> images = Image2DUtility.splitImageVertical(inputImage);
		Image2DUtility.showImage2D(images.get(0), "first image");
		Image2DUtility.showImage2D(images.get(1), "second image");
		
		// ... do something
		double transX = 0;
		double transY = 0;
		double rotAngle = 0;
		ImageDifference diffCalculator = null;

		
		 GenericDialog gd = new GenericDialog("Estimate values");
		 gd.addNumericField("translation X", 0.0, 1);
		 gd.addNumericField("translation Y", 0.0, 1);
		 gd.addNumericField("rotation", 0.0, 1); 
		 gd.addChoice("calculator",choiceNames(), choices.keys().nextElement());
		 gd.showDialog();
		 if(gd.wasCanceled()) { 
			 return; 
		 }
		  
		  
		 transX = gd.getNextNumber();
		 transY = gd.getNextNumber();
		 rotAngle = gd.getNextNumber();
		 diffCalculator = choices.get(gd.getNextChoice());
		 
/*
		int[][] transformImgArr = transformImg(inDataArr, width, height, transX, transY, rotAngle);
		
		int[][] diffImgAB = getDiffImg(inDataArr, transformImgArr, width, height);
		double binaryMatchError = getImgDiffBinary(inDataArr, transformImgArr, width, height);
		double sumOfSquaredError = getImgDiffSSE(inDataArr, transformImgArr, width, height);

		byte[] transImgArr1D = ImageJUtility.convertFrom2DIntArr(transformImgArr, width, height);
		ImageJUtility.showNewImage(transImgArr1D, width, height, "transformed image");
		
		byte[] diffImgArr1D = ImageJUtility.convertFrom2DIntArr(diffImgAB, width, height);
		ImageJUtility.showNewImage(diffImgArr1D, width, height, "diff image, SSE= " + sumOfSquaredError + ", Binary = " + binaryMatchError);*/
	} // run

	public void showAbout() {
		IJ.showMessage("About Register_ ...", "core registration functionality ");
	} // showAbout
	
	private void addImageDifferenceToChoices(ImageDifference id){
		choices.put(id.getName(), id);
	}
	
	private String[] choiceNames() {
		Enumeration<String> keys = choices.keys();
	    String[] names = new String[choices.size()];

	    for (int i = 0; i < choices.size(); i++) {
	        names[i] = keys.nextElement();
	    }

	    return names;
	}
	
	

} // class Register_
