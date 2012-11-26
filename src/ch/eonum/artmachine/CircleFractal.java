package ch.eonum.artmachine;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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

	public CircleFractal(int radius, int depth, int width, int x, int y) {
		this.radius = radius;
		this.depth = depth;
		this.width = width;
		this.x = x;
		this.y = y;
		this.circleskeys = new HashSet<String>();
		this.circles = new ArrayList<Circle>();
		this.circlesByDepth = new ArrayList<List<Circle>>();
		for(int i = 0; i < depth; i++)
			circlesByDepth.add(new ArrayList<Circle>());
		this.possibleWidths = new HashSet<Integer>();
		this.makeCircle(x, y, radius, depth);
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

}
