package ch.eonum.artmachine.test;

import static org.junit.Assert.*;

import org.junit.Test;

import ch.eonum.artmachine.Arc;
import ch.eonum.artmachine.CircleFractal;
import ch.eonum.artmachine.Drawing;

public class DrawingTest {
	@Test
	public void testJSONOutput(){
		Drawing d = new Drawing(25);
		d.addArc(new Arc(12, 3.2, 2.1, 2.2));
		d.addArc(new Arc(13, 3.3, 2.3, 2.2));
		
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
				"}]", d.toJSON());
	}
	
	@Test
	public void testRandomCreation(){
		CircleFractal cf = new CircleFractal(400, 5, 15, 450, 450);
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
