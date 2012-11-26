package ch.eonum.artmachine.test;

import static org.junit.Assert.*;

import org.junit.Test;

import ch.eonum.artmachine.Arc;
import ch.eonum.artmachine.Drawing;

public class DrawingTest {
	@Test
	public void testJSONOutput(){
		Drawing d = new Drawing();
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
}
