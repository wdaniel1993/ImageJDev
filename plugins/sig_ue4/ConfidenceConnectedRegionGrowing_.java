import java.awt.Rectangle;

import ij.IJ;
import ij.ImagePlus;
import ij.gui.GenericDialog;
import ij.process.ImageProcessor;
import ue4.utility.ImageJUtility;
import ue4.utility.SegmentationUtility;

public class ConfidenceConnectedRegionGrowing_ extends AbstractSegmentationFilter {

	private double confidence = 0.15;
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
		
		inputDialog();
		
		Rectangle rect = ip.getRoi();
		int startX = rect.x + rect.width / 2;
		int startY = rect.y + rect.height / 2;
		
		int startValue = inArr[startX][startY];
		
		int lower = startValue - (int) (confidence * 255);
		int upper = startValue + (int) (confidence * 255);
		
		int[][] outArr = SegmentationUtility.segmentRegionGrowing(width, height, inArr, startX, startY,n4,lower,upper);
		
		byte[] outPixels = ImageJUtility.convertFrom2DIntArr(outArr, width, height);
		ImageJUtility.showNewImage(outPixels, width, height, "region growing (confidence = " + confidence);
		
	} //run

	
	

	void showAbout() {
		IJ.showMessage("About Template_...",
			"this is a PluginFilter template\n");
	} //showAbout

	@Override
	protected void readDialogResult(GenericDialog gd) {
		confidence = gd.getNextNumber();
		n4 = gd.getNextBoolean();
	}

	@Override
	protected void prepareDialog(GenericDialog gd) {
		gd.addSlider("confidence", 0.01, 1, this.confidence);
		gd.addCheckbox("N4 (default N8)", n4);
	}
}
