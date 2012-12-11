package ch.eonum.artmachine.test;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Set;

import org.junit.Test;

import ch.eonum.artmachine.Circle;
import ch.eonum.artmachine.CircleFractal;

public class CircleFractalTest {
	
	@Test
	public void createFractal(){
		CircleFractal cf = new CircleFractal(400, 5, 15, 450, 450, 0);
		List<Circle> circles = cf.getCircles();
		assertEquals(1529, circles.size());
		assertEquals(7, cf.getCirclesByDepth(0).size());
		assertEquals(37, cf.getCirclesByDepth(1).size());
		assertEquals(91, cf.getCirclesByDepth(2).size());
		assertEquals(325, cf.getCirclesByDepth(3).size());
		assertEquals(1069, cf.getCirclesByDepth(4).size());
		
		Circle c145 = circles.get(145);
		assertEquals(558.253175, c145.x, 0.0001);
		assertEquals(562.5, c145.y, 0.0001);
		assertEquals(25, c145.radius, 0.0001);
		assertEquals(1, c145.depth);
		
		Set<Integer> possibleWidths = cf.getPossibleWidths();
		assertEquals(5, possibleWidths.size());

	}

}
