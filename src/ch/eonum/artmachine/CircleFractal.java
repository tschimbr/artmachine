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
		Circle c = new Circle(cx, cy, cradius, cdepth, circles.size());
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
		Drawing d = new Drawing(rand.nextInt());
		int arcs = this.rand.nextInt(maxArcs) + 1;
		for(int i = 0; i < arcs; i++)
			d.addArc(new Arc(rand.nextInt(circles.size()), widthList.get(rand
					.nextInt(widthList.size())) / 1000., rand.nextDouble() * 2
					* Math.PI, rand.nextDouble() * 2 * Math.PI));
		return d;
	}
	
	public Drawing createRandomDrawingWithIntersections(int maxArcs) {
		Drawing d = new Drawing(rand.nextInt());
		int arcs = (int)(maxArcs * d.getProb("numArcs")) + 1;
		
		double end = 0.0;
		boolean choice = false;
		double rwidth = widthList.get(rand
				.nextInt(widthList.size()));
		Circle previous = null;
		Circle next = null;
		for(int i = 0; i < arcs; i++){
			Circle circle = circles.get(rand.nextInt(circles.size()));
			double start = 0.0;
			if (next == null || rand.nextDouble() < d.getProb("probNewFragment")/2.) {
				previous = getRandomIntersectingCircle(circle);
				start = intersection(circle, previous, rand.nextBoolean());
				rwidth = widthList.get(rand
						.nextInt(widthList.size()));
			} else {
				circle = next;
				start = intersection(circle, previous, choice);
			}
			
			choice = rand.nextBoolean();
			int f = 0;
			do {
				next = getRandomIntersectingCircle(circle);
				end = intersection(circle, next, choice);
				f++;
			} while (f < 10 && Math.abs(start - end) > d.getProb("probArcSize") + 2.0 * Math.PI * rand.nextDouble());
			if(start > end){
				double temp = start;
				start = end;
				end = temp;
			}
			d.addArc(new Arc(circle.index, rwidth / 1000., start, end));
			previous = circle;
		}
		return d;
	}

	private double intersection(Circle circle1, Circle circle2, boolean choice) {
		double a = circle1.radius;
		double b = circle2.radius;
		double c = Math.sqrt(Math.pow(circle1.y - circle2.y, 2)
				+ Math.pow(circle1.x - circle2.x, 2));
		double d = circle2.y - circle1.y;
		double f = circle2.x - circle1.x;
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
