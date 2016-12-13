import ij.IJ;
import ij.ImagePlus;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;
import ue4.utility.ImageJUtility;

public class BinaryThresholdBorder_ implements PlugInFilter {

	public int setup(String arg, ImagePlus imp) {
		if (arg.equals("about"))
			{showAbout(); return DONE;}
		return DOES_8G+SUPPORTS_MASKING;
	} //setup

	public void run(ImageProcessor ip) {
		byte[] pixels = (byte[])ip.getPixels();
		int width = ip.getWidth();
		int height = ip.getHeight();
		
		int[][] inArr = ImageJUtility.convertFrom1DByteArr(pixels, width, height);
		
		int thresholdTop = 100;
		int thresholdBottom = 80;
		int BG_VAL = 0;
		int FG_VAL = 255;
		
		int[][] outArr = new int[width][height];
		for(int x = 0; x < width; x++){
			for(int y = 0; y <height; y++){
				if(inArr[x][y] <= thresholdTop && inArr[x][y] >= thresholdBottom){
					outArr[x][y] = FG_VAL;
				}
				else{
					outArr[x][y] = BG_VAL;
				}
			}
		}

		byte[] outPixels = ImageJUtility.convertFrom2DIntArr(outArr, width, height);
		ImageJUtility.showNewImage(outPixels, width, height, "threshold");
		
	} //run

	void showAbout() {
		IJ.showMessage("About Template_...",
			"this is a PluginFilter template\n");
	} //showAbout

}
