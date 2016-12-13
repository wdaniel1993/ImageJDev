import java.awt.Rectangle;

import ij.IJ;
import ij.ImagePlus;
import ij.gui.GenericDialog;
import ij.process.ImageProcessor;
import ue4.utility.ImageJUtility;
import ue4.utility.SegmentationUtility;

/**
 * RegionGrowing
 * RegionGrwoing with two thresholds
 *
 */
public class RegionGrowing_ extends AbstractSegmentationFilter {

	private int lowerThresh = 110;
	private int upperThresh = 250;
	private boolean n4 = false;
	
	
	public int setup(String arg, ImagePlus imp) {
		if (arg.equals("about"))
			{showAbout(); return DONE;}
		return DOES_8G+SUPPORTS_MASKING+ROI_REQUIRED;
	} //setup

	public void run(ImageProcessor ip) {
		byte[] pixels = (byte[])ip.getPixels();
		int width = ip.getWidth();
		int height = ip.getHeight();
		
		int[][] inArr = ImageJUtility.convertFrom1DByteArr(pixels, width, height);
		
		if(!inputDialog()){
			return;
		}
		
		/*
		 * get middle of region of interest
		 */
		Rectangle rect = ip.getRoi();
		int startX = rect.x + rect.width / 2;
		int startY = rect.y + rect.height / 2;
		
		/*
		 * start region growing from start points with the either n4 or n8 structure 
		 */
		int[][] outArr = SegmentationUtility.segmentRegionGrowing(width, height, inArr, startX, startY,n4,lowerThresh,upperThresh);
		
		/*
		 * convert 2d to 1d array and shows image
		 */
		byte[] outPixels = ImageJUtility.convertFrom2DIntArr(outArr, width, height);
		ImageJUtility.showNewImage(outPixels, width, height, "region growing (lower thresh = " + lowerThresh + ", upper thresh = " + upperThresh);
		
	} //run

	
	

	void showAbout() {
		IJ.showMessage("About Template_...",
			"this is a PluginFilter template\n");
	} //showAbout

	@Override
	protected void readDialogResult(GenericDialog gd) {
		lowerThresh = (int) gd.getNextNumber();
		upperThresh = (int) gd.getNextNumber();
		n4 = gd.getNextBoolean();
	}

	@Override
	protected void prepareDialog(GenericDialog gd) {
		gd.addNumericField("lower threshold", this.lowerThresh, 0);
		gd.addNumericField("upper threshold", this.upperThresh, 0);
		gd.addCheckbox("N4 (default N8)", n4);
	}
}
