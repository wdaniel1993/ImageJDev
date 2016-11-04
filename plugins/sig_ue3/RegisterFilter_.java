import ij.*;
import ij.text.*;
import ue3.utility.ImageJUtility;
import ij.plugin.filter.PlugInFilter;
import ij.process.*;

import java.awt.*;

import ij.gui.GenericDialog;

public class RegisterFilter_ implements PlugInFilter {

  int BG_VAL = 255;

  public int setup(String arg, ImagePlus imp) {
    if(arg.equals("about")){
      showAbout();
      return DONE;
    } //if
    return DOES_8G+DOES_STACKS+SUPPORTS_MASKING;
  } //setup
  
   //TODO
   public double getEntropyOfImg(int[][] imgData, int width, int height) {
   
     return -1;
   } //getEntropyOfImg
 
   //TODO
   public double getEntropyOfImages(int[][] imgA, int[][] imgB, int width, int height) {
     return -1;
   } 

   //TODO
public int getBilinearInterpolatedValue(int[][] imgData, int width, int height, double idxX, double idxY) {
  return -1;
} //getBilinearInterpolatedValue

public int getNNInterpolatedValue(int[][] imgData, int width, int height, double idxX, double idxY) {
	  int x1 = (int) (idxX + 0.5);
	  int y1 = (int) (idxY + 0.5);
	  	  
	  if((x1 >= 0) && (y1 >= 0) && (x1 < width) && (y1 < height)) {
	    int actVal = imgData[x1][y1];		  
		return actVal;
	  } //if
	  else {
	    return 255;
	  } //else ==> outside mask
	} //getBilinearInterpolatedValue

public int[][] getDiffImg(int[][] imgA, int[][] imgB, int width, int height) {
  int[][] returnImg = new int[width][height];
  
  
  return returnImg;
} //getDiffImg



 
  public double getImgDiffMI(int[][] imgData1, int[][] imgData2, int width, int height) {
    return getEntropyOfImg(imgData1, width, height) + 
	            getEntropyOfImg(imgData2, width, height) - 
				getEntropyOfImages(imgData1, imgData2, width, height);
  } //getImgDiffMI
 
  public double getImgDiffSSE(int[][] imgData1, int[][] imgData2, int width, int height) {
    
    return -1;
  } //getImageDiffSS

public int[][] transformImg(int[][] inImg, int width, int height, double transX, double transY, double rotAngle) {
    int[][] retArr = new int[width][height];

	//calc center position
		
	//rotation angle degree ==> radian
	
	
    

    return retArr;  
  } //transformImg

  public void run(ImageProcessor ip) {
    byte[] pixels = (byte[])ip.getPixels();
    int width = ip.getWidth();
    int height = ip.getHeight();

    int[][] inDataArr = ImageJUtility.convertFrom1DByteArr(pixels, width, height);   
	
	//... do something
	double transX = 10;
	double transY = -15;
	double rotAngle = 5;
	
	/*GenericDialog gd = new GenericDialog("Registration Parameters");
	gd.addNumericField("translation X  ", 0.0, 1);
	gd.addNumericField("translation Y  ", 0.0, 1);
	gd.addNumericField("roatation  ", 0.0, 1);
	gd.showDialog();
	if(gd.wasCanceled()) {  
	   return;
	} //if
	transX = (int)gd.getNextNumber();
	transY = (int)gd.getNextNumber();
	rotAngle = (int)gd.getNextNumber(); */

	
	
	
    
	int[][] transformImgArr = transformImg(inDataArr, width, height, transX, transY, rotAngle);
	
	
	
	byte[] transImgArr1D = ImageJUtility.convertFrom2DIntArr(transformImgArr, width, height);
		   
  } //run

  void showAbout() {
    IJ.showMessage("About Register_ ...", "core registration functionality ");
  } //showAbout


} //class Register_ 
