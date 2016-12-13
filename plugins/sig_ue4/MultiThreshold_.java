import ij.IJ;
import ij.ImagePlus;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;
import ue4.utility.ImageJUtility;

public class MultiThreshold_ implements PlugInFilter {

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
		
		int T1 = 20;
		int T2 = 55;
		int T3 = 85;
		int T4 = 140;
		int T5 = 200;
		
		int[][] outArr = new int[width][height];
		for(int x = 0; x < width; x++){
			for(int y = 0; y <height; y++){
				if(inArr[x][y] < T1){
					outArr[x][y] = 1;
				}
				else if(inArr[x][y] < T2){
					outArr[x][y] = 2;
				}
				else if(inArr[x][y] < T3){
					outArr[x][y] = 3;
				}
				else if(inArr[x][y] < T4){
					outArr[x][y] = 4;
				}
				else if(inArr[x][y] < T5){
					outArr[x][y] = 5;
				}else {
					outArr[x][y] = 0;
				}
			}
		}
		
		byte[] outPixels = ImageJUtility.convertFrom2DIntArr(outArr, width, height);
		ImageJUtility.showNewImage(outPixels, width, height, "threshold with T");
		
	} //run

	void showAbout() {
		IJ.showMessage("About Template_...",
			"this is a PluginFilter template\n");
	} //showAbout

}
