package filter;

import ij.IJ;
import ij.ImagePlus;
import ij.gui.GenericDialog;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;
import utility.ByteImage2D;
import utility.Image2D;
import utility.Image2DUtility;
import utility.ImageJUtility;

public abstract class AbstractBaseFilter implements PlugInFilter {
	
	@Override
	public void run(ImageProcessor ip) {
		byte[] pixels = (byte[]) ip.getPixels();
		int width = ip.getWidth();
		int height = ip.getHeight();

		final Image2D inputImage = new ByteImage2D(pixels, width, height);
		final Image2D outputImage = new ByteImage2D(pixels, width, height);

		GenericDialog gd = new GenericDialog("User Input");
		prepareDialog(gd);
		gd.showDialog();
		if (gd.wasCanceled()) {
			return;
		}
		readDialogResult(gd);
		
		processImage(inputImage, outputImage);
		
		byte[] outPixels = Image2DUtility.convertFromImage2D(outputImage);
		ImageJUtility.showNewImage(outPixels, width, height,getFilterName());
	}

	@Override
	public int setup(String arg, ImagePlus imp) {
		if (arg.equals("about")) {
			showAbout();
			return DONE;
		}
		return DOES_8G + SUPPORTS_MASKING;
	}

	private void showAbout() {
		IJ.showMessage("About " + getFilterName() + "...", "this is a PluginFilter template\n");
	}
	
	public abstract void processImage(final Image2D inputImage, final Image2D outputImage);
	
	public abstract String getFilterName();
	
	public abstract void readDialogResult(GenericDialog gd);

	public abstract void prepareDialog(GenericDialog gd);
}
