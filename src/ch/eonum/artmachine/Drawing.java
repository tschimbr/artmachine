package ch.eonum.artmachine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

/**
 * Set of arcs and areas describing a drawing on a fractal.
 * @author tim
 *
 */
public class Drawing {
	private static final String[] PROBABILITIES = { "numArcs",
			"probNewFragment", "probArcSize", "circleSizeRatio" };
	private List<Arc> arcs;
	/** arcs listed hierarchicaly. all arcs within one list are connected. */
	private List<List<Arc>> arcsHierarchical;
	private Map<String, Double> probabilities;
	private Random rand;

	public Drawing(int seed){
		this.setArcs(new ArrayList<Arc>());
		this.arcsHierarchical = new ArrayList<List<Arc>>();
		this.rand = new Random(seed);
		this.probabilities = new HashMap<String, Double>();
		for(String each : Drawing.PROBABILITIES)
			this.probabilities.put(each, rand.nextDouble());
	}
	
	/**
	 * copy the probabilities of drawing.
	 * 
	 * @param drawing
	 * @param seed
	 */
	public Drawing(Drawing drawing, int seed) {
		this.setArcs(new ArrayList<Arc>(drawing.arcs));
		this.arcsHierarchical = new ArrayList<List<Arc>>(drawing.arcsHierarchical);
		this.rand = new Random(seed);
		this.probabilities = new HashMap<String, Double>(drawing.probabilities);
	}

	/**
	 * add an arc. numSequence is the number of the sequence of connected arcs.
	 * if it does not exist it is created.
	 * 
	 * @param arc
	 * @param numSequence
	 * @return
	 */
	public boolean addArc(Arc arc, int numSequence){
		if(this.arcsHierarchical.size() == numSequence)
			this.arcsHierarchical.add(numSequence, new ArrayList<Arc>());
		this.arcsHierarchical.get(numSequence).add(arc);
		return this.arcs.add(arc);
	}
	
	/**
	 * get the probability of some creational feature.
	 * lazy random initialization if not present.
	 * 
	 * @param prob
	 * @return
	 */
	public double getProb(String prob){
		if(!this.probabilities.containsKey(prob))
			this.probabilities.put(prob, rand.nextDouble());
		return this.probabilities.get(prob);
	}

	public List<Arc> getArcs() {
		return arcs;
	}

	public void setArcs(List<Arc> arcs) {
		this.arcs = arcs;
	}

	public String arcsToJSON() {
		return arcsToJSON(arcs);
	}

	private String arcsToJSON(List<Arc> arcList) {
		String json ="[";
		for(int i = 0; i < arcList.size() - 1; i++)
			json += arcList.get(i).toJSON() + ",\n";
		if(arcList.size() >= 1)
			json += arcList.get(arcList.size() - 1).toJSON();
		return json + "]";
	}

	public String metaToJSON() {
		String json = "{\n";
		String[] probs = probabilities.keySet().toArray(new String[1]);
		for(int i = 0; i < probs.length - 1; i++)
			json += "  \"" + probs[i] + "\": " + probabilities.get(probs[i]) + ",\n";
		if(probs.length >= 1)
			json += "  \"" + probs[probs.length-1] + "\": " + probabilities.get(probs[probs.length-1]);
		return json + "\n}";
	}

	public String arcsToJSONHierarchical() {
		String json ="[";
		for(int i = 0; i < this.arcsHierarchical.size() - 1; i++)
			json += arcsToJSON(arcsHierarchical.get(i)) + ", ";
		if(arcsHierarchical.size() >= 1)
			json += arcsToJSON(arcsHierarchical.get(arcsHierarchical.size() - 1));
		return json + "]";
	}

	public static Drawing createFromJSON(String arcsJSON,
			String metaJSON, int seed) throws IOException {
		Drawing d = new Drawing(seed);
		ObjectMapper mapper = new ObjectMapper();
		d.loadArcs(mapper.readValue(arcsJSON, new TypeReference<List<Object>>() { }));
		d.loadMeta(mapper.readValue(metaJSON, new TypeReference<Map<String, Object>>() { }));
		return d;
	}

