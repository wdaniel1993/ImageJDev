import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
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
import ue3.utility.NearestNeighbourInterpolator;
import ue3.utility.Point;
import ue3.utility.ThresholdUtility;

public class DistanceMapRegisterFilter_ implements PlugInFilter {

	@Override
	public void run(ImageProcessor ip) {
		byte[] pixels = (byte[]) ip.getPixels();
		int width = ip.getWidth();
		int height = ip.getHeight();

		final Image2D inputImage = new ByteImage2D(pixels, width, height);
		List<Image2D> images = Image2DUtility.splitImageVertical(inputImage);

		Image2D imageA = images.get(0);
		Image2D imageB = images.get(1);

		int threshold = 127;
		
		double estTransX = 0;
		double estTransY = 0;
		double estRotAngle = 0;

		double stepX = 1;
		double stepY = 1;
		double stepRot = 1;

		double finalStep = 0.1;

		double searchX = 20;
		double searchY = 20;
		double searchRot = 10;

		GenericDialog gd = new GenericDialog("Estimate values");

		gd.addNumericField("threshold", threshold, 0);
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

		gd.addMessage("Konfiguration:");

		gd.addNumericField("precision", finalStep, 1);
		gd.addCheckbox("move only", false);
		gd.addCheckbox("show log", false);

		gd.showDialog();
		if (gd.wasCanceled()) {
			return;
		}
		
		threshold = (int) gd.getNextNumber();

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
		boolean moveOnly = gd.getNextBoolean();
		boolean showLog = gd.getNextBoolean();
		if (moveOnly) {
			searchRot = 0;
			estRotAngle = 0;
		}

		Image2D thresholdImageA = ThresholdUtility.binaryThreshold(imageA, threshold, true);
		Image2D thresholdImageB = ThresholdUtility.binaryThreshold(imageB, threshold, true);

		Image2DUtility.showImage2D(imageA, "image A");
		Image2DUtility.showImage2D(imageB, "image B");

		ImagePlus distanceMapImpA = Image2DUtility.toImagePlus(thresholdImageA, "distance Map");
		IJ.run(distanceMapImpA, "Distance Map", "");
		Image2D distanceMap = Image2DUtility.fromImagePlus(distanceMapImpA);

		ImageProcessor edgesIP = Image2DUtility.toImageProcessor(thresholdImageB);
		edgesIP.findEdges();
		Image2D edges = Image2DUtility.fromImageProcessor(edgesIP);

		List<Point<Integer>> landMarks = new ArrayList<Point<Integer>>();
		Iterator<Point<Integer>> it = edges.pointIterator();
		while (it.hasNext()) {
			Point<Integer> point = it.next();
			if (point.getValue() == 255) {
				landMarks.add(point);
			}
		}
		
		IJ.showProgress((int) ((finalStep / stepRot) * 100), 100);
		long startTime = System.currentTimeMillis();

		while (stepX >= finalStep || stepY >= finalStep || stepRot >= finalStep) {
			int minDifference = Integer.MAX_VALUE;
			double minX = 0;
			double minY = 0;
			double minRot = 0;

			for (double transX = estTransX - searchX * stepX; transX <= estTransX + searchX * stepX; transX += stepX) {
				for (double transY = estTransY - searchY * stepY; transY <= estTransY
						+ searchY * stepY; transY += stepY) {
					for (double transRot = estRotAngle - searchRot * stepRot; transRot <= estRotAngle
							+ searchRot * stepRot; transRot += stepRot) {
						int difference = TransformHelper.calculateDifferenceWithDistanceMap(landMarks, distanceMap, transX, transY, transRot, new NearestNeighbourInterpolator());
						
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

			stepX /= 2;
			stepY /= 2;
			stepRot /= 2;
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

	@Override
	public int setup(String arg, ImagePlus imp) {
		if (arg.equals("about")) {
			showAbout();
			return DONE;
		} // if
		return DOES_8G + DOES_STACKS + SUPPORTS_MASKING;
	}

	public void showAbout() {
		IJ.showMessage("About DistanceMapRegisterFilter_ ...", "distance map registration functionality ");
	} // showAbout
	
	private static String formatTime(long millis) {
		SimpleDateFormat sdf = new SimpleDateFormat("mm:ss.SSS");

		String strDate = sdf.format(millis);
		return strDate;
	}

}
