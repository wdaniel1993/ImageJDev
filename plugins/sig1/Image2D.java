import java.util.Iterator;
import java.util.NoSuchElementException;

public abstract class Image2D implements Iterable<Integer> {
	private int width;
	private int height;
	
	public Image2D(int width, int height) {
		this.width = width;
		this.height = height;
	}
	
	public int getPointCount(){
		return height * width;
	}
	
	public Image2D getSubImage(int offsetX, int offsetY, int width, int height){
		int subImageOffsetX = Math.max(offsetX,0);
		int subImageOffsetY = Math.max(offsetY,0);
		int subImageWidth = Math.min(this.width - subImageOffsetX,width - (subImageOffsetX-offsetX));
		int subImageHeight = Math.min(this.height - subImageOffsetY,height - (subImageOffsetY-offsetY));
		
		return new MaskImage2D(this,subImageOffsetX, subImageOffsetY, subImageWidth,subImageHeight);
	}
	
	public Image2D getMask(int x, int y, int radius){
		return getSubImage(x-radius, y - radius, radius*2+1,radius*2+1);
	}
	
	public abstract Integer get(int x, int y);
	
	public abstract void set(int x, int y, Integer point);
	
	public int[] asArray(){
		int[] intArr = new int[getPointCount()];
		
		int i = 0;
		for(Integer point : this){
			intArr[i++] = point;
		}
		return intArr;
	}

	@Override
	public Iterator<Integer> iterator() {
		return new Iterator<Integer>() {

			private int nextX = 0;
			private int nextY = 0;
			
			@Override
			public boolean hasNext() {
				return (nextX < width && nextY < height);
			}

			@Override
			public synchronized Integer next() {
				if(!hasNext()){
					throw new NoSuchElementException();
				}
				Integer ret = Image2D.this.get(nextX++, nextY);
				if(nextX >= width && nextY < height ){
					nextX = 0;
					nextY++;
				}
				return ret;
			}		
        };
	}
	
	public Iterator<Integer> rotatedIterator() {
		return new Iterator<Integer>() {

			private int nextX = 0;
			private int nextY = 0;
		
			@Override
			public boolean hasNext() {
				return (nextX < width && nextY < height);
			}

			@Override
			public synchronized Integer next() {
				if(!hasNext()){
					throw new NoSuchElementException();
				}
				Integer ret = Image2D.this.get(nextX, nextY++);
				if(nextY >= height && nextX < width ){
					nextX++;
					nextY=0;
				}
				return ret;
			}
        };
	}
	
	public Iterator<Point<Integer>> pointIterator() {
		return new Iterator<Point<Integer>>() {
				private int nextX = 0;
				private int nextY = 0;
				
				@Override
				public boolean hasNext() {
					return (nextX < width && nextY < height);
				}

				@Override
				public synchronized Point<Integer> next() {
					if(!hasNext()){
						throw new NoSuchElementException();
					}
					Point<Integer> ret = new Point<Integer>(nextX, nextY, Image2D.this.get(nextX, nextY));
					nextX++;
					if(nextX >= width && nextY < height ){
						nextX = 0;
						nextY++;
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
