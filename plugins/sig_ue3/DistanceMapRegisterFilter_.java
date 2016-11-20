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

/**
 * DistanceMapRegisterFilter_s
 * Registration with 
 */
public class DistanceMapRegisterFilter_ extends AbstractRegisterFilter  {

	private int threshold = 127;
	private int landMarkCount = 20;

	private List<Point<Integer>> landMarks;
	private Image2D distanceMap;
	
	//Transform the landmarks and calculate the difference with the distance map prepared
	@Override
	protected double transformAndCalculateDifference(double transX, double transY, double transRot) {
		return TransformHelper.calculateDifferenceWithDistanceMap(landMarks, distanceMap, transX, transY, transRot, new NearestNeighbourInterpolator());
	}

	//Prepare distance map and landmarks
	@Override
	protected void prepareImages(Image2D imageA, Image2D imageB) {
		//calculate the background and foreground of the images with a binary threshold
		Image2D thresholdImageA = ThresholdUtility.binaryThreshold(imageA, threshold, true);
		Image2D thresholdImageB = ThresholdUtility.binaryThreshold(imageB, threshold, true);

		//calculate distance map of the prepared image A
		ImagePlus distanceMapImpA = Image2DUtility.toImagePlus(thresholdImageA, "distance Map");
		IJ.run(distanceMapImpA, "Distance Map", "");
		distanceMap = Image2DUtility.fromImagePlus(distanceMapImpA);

		//calculate edges of the image B
		ImageProcessor edgesIP = Image2DUtility.toImageProcessor(thresholdImageB);
		edgesIP.findEdges();
		Image2D edges = Image2DUtility.fromImageProcessor(edgesIP);

		//pick landmarks from points in the edges
		landMarks = new ArrayList<Point<Integer>>();
		Iterator<Point<Integer>> it = edges.pointIterator();
		while (it.hasNext()) {
			Point<Integer> point = it.next();
			if (point.getValue() == 255) {
				landMarks.add(point);
			}
		}
		landMarks = pickNElements(landMarks,landMarkCount);
	}
	
	protected void prepareDialog(GenericDialog gd) {
		gd.addNumericField("threshold", threshold, 0);
		gd.addNumericField("landmark count", landMarkCount, 0);
		super.prepareDialog(gd);
	}
	
	protected void readDialogResult(GenericDialog gd) {
		threshold = (int) gd.getNextNumber();
		landMarkCount = (int) gd.getNextNumber();
		super.readDialogResult(gd);
	}

	@Override
	public String getFilterName() {
		return "DistanceMapRegisterFilter_";
	}
	
	//Pick n elements from list
	public static <E> List<E> pickNElements(List<E> list, int n) {
		if ( list.size() < n) return list;
		
		List<E> picks = new ArrayList<E>();
		int skips = list.size() / n;

	    for (int i = 0; i < n; i++)
	    {
	        picks.add(list.get(skips * i));
	    }
	    return picks;
	}

	@Override
	protected double calculateInitialDifference() {
		return TransformHelper.calculateDifferenceWithDistanceMap(landMarks, distanceMap, 0, 0, 0, new NearestNeighbourInterpolator());
	}

}
