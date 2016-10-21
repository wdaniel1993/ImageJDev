import ij.IJ;
import ij.ImagePlus;
import ij.gui.GenericDialog;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;
import ue2.utility.ByteImage2D;
import ue2.utility.Image2D;
import ue2.utility.Image2DUtility;
import ue2.utility.ImageJUtility;

public abstract class AbstractResamplingFilter implements PlugInFilter {

	@Override
	public void run(ImageProcessor ip) {
		byte[] pixels = (byte[]) ip.getPixels();
		int width = ip.getWidth();
		int height = ip.getHeight();

		GenericDialog gd = new GenericDialog("User Input");
		gd.addNumericField("Resize factor", 2, 1);
		gd.showDialog();
		if (gd.wasCanceled()) {
			return;
		}
		double resizeFactor = gd.getNextNumber();
		
		/*
		 * Factor below or equal to 0
		 */
		if(resizeFactor <= 0){
			return;
		}
		
		final Image2D inputImage = new ByteImage2D(pixels, width, height);

		//Calls abstract method, which processes the input image and writes changes into the output image
		Image2D outputImage = resizeImage(inputImage, resizeFactor);
		
		byte[] outPixels = Image2DUtility.convertFromImage2D(outputImage);
		ImageJUtility.showNewImage(outPixels, width, height,getFilterName());
		
	}
	
	

	public Image2D resizeImage(Image2D inputImage, double resizeFactor) {
		final Image2D outputImage = new ByteImage2D((int) (inputImage.getWidth() / resizeFactor), (int) (inputImage.getHeight() / resizeFactor));
		return null;
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

	protected abstract String getFilterName();

}
