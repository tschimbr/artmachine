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

}
