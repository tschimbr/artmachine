package ch.eonum.artmachine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Set of arcs and areas describing a drawing on a fractal.
 * @author tim
 *
 */
public class Drawing {
	private static final String[] PROBABILITIES = { "numArcs",
			"probNewFragment", "probArcSize", "circleSizeRatio" };
	private List<Arc> arcs;
	private Map<String, Double> probabilities;
	private Random rand;

	public Drawing(int seed){
		this.setArcs(new ArrayList<Arc>());
		this.rand = new Random(seed);
		this.probabilities = new HashMap<String, Double>();
		for(String each : Drawing.PROBABILITIES)
			this.probabilities.put(each, rand.nextDouble());
	}
	
	public boolean addArc(Arc arc){
		return this.arcs.add(arc);
	}
	
	public double getProb(String prob){
		return this.probabilities.get(prob);
	}

	public List<Arc> getArcs() {
		return arcs;
	}

	public void setArcs(List<Arc> arcs) {
		this.arcs = arcs;
	}

	public String toJSON() {
		String json ="[";
		for(int i = 0; i < arcs.size() - 1; i++)
			json += arcs.get(i).toJSON() + ",\n";
		json += arcs.get(arcs.size() - 1).toJSON();
		return json + "]";
	}

}
