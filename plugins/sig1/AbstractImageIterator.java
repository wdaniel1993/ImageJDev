public abstract class AbstractImageIterator<T> implements ImageIterator<T>{
	protected int currentX = 0;
	protected int currentY = 0;

	public int indexX() {
		return currentX;
	}

	public int indexY() {
		return currentY;
	}
}
