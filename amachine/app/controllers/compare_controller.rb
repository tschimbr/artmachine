include Java
$CLASSPATH << "../bin";
Dir["../lib/\*.jar"].each { |jar| require jar }
java_import Java::Ch.eonum.artmachine.CircleFractal
java_import Java::Ch.eonum.artmachine.Drawing

class CompareController < ApplicationController
  @@cf = CircleFractal.new(400, 5, 15, 450, 450);
  
  def compare
    if(params[:id] != nil)
      Rdrawing.where(:_id => params[:id]).delete
    end

    @@cf.makeDrawings(100, 300,
			"am_development", "localhost", "rdrawings") if Rdrawing.count < 90
		@drawing1 = Rdrawing.skip(rand * Rdrawing.count-1).first
    @drawing2 = Rdrawing.skip(rand * Rdrawing.count-1).first
  end
end
