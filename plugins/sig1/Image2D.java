import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;


public class Image2D implements Iterable<Integer> {

	private List<List<Integer>> image;
	private int width;
	private int height;
	
	public Image2D(List<Integer> values, int width, int height){
		this.width = width;
		this.height = height;
		List<List<Integer>> array2D = new ArrayList<List<Integer>>();
		
		int pixelIdx1D = 0;
		for(int x = 0;x < width; x++){
			List<Integer> col = new ArrayList<Integer>();
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
	
	public Image2D getSubImage(int offsetX, int offsetY, int width, int height){
		int subImageOffsetX = Math.max(offsetX,0);
		int subImageOffsetY = Math.max(offsetY,0);
		int subImageWidth = Math.min(this.width - subImageOffsetX,width);
		int subImageHeight = Math.min(this.height - subImageOffsetY,height);	
		
		List<Integer> imageList = new ArrayList<Integer>();
		for(int x = 0; x < subImageWidth; x++){
			for(int y = 0; y < subImageHeight; y++){
				imageList.add(image.get(x+subImageOffsetX).get(y+subImageOffsetY));
			}
		}
		return new Image2D(imageList,subImageWidth,subImageHeight);
	}
	
	public Image2D getMask(int x, int y, int radius){
		return getSubImage(x-radius, y - radius, radius*2+1,radius*2+1);
	}
	
	public Integer get(int x, int y){
		return this.image.get(x).get(y);
	}
	
	public void set(int x, int y, Integer point){
		this.image.get(x).set(y, point);
	}
	
	public List<Integer> asList(){
		List<Integer> list = new ArrayList<Integer>();
		
		for(Integer point : this){
			list.add(point);
		}
		return list;
	}

	@Override
	public ImageIterator<Integer> iterator() {
		return new AbstractImageIterator<Integer>() {

			private int x = 0;
			private int y = 0;
			
			@Override
			public boolean hasNext() {
				return (x < width && y < height);
			}

			@Override
			public Integer next() {
				if(!hasNext()){
					throw new NoSuchElementException();
				}
				Integer ret = Image2D.this.get(x++, y);
				if(x >= width && y < height ){
					x = 0;
					y++;
				}
				return ret;
			}

			
        };
	}
	
	public ImageIterator<Integer> rotatedIterator() {
		return new AbstractImageIterator<Integer>() {

			private int nextX = 0;
			private int nextY = 0;
		
			@Override
			public boolean hasNext() {
				return (nextX < width && nextY < height);
			}

			@Override
			public Integer next() {
				if(!hasNext()){
					throw new NoSuchElementException();
				}
				currentX = nextX;
				currentY = nextY;
				Integer ret = Image2D.this.get(nextX, nextY++);
				if(nextY >= height && nextX < width ){
					nextX++;
					nextY=0;
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
