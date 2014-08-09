require 'java'

# Looks up the id of the npc with the specified name.
def lookup_npc(name)
  return lookup_entity('npc', name)
end

# Looks up the id of the item with the specified name.
def lookup_item(name)
  return lookup_entity('item', name)
end

# Looks up the id of the object with the specified name.
def lookup_object(name)
  return lookup_entity('object', name)
end

# Looks up the id of an entity of the specified type (either 'npc', 'item', or 'object')
def lookup_entity(type, symbol)
  symbol = symbol.to_s
  cached = NAME_CACHE[type + symbol]
  return cached unless cached == nil

  name = symbol.to_s.gsub('_', ' ')
  if name.include?(' ')
    id = name[name.rindex(' ') + 1, name.length - 1].to_i
  end
  id = find_entities(type, name, 1).first if id == nil || id == 0
  
  if id == nil
    raise "The #{type} called #{name} could not be identified."
  end

  NAME_CACHE[type + symbol] = id
  return id
end

# Finds entities with the specified type (e.g. npc) and name, returning possible ids as an array.
def find_entities(type, name, limit=5)
  ids = []; name.downcase!

  Kernel.const_get("#{type.capitalize}Definition").definitions.each do |definition|
    break if ids.length == limit
    ids << definition.id.to_i if definition.name.to_s.downcase == name
  end

  return ids
end

private
NAME_CACHE = {}