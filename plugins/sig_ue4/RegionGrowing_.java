import java.awt.Point;
import java.awt.Rectangle;
import java.util.Stack;

import ij.IJ;
import ij.ImagePlus;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;
import ue4.utility.ImageJUtility;

public class RegionGrowing_ implements PlugInFilter {

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
		
		int lowerThresh = 80;
		int upperThresh = 120;
		int BG_VAL = 0;
		int FG_VAL = 255;
		
		int[][] outArr = new int[width][height];
		
		for(int x = 0; x < width; x++){
			for(int y = 0; y <height; y++){
				outArr[x][y] = BG_VAL;
			}
		}
		
		//user defined seed position
		Rectangle rect = ip.getRoi();
		int startX = rect.x + rect.width / 2;
		int startY = rect.y + rect.height / 2;
		
		Stack<Point> stack = new Stack<Point>();
		stack.push(new Point(startX, startY));
		outArr[startX][startY] = FG_VAL;
		
		while(!stack.empty()){
			Point curPosition = stack.pop();
			
			//search neighbours
			for(int xOffset = -1; xOffset <= 1; xOffset++){
				for(int yOffset = -1; yOffset <= 1; yOffset++){
					int nbX = curPosition.x + xOffset;
					int nbY = curPosition.y + yOffset;
					
					//range check
					if(nbX >= 0 && nbX < width && nbY >= 0 && nbY < height){
						int nbVal = inArr[nbX][nbY];
						if(nbVal >= lowerThresh && nbVal <= upperThresh && outArr[nbX][nbY] == BG_VAL){
							outArr[nbX][nbY] = FG_VAL;
							stack.push(new Point(nbX,nbY));
						}
					}
				}
			}
		}
		
		
		
		byte[] outPixels = ImageJUtility.convertFrom2DIntArr(outArr, width, height);
		ImageJUtility.showNewImage(outPixels, width, height, "region growing (lower thresh = " + lowerThresh + ", upper thresh = " + upperThresh);
		
	} //run

	void showAbout() {
		IJ.showMessage("About Template_...",
			"this is a PluginFilter template\n");
	} //showAbout

}
