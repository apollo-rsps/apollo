require 'java'

# Looks up the id of the npc with the specified name.
def lookup_npc(name)
  lookup_entity(:npc, name)
end

# Looks up the id of the item with the specified name.
def lookup_item(name)
  lookup_entity(:item, name)
end

# Looks up the id of the object with the specified name.
def lookup_object(name)
  lookup_entity(:object, name)
end

# Looks up the id of an entity of the specified type (either :npc, :item, or :object)
def lookup_entity(type, name)
  type = type.to_s
  name = name.to_s.gsub('_', ' ')

  cached = NAME_CACHE[type + name]
  return cached unless cached.nil?

  id = name[name.rindex(' ') + 1, name.length - 1].to_i if name.include?(' ')
  id = find_entities(type, name, 1).first if id.nil? || id.zero?

  fail "The #{type} called #{name} could not be identified." if id.nil?

  NAME_CACHE[type + name] = id
  id
end

# Finds entities with the specified type (e.g. npc) and name, returning possible ids as an array.
def find_entities(type, name, limit = 5)
  ids = []
  name.downcase!

  Kernel.const_get("#{type.capitalize}Definition").definitions.each do |definition|
    break if (ids.length == limit)
    ids << definition.id.to_i if definition.name.to_s.downcase == name
  end

  ids
end

private

NAME_CACHE = {} # Primitive, caching all may not be desirable.
