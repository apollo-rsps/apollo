require 'java'

java_import 'org.apollo.game.model.entity.Entity'

# Maps attribute names (i.e. symbols) to attribute definitions.
ATTRIBUTE_DEFINITIONS = {}

class Entity
  
  # Overridies method_missing
  def method_missing(symbol, *args)
    name = symbol.to_s.strip

    if name[-1] == "="
      raise "Error - expected argument count of 1, received #{args.length}" unless args.length == 1
      
      name = name[0...-1].strip # Drop the equals and preceeding whitespace
      attributes[name] = args[0].is_a?(Symbol) ? args[0].to_s : args[0]
    elsif ATTRIBUTE_DEFINITIONS[name] == nil
      super(symbol, *args)
    else
      if attributes[name] == nil then return ATTRIBUTE_DEFINITIONS[name].default end

      return ATTRIBUTE_DEFINITIONS[name].type == :symbol ? attributes[name].to_sym : attributes[name]
    end
  end

  def to_s
    return value.to_s
  end

end

# An attribute belonging to an entity.
class AttributeDefinition
  attr_reader :default, :type, :persistence

  def initialize(default, persistence=:transient)
    @default = default
    @type = get_type
    @persistence = persistence
  end

  def to_s
    return "[AttributeDefinition - default=#{default}, type=#{type}, persistence=#{persistence}]."
  end

  private # Gets the type of an attribute from its default value.
  def get_type
    case @default
      when Fixnum, Integer then type = :number
      when String then type = :string
      when Symbol then type = :symbol
      when TrueClass, FalseClass then type = :boolean
      else raise "Error - #{value} has an unrecognised attribute type of #{value.class}."
    end
  end

end

# Declares an attribute which can then be assigned.
def declare_attribute(name, default, persistence=:transient)
  ATTRIBUTE_DEFINITIONS[name.to_s] = AttributeDefinition.new(default, persistence)
end