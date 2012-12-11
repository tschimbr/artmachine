package ch.eonum.artmachine.main;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import ch.eonum.artmachine.CircleFractal;
import ch.eonum.artmachine.Drawing;

public class CreateJSON {

	public static void main(String[] args) throws IOException {
		CircleFractal cf = new CircleFractal(400, 5, 15, 450, 450, 0);
		Drawing rd = cf.createRandomDrawingWithIntersections(300);
		
		PrintStream ps = new PrintStream(new File("arcs.js"));
		ps.print("var arcs = " + rd.arcsToJSON());
		ps.close();
	}

}
