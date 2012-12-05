package ch.eonum.artmachine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;

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
	private int generation;

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
		generation = 0;
	}

	/**
	 * recursive function for the creation of the circle fractal.
	 * @param cx current x circle center
	 * @param cy current y circle center
	 * @param cradius current circle radius
	 * @param cdepth current fractal depth
	 */
	private void makeCircle(double cx, double cy, double cradius, int cdepth) {
		if (cdepth == 0)
			return;
		String circle = Math.floor(cx * 100) + "-" + Math.floor(cy * 100) + "-"
				+ Math.floor(cradius * 100);
		if (circleskeys.contains(circle))
			return;
		if (Math.sqrt((cy - y) * (cy - y) + (cx - x) * (cx - x)) > cradius
				+ radius)
			return;

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
		String circle = Math.floor(cx * 100) + "-" + Math.floor(cy * 100) + "-"
				+ Math.floor(cradius * 100);
		if (circleskeys.contains(circle))
			return;
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
					* Math.PI, rand.nextDouble() * 2 * Math.PI), 0);
		return d;
	}
	
	private Drawing createRandomDrawingWithIntersections(Drawing d, int maxArcs) {
		int arcs = (int) (maxArcs * d.getProb("numArcs")) + 1;

		double end = 0.0;
		boolean choice = false;
		double rwidth = getRandomWidth(d);
		Circle previous = null;
		Circle next = null;
		int sequence = -1;
		for (int i = 0; i < arcs; i++) {
			Circle circle = randomCircle(d);
			double start = 0.0;
			if (next == null
					|| rand.nextDouble() < d.getProb("probNewFragment") / 2.) {
				previous = getRandomIntersectingCircle(circle, d);
				start = circle.intersection(previous, rand.nextBoolean());
				rwidth = getRandomWidth(d);
				sequence++;
			} else {
				circle = next;
				start = circle.intersection(previous, choice);
			}

			choice = rand.nextBoolean();
			int f = 0;
			do {
				next = getRandomIntersectingCircle(circle, d);
				end = circle.intersection(next, choice);
				f++;
			} while (f < 10
					&& Math.abs(start - end) > d.getProb("probArcSize") + 2.0
							* Math.PI * rand.nextDouble());
			if (start > end) {
				double temp = start;
				start = end;
				end = temp;
			}
			d.addArc(new Arc(circle.index, rwidth / 1000., start, end), sequence);
			previous = circle;
		}
		return d;
	}

	/**
	 * Create a semi-random drawing. Many circles intersect.
	 * @param maxArcs
	 * @return
	 */
	public Drawing createRandomDrawingWithIntersections(int maxArcs) {
		Drawing d = new Drawing(rand.nextInt());
		return this.createRandomDrawingWithIntersections(d, maxArcs);
	}

	/**
	 * get a random width according the probability distribution of the given
	 * drawing.
	 * 
	 * @param d
	 * @return
	 */
	private double getRandomWidth(Drawing d) {
		int maxWidth = -1;
		double max = Double.NEGATIVE_INFINITY;
		for(int i = 0; i < widthList.size(); i++){
			double prob = d.getProb("widthProb" + i) * rand.nextDouble();
			if(prob > max){
				max = prob;
				maxWidth = i;
			}
		}
		return widthList.get(maxWidth);
	}

	/**
	 * get a random circle according the probability distribution of the given
	 * drawing.
	 * 
	 * @param d
	 * @return
	 */
	private Circle randomCircle(Drawing d) {
		if(d.getProb("circleSizeRatio") < rand.nextDouble())
			circles.get(rand.nextInt(circles.size()));
		int maxDepth = -1;
		double max = Double.NEGATIVE_INFINITY;
		for(int i = 0; i < circlesByDepth.size(); i++){
			double prob = d.getProb("depthProb" + i) * rand.nextDouble();
			if(prob > max){
				max = prob;
				maxDepth = i;
			}
		}
		List<Circle> cd = this.circlesByDepth.get(maxDepth);
		return cd.get(rand.nextInt(cd.size()));
	}

	/**
	 * choose a random circle from the fractal which intersects the given circle
	 * at exactly two points.
	 * 
	 * @param circle1
	 * @return
	 */
	private Circle getRandomIntersectingCircle(Circle circle1, Drawing d) {
		Circle circle = null;
		double delta = 0.;
		do {
			circle = randomCircle(d);
			delta = Math.sqrt(Math.pow(circle1.y - circle.y, 2)
					+ Math.pow(circle1.x - circle.x, 2));
		} while (delta >= circle.radius + circle1.radius
				|| delta + Math.min(circle.radius, circle1.radius) <= Math.max(
						circle.radius, circle1.radius) || circle == circle1);
		return circle;
	}

	/**
	 * create some initial random drawings which can later be further developed
	 * using genetic, neural or other algorithms.
	 * 
	 * @param numCases
	 *            number of drawings to be generated
	 * @param maxArcs
	 *            maximum number of arcs for each drawing
	 * @param drawings 
	 * @param dbName
	 *            mongo db name
	 * @param host
	 *            host name
	 * @param collection
	 *            mongodb collection name
	 * @throws IOException
	 * @throws RuntimeException
	 */
	public void createInitialRandomDrawings(int numCases, int maxArcs, DBCollection drawings) throws IOException,
			RuntimeException {

		for (int i = 0; i < numCases; i++) {
			BasicDBObject drawing = new BasicDBObject();
			Drawing d = this.createRandomDrawingWithIntersections(maxArcs);
			drawing.append("arcs", d.arcsToJSON());
			drawing.append("arcsHierarchical", d.arcsToJSONHierarchical());
			drawing.append("meta", d.metaToJSON());
			drawings.insert(drawing);
		}
	}
	
	/**
	 * create as many drawings until we have numDrawings drawings. if we have
	 * some drawings in the database, create new ones by geneticaly compose new
	 * drawings from the old. if not, create some intial random drawings.
	 * 
	 * @param numDrawings
	 *            number of drawings to be generated
	 * @param maxArcs
	 *            maximum number of arcs for each drawing
	 * @param dbName
	 *            mongo db name
	 * @param host
	 *            host name
	 * @param collection
	 *            mongodb collection name
	 * @throws IOException
	 * @throws RuntimeException
	 */
	public void makeDrawings(int numDrawings, int maxArcs,
			String dbName, String host, String collection) throws IOException,
			RuntimeException {
		Mongo m = new Mongo(host);
		DB db = m.getDB(dbName);
		DBCollection drawings = db.getCollection(collection);
		long count = drawings.getCount();
		if(count <= 0)
			createInitialRandomDrawings(numDrawings, maxArcs, drawings);
		else {
			List<Drawing> oldDrawings = new ArrayList<Drawing>();
			DBCursor cursor = drawings.find();
			while (cursor.hasNext()){
				DBObject bdo = cursor.next();
				oldDrawings.add(Drawing.createFromJSON(
						bdo.get("arcsHierarchical").toString(), bdo.get("meta")
								.toString(), rand.nextInt()));
			}
			cursor.close();
			BasicDBObject bdo = createGenerationStatistics(oldDrawings);
			DBCollection stats = db.getCollection("generationstats");
			stats.insert(bdo);
			List<Drawing> newDrawings = makeDrawings(oldDrawings, numDrawings - count, maxArcs);
			for(Drawing d : newDrawings){
				BasicDBObject drawing = new BasicDBObject();
				drawing.append("arcs", d.arcsToJSON());
				drawing.append("arcsHierarchical", d.arcsToJSONHierarchical());
				drawing.append("meta", d.metaToJSON());
				drawings.insert(drawing);
			}
		}
		
		m.close();
	}

	private BasicDBObject createGenerationStatistics(List<Drawing> oldDrawings) {
		Map<String, Double> stats = new HashMap<String, Double>();
		for(Drawing d : oldDrawings)
			for(String key : d.getAllProbabilities())
				if(stats.containsKey(key))
					stats.put(key, stats.get(key) + d.getProb(key));
				else
					stats.put(key, d.getProb(key));
		
		BasicDBObject bdo = new BasicDBObject();
		bdo.put("generation", generation++);
		for(String key : stats.keySet())
			bdo.put(key, stats.get(key) / oldDrawings.size());
		return bdo;
	}

	private List<Drawing> makeDrawings(List<Drawing> oldDrawings, long numDrawingsToGenerate, int maxArcs) {
		List<Drawing> newDrawings = new ArrayList<Drawing>();
		
		double newRandom = 0.0;
		double crossover = 0.0;
		double mutations = 0.0;
		
		for(Drawing old : oldDrawings){
			newRandom += old.getProb("newRandom");
			crossover += old.getProb("crossover");
			mutations += old.getProb("mutation");
		}
		
		double total = newRandom + crossover + mutations;
		newRandom /= total;
		crossover /= total;
		mutations /= total;
		newRandom = Math.max(0.1, newRandom);
		newRandom = Math.min(0.9, newRandom);
		crossover = Math.max(0.1, crossover);
		crossover = Math.min(0.9, crossover);
		mutations = Math.max(0.1, mutations);
		mutations = Math.min(0.9, mutations);
		
		for(int i = 0; i < numDrawingsToGenerate; i++){
			double generation = rand.nextDouble();
			Drawing d = null;
			if(generation < newRandom) {
				d = new Drawing(rand.nextInt());
				d.putProb("newRandom", 1.0);
			}
			else if (generation < newRandom + mutations) {
				d = new Drawing(oldDrawings.get(rand.nextInt(oldDrawings.size())), rand.nextInt());
				d.mutateMeta();
				d.putProb("mutation", 1.0);
			} else {
				Drawing d1 = oldDrawings.get(rand.nextInt(oldDrawings.size()));
				Drawing d2 = oldDrawings.get(rand.nextInt(oldDrawings.size()));
				d = d1.crossover(d2);
				d.putProb("crossover", 1.0);
			}
			
			newDrawings.add(this.createRandomDrawingWithIntersections(d, maxArcs));
		}
		return newDrawings;
	}
}
