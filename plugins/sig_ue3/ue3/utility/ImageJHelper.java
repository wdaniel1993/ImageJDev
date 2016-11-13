package ue3.utility;

import java.util.ArrayList;
import java.util.List;

public class ImageJHelper {
	public static List<Image2D> splitImageVertical(Image2D image){
		List<Image2D> ret = new ArrayList<Image2D>();
		int width = image.getWidth();
		int height = image.getHeight();
		int halfWidth = image.getWidth() / 2;
		ret.add(image.getSubImageByIndizes(0, 0, halfWidth-1, height-1));
		ret.add(image.getSubImageByIndizes(width - halfWidth, 0, width - 1, height-1));
		return null;
	}
}
