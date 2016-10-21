
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import ij.gui.GenericDialog;
import ue1.utility.Image2D;
import ue1.utility.Point;

/**
 * AbstractMaskFilter is the base class for all filters which are using the surrounding pixels for value calculation
 */
public abstract class AbstractMaskFilter extends AbstractBaseFilter {

	private int radius = 1;
	private int threadCount = 4;
	
	public void processImage(final Image2D inputImage, final Image2D outputImage) {
		final List<Thread> threads = new ArrayList<Thread>();
		final Iterator<Point<Integer>> iterator = inputImage.pointIterator();

		// Starts the user defined number of threads, which iterator together over the image points
		for (int i = 0; i < threadCount; i++) {
			threads.add(new Thread() {
				public void run() {
					while (true) {
						try {
							Point<Integer> point = iterator.next(); // get point from iterator
							Image2D mask = inputImage.getMask(point.getX(), point.getY(), AbstractMaskFilter.this.radius); // get mask surround the point
							Integer newValue = transformImagePoint(point.getX(), point.getY(), mask); // transform the point considering the mask

							outputImage.set(point.getX(), point.getY(), newValue); // set the newValue in the output image
						}catch(NoSuchElementException ex){ //iterator came to an end
							return;
						}
					}
				}
			});
		}

		startAndJoin(threads);
	}

	/*
	 * Starts the threads and blocks the main thread by joining the calculation threads
	 */
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

	protected abstract int transformImagePoint(int x, int y, Image2D mask);
	
	public void readDialogResult(GenericDialog gd) {
		this.radius = (int) gd.getNextNumber();
		this.threadCount = (int) gd.getNextNumber();
		super.readDialogResult(gd);
	}

	public void prepareDialog(GenericDialog gd) {
		gd.addNumericField("Radius", this.radius, 0);
		gd.addNumericField("Threads", this.threadCount, 0);
		super.prepareDialog(gd);
	}
}
