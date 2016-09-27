import ij.IJ;
import ij.ImagePlus;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;
import utility.ImageJUtility;

public class InvertFilter_ implements PlugInFilter{

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
		int[][] outArr = new int[width][height];
		//... do some filtering
		
		for (int x = 0; x < width; x++){
			for (int y=0; y < height; y++){
				int origiValue = inArr[x][y];
				int resultValue = 255 - origiValue;
				outArr[x][y] = resultValue;
			}
		}
		
		
		byte[] outPixels = ImageJUtility.convertFrom2DIntArr(outArr, width, height);
		ImageJUtility.showNewImage(outPixels, width, height, "inverted image");
		
	} //run

	void showAbout() {
		IJ.showMessage("About Template_...",
			"this is a PluginFilter template\n");
	} //showAbout

	
}
