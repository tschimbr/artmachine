package ch.eonum.artmachine.test;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import ch.eonum.artmachine.Arc;
import ch.eonum.artmachine.CircleFractal;
import ch.eonum.artmachine.Drawing;

public class DrawingTest {
	@Test
	public void testJSONOutput(){
		Drawing d = new Drawing(25);
		d.addArc(new Arc(12, 3.2, 2.1, 2.2), 0);
		d.addArc(new Arc(13, 3.3, 2.3, 2.2), 0);
		
		assertEquals("[{\n" + 
				"  \"circle\": 12,\n" + 
				"  \"width\": 3.2,\n" + 
				"  \"start\": 2.1,\n" + 
				"  \"end\": 2.2\n" + 
				"},\n" +
				"{\n" + 
				"  \"circle\": 13,\n" + 
				"  \"width\": 3.3,\n" + 
				"  \"start\": 2.3,\n" + 
				"  \"end\": 2.2\n" + 
				"}]", d.arcsToJSON());
		
		assertEquals("{\n" + 
				"  \"numArcs\": 0.7315948597820918,\n" + 
				"  \"circleSizeRatio\": 0.07447673863145976,\n" + 
				"  \"probArcSize\": 0.6453354838305244,\n" + 
				"  \"probNewFragment\": 0.053725659827636885\n" + 
				"}", d.metaToJSON());
	}
	
	@Test
	public void testJSONOutputHierarchical(){
		Drawing d = new Drawing(25);
		d.addArc(new Arc(12, 3.2, 2.1, 2.2), 0);
		d.addArc(new Arc(13, 3.3, 2.3, 2.2), 0);
		d.addArc(new Arc(13, 3.3, 2.3, 2.2), 1);
		
		assertEquals("[[{\n" + 
				"  \"circle\": 12,\n" + 
				"  \"width\": 3.2,\n" + 
				"  \"start\": 2.1,\n" + 
				"  \"end\": 2.2\n" + 
				"},\n" +
				"{\n" + 
				"  \"circle\": 13,\n" + 
				"  \"width\": 3.3,\n" + 
				"  \"start\": 2.3,\n" + 
				"  \"end\": 2.2\n" + 
				"}], [{\n" + 
				"  \"circle\": 13,\n" + 
				"  \"width\": 3.3,\n" + 
				"  \"start\": 2.3,\n" + 
				"  \"end\": 2.2\n" + 
				"}]]", d.arcsToJSONHierarchical());
		
	}
	
	@Test
	public void readJSON() throws IOException{
		Drawing d = new Drawing(25);
		d.addArc(new Arc(12, 3.2, 2.1, 2.2), 0);
		d.addArc(new Arc(13, 3.3, 2.3, 2.2), 0);
		d.addArc(new Arc(13, 3.3, 2.3, 2.2), 1);
		
		assertEquals(
				d.arcsToJSONHierarchical(),
				Drawing.createFromJSON(d.arcsToJSONHierarchical(),
						d.metaToJSON(), 23).arcsToJSONHierarchical());

		assertEquals(
				d.metaToJSON(),
				Drawing.createFromJSON(d.arcsToJSONHierarchical(),
						d.metaToJSON(), 23).metaToJSON());

	}
	
	@Test
	public void testRandomCreation(){
		CircleFractal cf = new CircleFractal(400, 5, 15, 450, 450, 0);
		for(int i = 0; i < 100; i++){
			Drawing rd = cf.createRandomDrawingWithIntersections(150);
			assertTrue(rd.getArcs().size() > 0);
			assertTrue(rd.getArcs().size() <= 150);
			for(Arc each : rd.getArcs()){
				assertTrue(each.circle >= 0);
				assertTrue(each.circle < 1529);
				assertFalse(Double.isNaN(each.end));
				assertFalse(Double.isNaN(each.start));
				assertTrue(each.start >= 0);
				assertTrue(each.start < 2*Math.PI);
				assertTrue(each.end >= 0);
				assertTrue(each.end < 2*Math.PI);
				
			}
		}
	}
}
