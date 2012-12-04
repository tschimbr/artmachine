include Java
$CLASSPATH << "../bin";
Dir["../lib/\*.jar"].each { |jar| require jar }
java_import Java::Ch.eonum.artmachine.CircleFractal
java_import Java::Ch.eonum.artmachine.Drawing

class CompareController < ApplicationController
  @@cf = CircleFractal.new(400, 5, 15, 450, 450);
  
  def compare
    
    @@cf.createInitialRandomCases(100, 400,
			"am_development", "localhost", "drawings");
		@drawing1 = @@cf.createRandomDrawingWithIntersections(300);
    @drawing2 = @@cf.createRandomDrawingWithIntersections(300);
  end
end
