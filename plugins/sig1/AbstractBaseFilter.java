import java.text.SimpleDateFormat;
import ij.IJ;
import ij.ImagePlus;
import ij.gui.GenericDialog;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;
import utility.ByteImage2D;
import utility.Image2D;
import utility.Image2DUtility;
import utility.ImageJUtility;

/**
 * Abstractly implements the ImageJ PlugInFilter
 * Unifies the input dialog and defines a processImage method
 *
 */
public abstract class AbstractBaseFilter implements PlugInFilter {
	
	private boolean showTimer = false;
	
	@Override
	public void run(ImageProcessor ip) {
		byte[] pixels = (byte[]) ip.getPixels();
		int width = ip.getWidth();
		int height = ip.getHeight();

		final Image2D inputImage = new ByteImage2D(pixels, width, height);
		final Image2D outputImage = new ByteImage2D(pixels, width, height);

		inputDialog();
		long startTime = System.currentTimeMillis();
		processImage(inputImage, outputImage);
		long ellapsedTime = System.currentTimeMillis() - startTime;
		
		byte[] outPixels = Image2DUtility.convertFromImage2D(outputImage);
		ImageJUtility.showNewImage(outPixels, width, height,getFilterName());
		
		
		if(showTimer){
			IJ.showMessage("Ellapsed Time: " + formatTime(ellapsedTime));
		}
	}
	
	private static String formatTime(long millis) {
	    SimpleDateFormat sdf = new SimpleDateFormat("mm:ss.SSS");

	    String strDate = sdf.format(millis);
	    return strDate;
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
	
	/*
	 * processes an Image2D (inputImage) and writes the transformed values into the outputImage
	 */
	public abstract void processImage(final Image2D inputImage, final Image2D outputImage);
	
	public abstract String getFilterName();
	
	public void readDialogResult(GenericDialog gd){
		showTimer = gd.getNextBoolean();
	}

	public void prepareDialog(GenericDialog gd) {
		gd.addCheckbox("Timer", false);
	}
	
	/*
	 * Opens a dialog, prepares the dialog and reads the values
	 */
	public void inputDialog(){
		GenericDialog gd = new GenericDialog("User Input");
		prepareDialog(gd);
		gd.showDialog();
		if (gd.wasCanceled()) {
			return;
		}
		readDialogResult(gd);
	}
}
