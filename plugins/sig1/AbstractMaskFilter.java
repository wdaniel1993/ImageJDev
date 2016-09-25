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
		
		Image2D inputImage = new Image2D(pixels, width, height);
		Image2D outputImage =  new Image2D(pixels, width, height);
		
		GenericDialog gd = new GenericDialog("User Input");
		gd.addNumericField("Radius", this.radius , 0);
		gd.showDialog();
		if(gd.wasCanceled()) {
			return;
		} 
		
		this.radius = (int)gd.getNextNumber();
		
		for (ImageIterator<Integer> iterator = inputImage.iterator(); iterator.hasNext();){
			int pixel = iterator.next();
			int x = iterator.indexX();
			int y = iterator.indexY();
			
			Image2D mask = inputImage.getMask(x, y, this.radius);
			Integer newValue = transformImagePoint(x,y,mask);
			
			outputImage.set(x, y, newValue);
		}
		
		byte[] outPixels = Image2DUtility.convertFromImage2D(outputImage);
		ImageJUtility.showNewImage(outPixels, width, height, getFilterName() + " calculated with radius r = " + this.radius);
	}

	protected abstract int transformImagePoint(int x, int y, Image2D mask);

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
