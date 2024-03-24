package drugi;

public class Location {
	private int row;
	private int column;
	
	public Location(int x, int y) {
		row=x;
		column=y;
	}
	
	public void reallocate(int plusX, int plusY) {
		row+=plusX;
		column+=plusY;
	}
	
	public int getX() {
		return row;
	}
	
	public int getY() {
		return column;
	}
	
	public void setX(int x) {
		row=x;
	}
	
	public void setY(int y) {
		column=y;
	}

}
