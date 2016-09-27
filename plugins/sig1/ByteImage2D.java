
public class ByteImage2D extends Image2D{

	private int[][] image;
	
	public ByteImage2D(byte[] values, int width, int height){
		super(width, height);
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
	
	public ByteImage2D(int[] values, int width, int height){
		super(width, height);
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
	
	public Integer get(int x, int y){
		return this.image[x][y];
	}
	
	public void set(int x, int y, Integer point){
		this.image[x][y] = point;
	}
	
}
