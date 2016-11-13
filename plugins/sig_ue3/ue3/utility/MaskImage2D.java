package ue3.utility;

import ue3.utility.Image2D;

/**
 * Proxies an Image2D with an offset, width and height
 * represents a subimage
 */
public class MaskImage2D extends Image2D {

	private Image2D image;
	private int offsetX;
	private int offsetY;
	
	public MaskImage2D(Image2D image, int offsetX, int offsetY, int width, int height) {
		super(width, height);
		this.offsetX = offsetX;
		this.offsetY = offsetY;
		this.image = image;
	}

	@Override
	public Integer get(int x, int y) {
		return image.get(x+offsetX, y+offsetY);
	}

	@Override
	public void set(int x, int y, Integer point) {
		image.set(x+offsetX, y+offsetY, point);
	}

}
