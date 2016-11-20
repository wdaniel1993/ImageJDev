import ij.IJ;
import ij.ImagePlus;
import ij.gui.GenericDialog;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;
import ue3.transform.TransformHelper;
import ue3.utility.BiLinearInterpolator;
import ue3.utility.ByteImage2D;
import ue3.utility.Image2D;
import ue3.utility.Image2DUtility;

public class TransformationFilter_ implements PlugInFilter{

	public int setup(String arg, ImagePlus imp) {
		if (arg.equals("about")) {
			showAbout();
			return DONE;
		} // if
		return DOES_8G + DOES_STACKS + SUPPORTS_MASKING;
	} // setup
	
	public void showAbout() {
		IJ.showMessage("About TransformationFilter_ ...", "transformation functionality ");
	} // showAbout

	@Override
	public void run(ImageProcessor ip) {
		byte[] pixels = (byte[]) ip.getPixels();
		int width = ip.getWidth();
		int height = ip.getHeight();
		
		final Image2D inputImage = new ByteImage2D(pixels, width, height);
		
		double transX = 0;
		double transY = 0;
		double rotAngle = 0;
		
		GenericDialog gd = new GenericDialog("Estimate values");
		 gd.addNumericField("translation X", 0.0, 1);
		 gd.addNumericField("translation Y", 0.0, 1);
		 gd.addNumericField("rotation", 0.0, 1); 
		 gd.showDialog();
		 if(gd.wasCanceled()) { 
			 return; 
		 }
		 
		 transX = gd.getNextNumber();
		 transY = gd.getNextNumber();
		 rotAngle = gd.getNextNumber();
		 
		 //transform image with user input parameters and bi linear interpolation
		 Image2D outputImage = TransformHelper.transformImage(inputImage, transX, transY, rotAngle, new BiLinearInterpolator());
		 
		 Image2DUtility.showImage2D(outputImage, "transformed image");
	}
	
}
