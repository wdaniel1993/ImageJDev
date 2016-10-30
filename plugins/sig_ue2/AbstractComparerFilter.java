import ij.IJ;
import ij.ImagePlus;
import ij.gui.GenericDialog;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;
import ue2.utility.ByteImage2D;
import ue2.utility.Image2D;
import ue2.utility.Image2DUtility;
import ue2.utility.ImageJUtility;

/**
 * Abstract base class for all filters which compare two images
 */
public abstract class AbstractComparerFilter implements PlugInFilter {
	
	public abstract Image2D compareImage(Image2D image1, Image2D image2);

	@Override
	public void run(ImageProcessor ip) {
		byte[] pixels = (byte[]) ip.getPixels();
		int width = ip.getWidth();
		int height = ip.getHeight();
		
		final Image2D inputImage = new ByteImage2D(pixels, width, height);
		
		//Read resize factor
		GenericDialog gd = new GenericDialog("User Input");
		gd.addNumericField("Resize factor", 2, 1);
		gd.showDialog();
		if (gd.wasCanceled()) {
			return;
		}
		double resizeFactor = gd.getNextNumber();
		
		//Instantiate the resampling filters and resize the images
		AbstractResamplingFilter plugin1 = new BiLinearFilter_();
		AbstractResamplingFilter plugin2 = new NearestNeighbourFilter_();
		Image2D image1 = plugin1.resizeImage(inputImage, resizeFactor);
		Image2D image2 = plugin2.resizeImage(inputImage, resizeFactor);
		
		//compare the two resized images
		Image2D comparedImage = compareImage(image1, image2);
		
		//output the comparision
		byte[] outPixels = Image2DUtility.convertFromImage2D(comparedImage);
		ImageJUtility.showNewImage(outPixels, comparedImage.getWidth(), comparedImage.getHeight(),getFilterName());	
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
