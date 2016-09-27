import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import ij.IJ;
import ij.ImagePlus;
import ij.gui.GenericDialog;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;

public abstract class AbstractMaskFilter implements PlugInFilter {

	private int radius = 1;

	@Override
	public void run(ImageProcessor ip) {
		byte[] pixels = (byte[]) ip.getPixels();
		int width = ip.getWidth();
		int height = ip.getHeight();

		int threadCount = 4;

		final Image2D inputImage = new ByteImage2D(pixels, width, height);
		final Image2D outputImage = new ByteImage2D(pixels, width, height);

		GenericDialog gd = new GenericDialog("User Input");
		gd.addNumericField("Threads", threadCount, 0);
		gd.addNumericField("Radius", this.radius, 0);
		prepareDialog(gd);
		gd.showDialog();
		if (gd.wasCanceled()) {
			return;
		}
		threadCount = (int) gd.getNextNumber();
		this.radius = (int) gd.getNextNumber();
		readDialogResult(gd);

		final List<Thread> threads = new ArrayList<Thread>();
		final ImageIterator<Integer> iterator = inputImage.iterator();
		final Lock lock = new ReentrantLock();

		for (int i = 0; i < threadCount; i++) {
			threads.add(new Thread() {
				public void run() {
					while (true) {
						int x = 0;
						int y = 0;
						lock.lock();
						try {
							if (iterator.hasNext()) {
								iterator.next();
								x = iterator.indexX();
								y = iterator.indexY();
							} else {
								return;
							}
						} finally {
							lock.unlock();
						}
						Image2D mask = inputImage.getMask(x, y, AbstractMaskFilter.this.radius);
						Integer newValue = transformImagePoint(x, y, mask);

						outputImage.set(x, y, newValue);
					}
				}
			});
		}

		startAndJoin(threads);

		byte[] outPixels = Image2DUtility.convertFromImage2D(outputImage);
		ImageJUtility.showNewImage(outPixels, width, height,
				getFilterName() + " calculated with radius r = " + this.radius);
	}

	protected void readDialogResult(GenericDialog gd) {
	}

	protected void prepareDialog(GenericDialog gd) {
	}

	protected abstract String getFilterName();

	protected abstract int transformImagePoint(int x, int y, Image2D mask);

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
}
