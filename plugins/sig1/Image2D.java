import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;


public class Image2D<T> implements Iterable<ImagePoint<T>> {

	private List<List<T>> image;
	private int width;
	private int height;
	
	public Image2D(List<T> values, int width, int height){
		this.width = width;
		this.height = height;
		List<List<T>> array2D = new ArrayList<List<T>>();
		
		int pixelIdx1D = 0;
		for(int x = 0;x < width; x++){
			List<T> col = new ArrayList<T>();
			for(int y = 0;y < height; y++){
				col.add(values.get(pixelIdx1D));
				pixelIdx1D++;
			}
			array2D.add(col);
		}
		image = array2D;
	}
	
	public int getPointCount(){
		return height * width;
	}
	
	public Image2D<T> getSubImage(int offsetX, int offsetY, int width, int height){
		int subImageWidth = Math.min(this.width - offsetX,width);
		int subImageHeight = Math.min(this.height - offsetY,height);
		
		List<T> imageList = new ArrayList<T>();
		for(int x = 0; x < subImageWidth; x++){
			for(int y = 0; y < subImageHeight; y++){
				imageList.add(image.get(x).get(y));
			}
		}
		return new Image2D<T>(imageList,subImageWidth,subImageHeight);
	}
	
	public Image2D<T> getMask(int x, int y, int radius){
		return getSubImage(x-radius, y - radius, radius*2+1,radius*2+1);
	}
	
	public ImagePoint<T> get(int x, int y){
		return new ImagePoint<T>(x,y,this.image.get(x).get(y));
	}
	
	public void set(ImagePoint<T> point){
		this.image.get(point.getX()).set(point.getY(), point.getValue());
	}

	@Override
	public Iterator<ImagePoint<T>> iterator() {
		return new Iterator<ImagePoint<T>>() {

			private int x = 0;
			private int y = 0;
			
			@Override
			public boolean hasNext() {
				return (x >= width && y >= height);
			}

			@Override
			public ImagePoint<T> next() {
				if(!hasNext()){
					throw new NoSuchElementException();
				}
				ImagePoint<T> ret = Image2D.this.get(x++, y);
				if(x >= width && y < height ){
					x = 0;
					y++;
				}
				return ret;
			}

			
        };
	}

	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return width;
	}

}
