package filter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import ij.IJ;
import ij.ImagePlus;
import ij.gui.GenericDialog;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;
import utility.ByteImage2D;
import utility.Image2D;
import utility.Image2DUtility;
import utility.ImageJUtility;
import utility.Point;

public abstract class AbstractMaskFilter implements PlugInFilter {

	private int radius = 1;
	private int threadCount = 4;

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

		final List<Thread> threads = new ArrayList<Thread>();
		final Iterator<Point<Integer>> iterator = inputImage.pointIterator();

		for (int i = 0; i < threadCount; i++) {
			threads.add(new Thread() {
				public void run() {
					while (true) {
						try {
							Point<Integer> point = iterator.next();
							Image2D mask = inputImage.getMask(point.getX(), point.getY(), AbstractMaskFilter.this.radius);
							Integer newValue = transformImagePoint(point.getX(), point.getY(), mask);

							outputImage.set(point.getX(), point.getY(), newValue);
						}catch(NoSuchElementException ex){
							return;
						}
					}
				}
			});
		}

		startAndJoin(threads);

		byte[] outPixels = Image2DUtility.convertFromImage2D(outputImage);
		ImageJUtility.showNewImage(outPixels, width, height,
				getFilterName() + " calculated with radius r = " + this.radius);
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
	} // showAbout

	private static void startAndJoin(List<Thread> threads) {
		for (int ithread = 0; ithread < threads.size(); ithread++) {
			threads.get(ithread).setPriority(Thread.NORM_PRIORITY);
			threads.get(ithread).start();
		}

		try {
			for (int ithread = 0; ithread < threads.size(); ithread++)
				threads.get(ithread).join();
		} catch (InterruptedException ie) {
			throw new RuntimeException(ie);
		}
	}

	public int getRadius() {
		return this.radius;
	}

	public int getMaxMaskFields() {
		return (this.radius * 2 + 1) * (this.radius * 2 + 1);
	}

	protected abstract String getFilterName();

	protected abstract int transformImagePoint(int x, int y, Image2D mask);
	
	protected void readDialogResult(GenericDialog gd) {
		this.threadCount = (int) gd.getNextNumber();
		this.radius = (int) gd.getNextNumber();
	}

	protected void prepareDialog(GenericDialog gd) {
		gd.addNumericField("Threads", this.threadCount, 0);
		gd.addNumericField("Radius", this.radius, 0);
	}
}
