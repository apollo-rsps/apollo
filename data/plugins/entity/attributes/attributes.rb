require 'java'

java_import 'org.apollo.game.model.entity.Entity'
java_import 'org.apollo.game.model.entity.attr.Attribute'
java_import 'org.apollo.game.model.entity.attr.AttributeDefinition'
java_import 'org.apollo.game.model.entity.attr.AttributeMap'
java_import 'org.apollo.game.model.entity.attr.AttributePersistence'
java_import 'org.apollo.game.model.entity.attr.AttributeType'
java_import 'org.apollo.game.model.entity.attr.BooleanAttribute'
java_import 'org.apollo.game.model.entity.attr.NumericalAttribute'
java_import 'org.apollo.game.model.entity.attr.StringAttribute'

class Entity
  
  # Overrides method_missing
  def method_missing(symbol, *args)
    name = symbol.to_s.strip

    if name[-1] == "="
      raise "Expected argument count of 1, received #{args.length}" unless args.length == 1
      
      puts name
      name = name[0...-1].strip # Drop the equals
      set_attribute(name, to_attribute(args[0]))
    elsif AttributeMap::get_definition(name) == nil
      super(symbol, *args)
    else
      attribute = get_attribute(name); definition = AttributeMap::get_definition(name)
      value = attribute == nil ? definition.default : attribute.value
      
      return definition.type == AttributeType::SYMBOL ? value.to_sym : value
    end
  end

  # Gets the appropriate attribute for the specified value.
  private
  def to_attribute(value)
    case value
      when String, Symbol then return StringAttribute.new(value.to_s, value.is_a?(Symbol))
      when Integer, Float then return NumericalAttribute.new(value)
      when TrueClass, FalseClass then return BooleanAttribute.new(value)
      else raise "Undefined attribute type #{value.class}."
    end
  end

end

# Declares an attribute and adds its definition.
def declare_attribute(name, default, persistence=:transient)
  AttributeMap::add_definition(name.to_s, AttributeDefinition.new(default, get_persistence(persistence), get_type(default)))
end

# Gets the attribute type of the specified value.
private
def get_type(value)
  case value
    when String  then return AttributeType::STRING
    when Symbol  then return AttributeType::SYMBOL
    when Integer then return AttributeType::LONG
    when Float   then return AttributeType::DOUBLE
    when TrueClass, FalseClass then return AttributeType::BOOLEAN
    else raise "Undefined attribute type #{value.class}."
  end
end

# Gets the Persistence type of the specified value.
def get_persistence(persistence)
  raise "Undefined persistence type #{persistence}." unless persistence == :serialized || persistence == :transient

  return persistence == :serialized ? AttributePersistence::SERIALIZED : AttributePersistence::TRANSIENT
end