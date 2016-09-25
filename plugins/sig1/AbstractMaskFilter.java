import ij.IJ;
import ij.ImagePlus;
import ij.gui.GenericDialog;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;

public abstract class AbstractMaskFilter implements PlugInFilter{

	private int radius = 1;
	
	@Override
	public void run(ImageProcessor ip) {
		byte[] pixels = (byte[])ip.getPixels();
		int width = ip.getWidth();
		int height = ip.getHeight();
		
		Image2D<Integer> inputImage = Image2DUtility.convert1DByteArrToImage2D(pixels, width, height);
		Image2D<Integer> outputImage = Image2DUtility.convert1DByteArrToImage2D(pixels, width, height);
		
		GenericDialog gd = new GenericDialog("User Input");
		gd.addNumericField("Radius", radius , 0);
		gd.showDialog();
		if(gd.wasCanceled()) {
			return;
		} //if
		this.radius = (int)gd.getNextNumber();
		
		for (ImagePoint<Integer> pixel : inputImage){
			Image2D<Integer> mask = inputImage.getMask(pixel.getX(), pixel.getY(), this.radius);
			
			Integer newValue = transformImagePoint(pixel,mask);
			outputImage.set(new ImagePoint<Integer>(newValue,pixel.getX(),pixel.getY()));
		}
		
		
		byte[] outPixels = Image2DUtility.convertFromImage2D(outputImage);
		ImageJUtility.showNewImage(outPixels, width, height, getFilterName() + " calculated with radius r = " + this.radius);
	}

	protected abstract Integer transformImagePoint(ImagePoint<Integer> pixel, Image2D<Integer> mask);

	@Override
	public int setup(String arg, ImagePlus imp) {
		if (arg.equals("about")){
			showAbout(); 
			return DONE;
		}
		return DOES_8G+SUPPORTS_MASKING;
	}
	
	private void showAbout() {
		IJ.showMessage("About " + getFilterName() +"...",
			"this is a PluginFilter template\n");
	} //showAbout

	protected abstract String getFilterName();
	
	public int getRadius(){
		return this.radius;
	}
	
	public int getMaxMaskFields(){
		return (this.radius * 2 + 1) * (this.radius * 2 + 1);
	}
}
