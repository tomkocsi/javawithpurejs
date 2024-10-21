package kocsist.factory;

public class Position {
	private int x;
	private int y;
	private String label;
	
	public Position(int x, int y, String label) {
		this.x = x;
		this.y = y;
		this.label = label;
	}
	public Position(int x, int y) {
		this.x = x;
		this.y = y;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	
}
