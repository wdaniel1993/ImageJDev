import ij.IJ;
import ij.ImagePlus;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;

public class MeanFilter_ implements PlugInFilter {

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
		
		int filterRadius = 1;
		double ratio = 1.0 / 9.0;
		double[][] filterMask = new double[][]{{ratio,ratio,ratio},{ratio,ratio,ratio},{ratio,ratio,ratio}};
		
		for (int x = 0; x < width; x++){
			for (int y=0; y < height; y++){
				int origiValue = inArr[x][y];
				
				double sum = 0.0;
				
				//filter one pixel
				for(int xOffset = -filterRadius; xOffset <= filterRadius; xOffset ++){
					for(int yOffset = -filterRadius; yOffset <= filterRadius; yOffset ++){
						int  nbX =  x+ xOffset;
						int nbY = y + yOffset;
						if(nbX >= 0 && nbX <width && nbY >= 0 && nbY < height){
							double nbVal = inArr[nbX][nbY] * filterMask[xOffset + filterRadius][yOffset + filterRadius];
							sum += nbVal;
						}
						
					}
				}
				
				//apply pix result to image B
				
				int resultValue = (int)(sum +0.5);
				outArr[x][y] = resultValue;
			}
		}
		
		
		byte[] outPixels = ImageJUtility.convertFrom2DIntArr(outArr, width, height);
		ImageJUtility.showNewImage(outPixels, width, height, "mean filter calculated with radius r = 1");
		
	} //run

	void showAbout() {
		IJ.showMessage("About mean filter...",
			"this is a PluginFilter template\n");
	} //showAbout
}
