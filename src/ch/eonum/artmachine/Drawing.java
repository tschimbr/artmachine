package ch.eonum.artmachine;

import java.util.ArrayList;
import java.util.List;

/**
 * Set of arcs and areas describing a drawing on a fractal.
 * @author tim
 *
 */
public class Drawing {
	private List<Arc> arcs;

	public Drawing(){
		this.setArcs(new ArrayList<Arc>());
	}
	
	public boolean addArc(Arc arc){
		return this.arcs.add(arc);
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
