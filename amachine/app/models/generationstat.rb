class Generationstat
  include Mongoid::Document

  @abs

  def header
    headers = "["
    @abs = []
    attributes.each do |attr_name, value|
      if(attr_name != '_id')
        @abs << attr_name
      end
    end
    @abs.each do |attr_name|
      headers = headers + "'" + attr_name + "', "
    end
    headers = headers[0..headers.length - 3]

    return headers + "]"
  end

  def statvalues abs
    values = "["
    abs.each do |attr_name|
      values = values + self[attr_name].to_s + ", "
    end
    values = values[0..values.length - 3]

    return values + "]"
  end

  def abs
    if(@abs == nil)
      header
    end
    @abs
  end
end
