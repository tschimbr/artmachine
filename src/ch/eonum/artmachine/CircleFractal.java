package ch.eonum.artmachine;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * circle fractal. ground structure for arcs and areas.
 * Low Complexity Art by Jürgen Schmidhuber
 * 
 * @author tim
 *
 */
public class CircleFractal {

	private final int radius;
	private final int depth;
	private final int width;
	private final int x;
	private final int y;
	private Set<String> circleskeys;
	private List<Circle> circles;
	private List<List<Circle>> circlesByDepth;
	private Set<Integer> possibleWidths;
	private List<Integer> widthList;
	private Random rand;

	public CircleFractal(int radius, int depth, int width, int x, int y) {
		this.radius = radius;
		this.depth = depth;
		this.width = width;
		this.x = x;
		this.y = y;
		this.circleskeys = new HashSet<String>();
		this.circles = new ArrayList<Circle>();
		this.circlesByDepth = new ArrayList<List<Circle>>();
		this.rand = new Random(23);
		for(int i = 0; i < depth; i++)
			circlesByDepth.add(new ArrayList<Circle>());
		this.possibleWidths = new HashSet<Integer>();
		this.widthList= new ArrayList<Integer>();
		this.makeCircle(x, y, radius, depth);
		for(Integer each : possibleWidths)
			widthList.add(each);
	}

	private void makeCircle(double cx, double cy, double cradius, int cdepth) {
		if(cdepth == 0) return;
		String circle = Math.floor(cx*100) + "-" + Math.floor(cy*100) + "-" + Math.floor(cradius*100);
		if(circleskeys.contains(circle)) return;
		if(Math.sqrt((cy - y)*(cy - y)+(cx - x)*(cx - x)) > cradius + radius) return;

		double lineWidth = width / (Math.pow(2.0, depth-cdepth+2));
		this.possibleWidths.add((int)(lineWidth*1000.));

		storeCircle(cx, cy, cradius, cdepth);
		storeCircle(cx, cy+cradius, cradius, cdepth);
		storeCircle(cx, cy-cradius, cradius, cdepth);
		double xlag = Math.sqrt(cradius*cradius - (cradius/2)*(cradius/2));
		storeCircle(cx+xlag, cy+cradius/2, cradius, cdepth);
		storeCircle(cx+xlag, cy-cradius/2, cradius, cdepth);
		storeCircle(cx-xlag, cy+cradius/2, cradius, cdepth);
		storeCircle(cx-xlag, cy-cradius/2, cradius, cdepth);
		
		makeCircle(cx, cy, cradius/2., cdepth - 1);
		makeCircle(cx, cy+cradius, cradius/2., cdepth - 1);
		makeCircle(cx, cy-cradius, cradius/2., cdepth - 1);
		makeCircle(cx+xlag, cy+cradius/2., cradius/2., cdepth - 1);
		makeCircle(cx+xlag, cy-cradius/2., cradius/2., cdepth - 1);
		makeCircle(cx-xlag, cy+cradius/2., cradius/2., cdepth - 1);
		makeCircle(cx-xlag, cy-cradius/2., cradius/2., cdepth - 1);
	}

	private void storeCircle(double cx, double cy, double cradius, int cdepth) {
		String circle = Math.floor(cx*100) + "-" + Math.floor(cy*100) + "-" + Math.floor(cradius*100);
		if(circleskeys.contains(circle)) return;
		Circle c = new Circle(cx, cy, cradius, cdepth);
		circles.add(c);
		circlesByDepth.get(depth - cdepth).add(c);
		circleskeys.add(circle);
	}

	public List<Circle> getCircles() {
		return this.circles;
	}
	
	public List<Circle> getCirclesByDepth(int i) {
		return this.circlesByDepth.get(i);
	}

	public Set<Integer> getPossibleWidths() {
		return this.possibleWidths;
	}

	/**
	 * create a random drawing.
	 * @param maxArcs
	 * @return
	 */
	public Drawing createRandomDrawing(int maxArcs) {
		Drawing d = new Drawing();
		int arcs = this.rand.nextInt(maxArcs) + 1;
		for(int i = 0; i < arcs; i++)
			d.addArc(new Arc(rand.nextInt(circles.size()), widthList.get(rand
					.nextInt(widthList.size())) / 1000., rand.nextDouble() * 2
					* Math.PI, rand.nextDouble() * 2 * Math.PI));
		return d;
	}
	
	public Drawing createRandomDrawingWithIntersections(int maxArcs) {
		Drawing d = new Drawing();
		int arcs = this.rand.nextInt(maxArcs) + 1;
		for(int i = 0; i < arcs; i++){
			int circleIndex = rand.nextInt(circles.size());
			Circle circle1 = circles.get(circleIndex);
			double start = randomIntersection(circle1);
			double end = randomIntersection(circle1);
			d.addArc(new Arc(circleIndex, widthList.get(rand
					.nextInt(widthList.size())) / 1000., start, end));
		}
		return d;
	}

	/**
	 * get a random intersection on this circle.
	 * 
	 * 
	 * @param circle1
	 * @return angle [0,2*PI]
	 */
	private double randomIntersection(Circle circle1) {
		Circle circle2 = getRandomIntersectingCircle(circle1);
		double b = circle1.radius;
		double a = circle2.radius;
		double c = Math.sqrt(Math.pow(circle1.y - circle2.y, 2)
				+ Math.pow(circle1.x - circle2.x, 2));
		double d = circle2.y - circle1.y;
		double beta = Math.acos(Math.abs(d)/c);
		double alpha = Math.acos((b*b-a*a-c*c)/(-2*a*c));
		double angle = rand.nextBoolean() ? alpha - beta : alpha + beta;
		if(angle >= 2*Math.PI) angle -= Math.PI;
		if(angle < 0) angle += Math.PI;
		return angle;
	}

	private Circle getRandomIntersectingCircle(Circle circle1) {
		Circle circle = null;
		double delta = 0.;
		do {
			circle = circles.get(rand.nextInt(circles.size()));
			delta = Math.sqrt(Math.pow(circle1.y - circle.y, 2)
					+ Math.pow(circle1.x - circle.x, 2));
		} while (delta > circle.radius + circle1.radius
				|| delta + Math.min(circle.radius, circle1.radius) < Math.max(
						circle.radius, circle1.radius)
						|| circle == circle1);
		return circle;
	}
}
