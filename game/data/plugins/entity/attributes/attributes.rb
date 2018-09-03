require 'java'

java_import 'org.apollo.game.model.entity.Mob'
java_import 'org.apollo.game.model.entity.Npc'
java_import 'org.apollo.game.model.entity.Player'
java_import 'org.apollo.game.model.entity.attr.Attribute'
java_import 'org.apollo.game.model.entity.attr.AttributeDefinition'
java_import 'org.apollo.game.model.entity.attr.AttributeMap'
java_import 'org.apollo.game.model.entity.attr.AttributePersistence'
java_import 'org.apollo.game.model.entity.attr.AttributeType'
java_import 'org.apollo.game.model.entity.attr.BooleanAttribute'
java_import 'org.apollo.game.model.entity.attr.NumericalAttribute'
java_import 'org.apollo.game.model.entity.attr.StringAttribute'

# Declares an attribute and adds its definition.
def declare_attribute(name, default, persistence = :transient)
  if Player.method_defined?(name) || Mob.method_defined?(name) || Npc.method_defined?(name)
    fail "Attribute #{name} clashes with an existing variable."
  end

  definition = AttributeDefinition.new(default, get_persistence(persistence), get_type(default))
  AttributeMap::define(name.to_s, definition)
end

private

# The existing Mob class.
class Mob

  # Overrides method_missing to implement the functionality.
  def method_missing(symbol, *args)
    name = symbol.to_s.strip

    if name[-1] == '='
      fail "Expected argument count of 1, received #{args.length}" unless args.length == 1

      name = name[0...-1].strip # Drop the equals and trim whitespace.
      set_attribute(name, to_attribute(args[0]))
    elsif AttributeMap::get_definition(name).nil?
      super(symbol, *args)
    else
      attribute = get_attribute(name)
      value = attribute.value
      (attribute.type == AttributeType::SYMBOL) ? value.to_sym : value
    end
  end

end

# Gets the appropriate attribute for the specified value.
def to_attribute(value)
  case value
    when String, Symbol then return StringAttribute.new(value.to_s, value.is_a?(Symbol))
    when Integer, Float then return NumericalAttribute.new(value)
    when TrueClass, FalseClass then return BooleanAttribute.new(value)
    else fail "Undefined attribute type #{value.class}."
  end
end

# Gets the attribute type of the specified value.
def get_type(value)
  case value
    when String  then return AttributeType::STRING
    when Symbol  then return AttributeType::SYMBOL
    when Integer then return AttributeType::LONG
    when Float   then return AttributeType::DOUBLE
    when TrueClass, FalseClass then return AttributeType::BOOLEAN
    else fail "Undefined attribute type #{value.class}."
  end
end

# Gets the Persistence type of the specified value.
def get_persistence(persistence)
  unless [:persistent, :transient].include?(persistence)
    fail "Undefined persistence type #{persistence}."
  end

  (persistence == :persistent) ? AttributePersistence::PERSISTENT : AttributePersistence::TRANSIENT
end
