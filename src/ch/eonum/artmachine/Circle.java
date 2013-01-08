package ch.eonum.artmachine;

import java.util.ArrayList;
import java.util.List;

public class Circle {

	public final int depth;
	public final double radius;
	public final double y;
	public final double x;
	public final int index;
	private List<Circle> intersectingCircles;

	public Circle(double x, double y, double radius, int depth, int index) {
		this.x = x;
		this.y = y;
		this.radius = radius;
		this.depth = depth;
		this.index = index;
	}
	
	/**
	 * get the intersection point of two circles. An angle on this circle
	 * is returned. east => 0, south => 0.5 PI, west => PI, north => 1.5 PI. If
	 * the circles do not intersect, are the same or intersect at only one
	 * point, the behavior of this method is not defined. choice: pick the
	 * first or the second intersection
	 * 
	 * @param circle
	 * @param choice
	 * @return
	 */
	public double intersection(Circle circle, boolean choice) {
		double a = this.radius;
		double b = circle.radius;
		double c = Math.sqrt(Math.pow(this.y - circle.y, 2)
				+ Math.pow(this.x - circle.x, 2));
		double d = circle.y - this.y;
		double f = circle.x - this.x;
		double beta = Math.asin(Math.abs(d)/c);
		double alpha = Math.acos((b*b-a*a-c*c)/(-2*a*c));
		double angle = choice ? beta - alpha : beta + alpha;
		if(f < 0 && d > 0) angle = Math.PI - angle;
		if(f < 0 && d < 0) angle = Math.PI + angle;
		if(f > 0 && d < 0) angle = 2* Math.PI - angle;
		if(angle >= 2*Math.PI) angle = angle%(2*Math.PI);
		if(angle < 0.) angle = angle%(2*Math.PI);
		if(angle >= 2*Math.PI) angle = 1.99*Math.PI;
		if(angle < 0.) angle = 0.;
		return angle;
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

	/**
	 * create a list with all circles in circles which do intersect with this
	 * circle.
	 * 
	 * @param circles
	 */
	public void createListOfIntersectingCircles(List<Circle> circles) {
		this.intersectingCircles = new ArrayList<Circle>();
		for (Circle circle : circles) {
			double delta = Math.sqrt(Math.pow(this.y - circle.y, 2)
					+ Math.pow(this.x - circle.x, 2));
			if (delta >= circle.radius + this.radius
					|| delta + Math.min(circle.radius, this.radius) <= Math
							.max(circle.radius, this.radius) || circle == this)
				continue;

			intersectingCircles.add(circle);
		}
	}

	public boolean intersects(Circle circle) {
		return this.intersectingCircles.contains(circle);
	}

}
