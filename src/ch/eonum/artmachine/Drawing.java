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
	 * add an arc. numSequence is the number of the sequence of connected arcs.
	 * if it does not exist it is created.
	 * 
	 * @param arc
	 * @param numSequence
	 * @return
	 */
	public boolean addArc(Arc arc, int numSequence){
		if(this.arcsHierarchical.size() < numSequence + 1)
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

	public String toJSON() {
		String json ="[";
		for(int i = 0; i < arcs.size() - 1; i++)
			json += arcs.get(i).toJSON() + ",\n";
		json += arcs.get(arcs.size() - 1).toJSON();
		return json + "]";
	}

}
