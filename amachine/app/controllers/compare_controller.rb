include Java
$CLASSPATH << "../bin";
java_import Java::Ch.eonum.artmachine.CircleFractal
java_import Java::Ch.eonum.artmachine.Drawing

class CompareController < ApplicationController
  @@cf = CircleFractal.new(400, 5, 15, 450, 450);
  
  def compare
		@drawing1 = @@cf.createRandomDrawingWithIntersections(300);
    @drawing2 = @@cf.createRandomDrawingWithIntersections(300);
  end
end
