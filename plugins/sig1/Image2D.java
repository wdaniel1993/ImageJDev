import java.util.NoSuchElementException;

public class Image2D implements Iterable<Integer> {

	private int[][] image;
	private int width;
	private int height;
	
	public Image2D(byte[] values, int width, int height){
		this.width = width;
		this.height = height;
		int[][] array2D = new int[width][height];
		
		int pixelIdx1D = 0;
		for(int y = 0;y < height; y++){
			for(int x = 0;x < width; x++){
				array2D[x][y] = values[pixelIdx1D];
				if (array2D[x][y]  < 0) {
					array2D[x][y]  += 256;
				}
				pixelIdx1D++;
			}
		}
		image = array2D;
	}
	
	public Image2D(int[] values, int width, int height){
		this.width = width;
		this.height = height;
		int[][] array2D = new int[width][height];
		
		int pixelIdx1D = 0;
		for(int x = 0;x < width; x++){
			for(int y = 0;y < height; y++){
				array2D[x][y] = values[pixelIdx1D];
				pixelIdx1D++;
			}
		}
		
		image = array2D;
	}
	
	public int getPointCount(){
		return height * width;
	}
	
	public Image2D getSubImage(int offsetX, int offsetY, int width, int height){
		int subImageOffsetX = Math.max(offsetX,0);
		int subImageOffsetY = Math.max(offsetY,0);
		int subImageWidth = Math.min(this.width - subImageOffsetX,width - (subImageOffsetX-offsetX));
		int subImageHeight = Math.min(this.height - subImageOffsetY,height - (subImageOffsetY-offsetY));
		
		int[] imageArr = new int[subImageWidth*subImageHeight];
		int i = 0;
		for(int x = 0; x < subImageWidth; x++){
			for(int y = 0; y < subImageHeight; y++, i++){
				imageArr[i] = image[x+subImageOffsetX][y+subImageOffsetY];
			}
		}
		
		return new Image2D(imageArr,subImageWidth,subImageHeight);
	}
	
	public Image2D getMask(int x, int y, int radius){
		return getSubImage(x-radius, y - radius, radius*2+1,radius*2+1);
	}
	
	public Integer get(int x, int y){
		return this.image[x][y];
	}
	
	public void set(int x, int y, Integer point){
		this.image[x][y] = point;
	}
	
	public int[] asArray(){
		int[] intArr = new int[getPointCount()];
		
		int i = 0;
		for(Integer point : this){
			intArr[i++] = point;
		}
		return intArr;
	}

	@Override
	public ImageIterator<Integer> iterator() {
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
				Integer ret = Image2D.this.get(nextX++, nextY);
				if(nextX >= width && nextY < height ){
					nextX = 0;
					nextY++;
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
