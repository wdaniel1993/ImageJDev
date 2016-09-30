package filter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import ij.gui.GenericDialog;
import utility.Image2D;
import utility.Point;

public abstract class AbstractMaskFilter extends AbstractBaseFilter {

	private int radius = 1;
	private int threadCount = 4;
	
	public void processImage(final Image2D inputImage, final Image2D outputImage) {
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
	}

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
		this.threadCount = (int) gd.getNextNumber();
		this.radius = (int) gd.getNextNumber();
	}

	public void prepareDialog(GenericDialog gd) {
		gd.addNumericField("Threads", this.threadCount, 0);
		gd.addNumericField("Radius", this.radius, 0);
	}
}