	private void loadMeta(Object readValue) {
		@SuppressWarnings("unchecked")
		Map<String, Double> meta = (Map<String, Double>) readValue;
		this.probabilities = meta;
	}

	/**
	 * load arcs from parsed json.
	 * @param readValue
	 */
	@SuppressWarnings("unchecked")
	private void loadArcs(Object value) {
		List<List<Map<String, Object>>> sequences = (List<List<Map<String, Object>>>) value;
		this.arcs = new ArrayList<Arc>();
		this.arcsHierarchical = new ArrayList<List<Arc>>();
		for(List<Map<String, Object>> seq : sequences){
			ArrayList<Arc> cList = new ArrayList<Arc>();
			this.arcsHierarchical.add(cList);
			for(Map<String, Object> arcMap : seq){
				double end = (Double) arcMap.get("end");
				double start = (Double) arcMap.get("start");
				double width = (Double) arcMap.get("width");
				int circle = (Integer) arcMap.get("circle");
				Arc arc = new Arc(circle, width, start, end);
				cList.add(arc);
				arcs.add(arc);
			}
		}
	}

	/**
	 * crossover meta parameters.
	 * @param d2
	 * @return
	 */
	public Drawing crossoverMeta(Drawing d2) {
		Drawing d = new Drawing(rand.nextInt());
		for(String key : this.probabilities.keySet()){
			if(rand.nextBoolean())
				d.probabilities.put(key, this.getProb(key));
			else
				d.probabilities.put(key, d2.getProb(key));
		}
		return d;
	}

	public void mutateMeta() {
		Set<String> keys = new HashSet<String>(this.probabilities.keySet());
		for(String key : keys){
			if(rand.nextDouble() < this.getProb("mutationProb"))
				this.putProb(key, rand.nextDouble());
		}
	}

	public void putProb(String key, double value) {
		this.probabilities.put(key, value);
	}

	public Set<String> getAllProbabilities() {
		return this.probabilities.keySet();
	}

	public void mutate(CircleFractal fractal) {
		this.mutateMeta();
		if(rand.nextDouble() < this.getProb("removeArchH"))
			if(arcsHierarchical.size() >= 1)
				this.arcsHierarchical.remove(rand.nextInt(arcsHierarchical.size()));
		if(rand.nextDouble() < this.getProb("removeArch")){
			List<Arc> l = this.arcsHierarchical.get(rand.nextInt(arcsHierarchical.size()));
			l.remove(rand.nextInt(l.size()));
		}
		
		
		if(rand.nextDouble() < this.getProb("addArchH")){
			Drawing d = fractal.createRandomDrawingWithIntersections(this, this.arcs.size() * 2);
			this.arcsHierarchical.add(d.arcsHierarchical.get(rand.nextInt(arcsHierarchical.size())));
		}
		
		if(rand.nextDouble() < this.getProb("cutLine")){
			List<Arc> line = this.arcsHierarchical.get(rand.nextInt(arcsHierarchical.size()));
			int cut = rand.nextInt(line.size());
			List<Arc> line1 = line.subList(0, Math.max(0, cut - 1));
			List<Arc> line2 = line.subList(Math.min(line.size(), cut + 1), line.size());
			arcsHierarchical.remove(line);
			arcsHierarchical.add(line1);
			arcsHierarchical.add(line2);
		}
		
		this.recalculateArcs();
	}

	private void recalculateArcs() {
		this.arcs = new ArrayList<Arc>();
		for(List<Arc> l : arcsHierarchical)
			for(Arc e : l)
				arcs.add(e);
	}

	public Drawing crossover(Drawing d2) {
		Drawing newD = this.crossoverMeta(d2);
		List<List<Arc>> arcsLists = this.arcsHierarchical;
		arcsLists.addAll(d2.arcsHierarchical);
		newD.arcsHierarchical = new ArrayList<List<Arc>>();
		newD.arcs = new ArrayList<Arc>();
		int i = 0;
		for (List<Arc> each : arcsLists) {
			if (rand.nextBoolean() && !each.isEmpty()) {
				for (Arc arc : each)
					newD.addArc(arc, i);
				i++;
			}
		}

		return newD;
	}

}
