package ch.eonum.artmachine;

public class Circle {

	public final int depth;
	public final double radius;
	public final double y;
	public final double x;
	public final int index;

	public Circle(double x, double y, double radius, int depth, int index) {
		this.x = x;
		this.y = y;
		this.radius = radius;
		this.depth = depth;
		this.index = index;
	}
	
	public String toJSON() {
		return "{\n" + 
				"  \"x\": " + x + ",\n" + 
				"  \"y\": " + y + ",\n" + 
				"  \"radius\": " + radius + ",\n" + 
				"  \"depth\": " + depth + ",\n" + 
				"  \"index\": " + index + "\n" + 
				"}";
	}
	
	@Override
	public String toString(){
		return toJSON();
	}

}
