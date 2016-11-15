import java.text.SimpleDateFormat;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import ij.*;
import ue3.utility.NearestNeighbourInterpolator;
import ij.plugin.filter.PlugInFilter;
import ij.process.*;
import ue3.utility.Image2DUtility;
import ue3.transform.BinaryDifference;
import ue3.transform.ImageDifference;
import ue3.transform.MutualInformationDifference;
import ue3.transform.SumOfSquaredErrorDifference;
import ue3.transform.TransformHelper;
import ue3.utility.BiLinearInterpolator;
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
		
		final Image2D inputImage = new ByteImage2D(pixels, width, height);
		
		List<Image2D> images = Image2DUtility.splitImageVertical(inputImage);
		
		
		Image2D imageA = images.get(0);
		Image2D imageB = images.get(1);
		
		// ... do something
		double estTransX = 0;
		double estTransY = 0;
		double estRotAngle = 0;
		boolean moveOnly = false;
		ImageDifference diffCalculator = null;

		
		 GenericDialog gd = new GenericDialog("Estimate values");
		 gd.addNumericField("translation X", 0.0, 1);
		 gd.addNumericField("translation Y", 0.0, 1);
		 gd.addNumericField("rotation", 0.0, 1); 
		 gd.addCheckbox("move only", false);
		 gd.addChoice("calculator",choiceNames(), choices.keys().nextElement());
		 gd.showDialog();
		 if(gd.wasCanceled()) { 
			 return; 
		 }
		  
		 Image2DUtility.showImage2D(imageA, "image A");
		 Image2DUtility.showImage2D(imageB, "image B");
		  
		 estTransX = gd.getNextNumber();
		 estTransY = gd.getNextNumber();
		 estRotAngle = gd.getNextNumber();
		 moveOnly = gd.getNextBoolean();
		 diffCalculator = choices.get(gd.getNextChoice());
		 
		 double stepX = 1;
		 double stepY = 1;
		 double stepRot = 1;
		 
		 double finalStep = 0.2;
		 
		 double searchX = 10;
		 double searchY = 10;
		 double searchRot = moveOnly ? 0 : 5;
		 
		 IJ.showProgress((int)((finalStep / stepRot) * 100), 100);
		 long startTime = System.currentTimeMillis();
		 
		 while(stepRot > finalStep) {
			 double minDifference = Double.MAX_VALUE;
			 double minX = 0;
			 double minY = 0;
			 double minRot = 0;
			 
			 for(double transX = estTransX - searchX * stepX; transX <= estTransX + searchX * stepX; transX += stepX){
				 for(double transY = estTransY - searchY * stepY; transY <= estTransY + searchY * stepY; transY += stepY){
					 for(double transRot = estRotAngle - searchRot * stepRot; transRot <= estRotAngle + searchRot * stepRot; transRot += stepRot){
						 Image2D transformedImage = TransformHelper.transformImage(imageB, transX, transY, transRot, new NearestNeighbourInterpolator());
						 double difference = diffCalculator.calculateDifference(imageA, transformedImage);
						 IJ.log("transformed image (x = " + transX + ", y = " + transY + ", rot = " + transRot +", diff = "+ difference +")");
						 if(difference < minDifference){
							 minDifference = difference;
							 minX = transX;
							 minY = transY;
							 minRot = transRot;
						 }
					 }
				 }
			 }
			 
			 estTransX = minX;
			 estTransY = minY;
			 estRotAngle = minRot;
			 stepX /= 2;
			 stepY /= 2;
			 stepRot /= 2;
			 
			 IJ.showProgress((int)((finalStep / stepRot) * 100), 100);
		 }
		 
		 Image2D outputImage = TransformHelper.transformImage(imageB, estTransX, estTransY, estRotAngle, new BiLinearInterpolator());
		 Image2DUtility.showImage2D(outputImage, "registered image (x = " + estTransX + ", y = " + estTransY + ", rot = " + estRotAngle +" )");
		 long ellapsedTime = System.currentTimeMillis() - startTime;
		 IJ.showMessage("Ellapsed Time: " + formatTime(ellapsedTime));
	} // run

	public void showAbout() {
		IJ.showMessage("About RegisterFilter_ ...", "core registration functionality ");
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
	
	private static String formatTime(long millis) {
	    SimpleDateFormat sdf = new SimpleDateFormat("mm:ss.SSS");

	    String strDate = sdf.format(millis);
	    return strDate;
	}
	

} // class RegisterFilter_
