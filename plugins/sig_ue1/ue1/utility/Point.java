package ue1.utility;

public class Point<T> {
	private int x;
	private int y;
	private T value;
	
	public Point(int x, int y, T value){
		this.x = x;
		this.y = y;
		this.value = value;
	}
	
	public int getX() {
		return x;
	}
	
	public T getValue() {
		return value;
	}
	
	public int getY() {
		return y;
	}
}
