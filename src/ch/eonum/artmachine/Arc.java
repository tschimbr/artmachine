package ch.eonum.artmachine;

/**
 * arc in a fractal.
 * @author tim
 *
 */
public class Arc {
	public final int circle;
	public final double width;
	public final double start;
	public final double end;

	public Arc(int circle, double width, double start, double end){
		this.circle = circle;
		this.width = width;
		this.start = start;
		this.end = end;
	}

	public String toJSON() {
		return "{\n" + 
				"  \"circle\": " + circle + ",\n" + 
				"  \"width\": " + width + ",\n" + 
				"  \"start\": " + start + ",\n" + 
				"  \"end\": " + end + "\n" + 
				"}";
	}
}
