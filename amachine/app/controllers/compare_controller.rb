include Java
$CLASSPATH << "../bin";
Dir["../lib/\*.jar"].each { |jar| require jar }
java_import Java::Ch.eonum.artmachine.CircleFractal
java_import Java::Ch.eonum.artmachine.Drawing

class CompareController < ApplicationController
  @@cf = CircleFractal.new(400, 5, 15, 450, 450);
  
  def compare
    unless(params[:id] == nil)
      ids = params[:id].split("|")
      ids.each do |id|
         Rdrawing.where(:_id => id).delete 
      end
    end

    @@cf.makeDrawings(100, 300,
			"am_development", "localhost", "rdrawings") if Rdrawing.count < 90
    
    first = rand * Rdrawing.count-1
    second = rand * Rdrawing.count-1
    while(second == first) # avoid duplicates
      second = rand * Rdrawing.count-1
    end
		@drawing1 = Rdrawing.skip(first).first
    @drawing2 = Rdrawing.skip(second).first
  end

  def stats
    @stats = Generationstat.all.order_by([[ :generation, :asc ]])
    @last = @stats.last
  end
end
