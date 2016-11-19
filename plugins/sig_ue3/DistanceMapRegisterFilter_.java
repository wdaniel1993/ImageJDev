import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ij.IJ;
import ij.ImagePlus;
import ij.gui.GenericDialog;
import ij.process.ImageProcessor;
import ue3.transform.TransformHelper;
import ue3.utility.Image2D;
import ue3.utility.Image2DUtility;
import ue3.utility.NearestNeighbourInterpolator;
import ue3.utility.Point;
import ue3.utility.ThresholdUtility;

public class DistanceMapRegisterFilter_ extends AbstractRegisterFilter  {

	private int threshold = 127;
	private List<Point<Integer>> landMarks;
	private Image2D distanceMap;

	@Override
	protected double transformAndCalculateDifference(double transX, double transY, double transRot) {
		return TransformHelper.calculateDifferenceWithDistanceMap(landMarks, distanceMap, transX, transY, transRot, new NearestNeighbourInterpolator());
	}

	@Override
	protected void prepareImages(Image2D imageA, Image2D imageB) {
		Image2D thresholdImageA = ThresholdUtility.binaryThreshold(imageA, threshold, true);
		Image2D thresholdImageB = ThresholdUtility.binaryThreshold(imageB, threshold, true);

		ImagePlus distanceMapImpA = Image2DUtility.toImagePlus(thresholdImageA, "distance Map");
		IJ.run(distanceMapImpA, "Distance Map", "");
		distanceMap = Image2DUtility.fromImagePlus(distanceMapImpA);

		ImageProcessor edgesIP = Image2DUtility.toImageProcessor(thresholdImageB);
		edgesIP.findEdges();
		Image2D edges = Image2DUtility.fromImageProcessor(edgesIP);

		landMarks = new ArrayList<Point<Integer>>();
		Iterator<Point<Integer>> it = edges.pointIterator();
		while (it.hasNext()) {
			Point<Integer> point = it.next();
			if (point.getValue() == 255) {
				landMarks.add(point);
			}
		}
	}
	
	protected void prepareDialog(GenericDialog gd) {
		gd.addNumericField("threshold", threshold, 0);
		super.prepareDialog(gd);
	}
	
	protected void readDialogResult(GenericDialog gd) {
		threshold = (int) gd.getNextNumber();
		super.readDialogResult(gd);
	}

	@Override
	public String getFilterName() {
		return "DistanceMapRegisterFilter_";
	}

}
