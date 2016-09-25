
public class ImagePoint<T> {
	private int x;
	private int y;
	private T value;
	
	public ImagePoint(int x, int y, T value){
		this.x = x;
		this.y = y;
		this.value = value;
	}
	
	public T getValue() {
		return value;
	}
	
	public int getY() {
		return y;
	}
	
	public int getX() {
		return x;
	}
}
