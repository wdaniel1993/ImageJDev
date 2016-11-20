import java.text.SimpleDateFormat;
import java.util.List;

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

public abstract class AbstractRegisterFilter implements PlugInFilter {

	private double estTransX = 0;
	private double estTransY = 0;
	private double estRotAngle = 0;

	private double stepX = 1;
	private double stepY = 1;
	private double stepRot = 1;

	private double searchX = 10;
	private double searchY = 10;
	private double searchRot = 5;
	
	private double finalStep = 0.1;
	private double stepReductionFactor = 0.5;
	private double rangeReductionFactor = 0.8;
	
	private boolean moveOnly = false;
	private boolean showLog = false;
	
	@Override
	public void run(ImageProcessor ip) {
		byte[] pixels = (byte[]) ip.getPixels();
		int width = ip.getWidth();
		int height = ip.getHeight();

		final Image2D inputImage = new ByteImage2D(pixels, width, height);

		List<Image2D> images = Image2DUtility.splitImageVertical(inputImage);

		Image2D imageA = images.get(0);
		Image2D imageB = images.get(1);
		
		if(!inputDialog()){
			return;
		}
		
		Image2DUtility.showImage2D(imageA, "image A");
		Image2DUtility.showImage2D(imageB, "image B");
		
		IJ.showProgress((int) ((finalStep / stepRot) * 100), 100);
		
		prepareImages(imageA, imageB);
		
		
		long startTime = System.currentTimeMillis();
		
		while (stepX >= finalStep || stepY >= finalStep || stepRot >= finalStep) {
			double minDifference = Double.MAX_VALUE;
			double minX = 0;
			double minY = 0;
			double minRot = 0;

			for (double transX = estTransX - searchX * stepX; transX <= estTransX + searchX * stepX; transX += stepX) {
				for (double transY = estTransY - searchY * stepY; transY <= estTransY
						+ searchY * stepY; transY += stepY) {
					for (double transRot = estRotAngle - searchRot * stepRot; transRot <= estRotAngle
							+ searchRot * stepRot; transRot += stepRot) {
						
						double difference = transformAndCalculateDifference(transX, transY, transRot);
						
						if(showLog){
							IJ.log("transformed image (x = " + transX + ", y = " + transY + ", rot = " + transRot
									+ ", diff = " + difference + ")");
						}
						
						if (difference < minDifference) {
							minDifference = difference;
							minX = transX;
							minY = transY;
							minRot = transRot;
						}
					}
				}
			}

			stepX *= stepReductionFactor;
			stepY *= stepReductionFactor;
			stepRot *= stepReductionFactor;
			
			searchX *= rangeReductionFactor / stepReductionFactor;
			searchY *= rangeReductionFactor / stepReductionFactor;
			searchRot *= rangeReductionFactor / stepReductionFactor;
			
			estTransX = minX;
			estTransY = minY;
			estRotAngle = minRot;
			
			IJ.showProgress((int) ((finalStep / stepRot) * 100), 100);
		}
		
		Image2D outputImage = TransformHelper.transformImage(imageA, estTransX, estTransY, estRotAngle,
				new BiLinearInterpolator());
		Image2DUtility.showImage2D(outputImage,
				"registered image (x = " + estTransX + ", y = " + estTransY + ", rot = " + estRotAngle + " )");
		Image2DUtility.showImage2D(Image2DUtility.calculateDifferenceImage(outputImage, imageB),
				"difference image (x = " + estTransX + ", y = " + estTransY + ", rot = " + estRotAngle + " )");
		
		long ellapsedTime = System.currentTimeMillis() - startTime;
		IJ.showMessage("Ellapsed Time: " + formatTime(ellapsedTime));
	}
	
	protected abstract double transformAndCalculateDifference(double transX, double transY, double transRot);
	
	protected abstract void prepareImages(Image2D imageA, Image2D imageB );
	@Override
	public int setup(String arg, ImagePlus imp) {
		if (arg.equals("about")) {
			showAbout();
			return DONE;
		} // if
		return DOES_8G + DOES_STACKS + SUPPORTS_MASKING;
	}

	public void showAbout() {
		IJ.showMessage("About " + getFilterName() + " ...", "registration functionality ");
	} // showAbout
	
	public abstract String getFilterName();
	
	public boolean inputDialog(){
		GenericDialog gd = new GenericDialog("User Input");
		prepareDialog(gd);
		gd.showDialog();
		if (gd.wasCanceled()) {
			return false;
		}
		readDialogResult(gd);
		return true;
	}

	protected void readDialogResult(GenericDialog gd) {
		estTransX = gd.getNextNumber();
		estTransY = gd.getNextNumber();
		estRotAngle = gd.getNextNumber();

		stepX = gd.getNextNumber();
		stepY = gd.getNextNumber();
		stepRot = gd.getNextNumber();

		searchX = gd.getNextNumber();
		searchY = gd.getNextNumber();
		searchRot = gd.getNextNumber();

		finalStep = gd.getNextNumber();
		stepReductionFactor = gd.getNextNumber();
		rangeReductionFactor = gd.getNextNumber();
		
		moveOnly = gd.getNextBoolean();
		showLog = gd.getNextBoolean();
		if (moveOnly) {
			searchRot = 0;
			estRotAngle = 0;
		}
	}

	protected void prepareDialog(GenericDialog gd) {
		gd.addMessage("Estimates:");

		gd.addNumericField("translation X", estTransX, 1);
		gd.addNumericField("translation Y", estTransY, 1);
		gd.addNumericField("rotation", estRotAngle, 1);

		gd.addMessage("Initial Step Sizes:");

		gd.addNumericField("step size X", stepX, 1);
		gd.addNumericField("step size Y", stepY, 1);
		gd.addNumericField("step size Rotation", stepRot, 1);

		gd.addMessage("Step Count:");

		gd.addNumericField("step count X", searchX, 1);
		gd.addNumericField("step count Y", searchY, 1);
		gd.addNumericField("step count Rotation", searchRot, 1);

		gd.addMessage("Precision:");

		gd.addNumericField("precision", finalStep, 1);
		gd.addNumericField("step reduction", stepReductionFactor, 1);
		gd.addNumericField("range reduction", rangeReductionFactor, 1);
		
		gd.addMessage("Konfiguration:");

		gd.addCheckbox("move only", moveOnly);
		gd.addCheckbox("show log", showLog);
	}
	
	protected static String formatTime(long millis) {
	    SimpleDateFormat sdf = new SimpleDateFormat("mm:ss.SSS");

	    String strDate = sdf.format(millis);
	    return strDate;
	}

}
