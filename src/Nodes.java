// This class hold the id, x, and y values of the points in the coordinate system.
public class Nodes {
	
	// Our variables.
	int id;
	int x;
	int y;
	
	// This is our constructor.
	public Nodes(int id, int x, int y) {
		this.id = id;
		this.x = x;
		this.y = y;
	}

	// Here we got our getter and setter methods
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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
	
}
