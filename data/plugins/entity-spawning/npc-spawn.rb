require 'java'

java_import 'org.apollo.game.model.Animation'
java_import 'org.apollo.game.model.Graphic'
java_import 'org.apollo.game.model.Npc'
java_import 'org.apollo.game.model.Position'
java_import 'org.apollo.game.model.World'
java_import 'org.apollo.game.model.def.NpcDefinition'

# Spawns an npc with the properties specified in the hash.
def spawn_npc(hash)
  raise 'A name or id must be specified to spawn an npc.' if !(hash.has_key?(:name) || hash.has_key?(:id))
  id = hash.delete(:id)

  if id == nil
  	name = hash.delete(:name).to_s.gsub('_', ' ')
    if name.include?(' ')
      id = name[name.rindex(' ') + 1, name.length - 1].to_i
    end
    id = locate_entity('npc', name, 1).first if id == nil || id == 0
  end

  raise "The npc called #{name} could not be identified." if id == nil

  z = hash.delete(:z)
  position = Position.new(hash.delete(:x), hash.delete(:y), z == nil ? 0 : z)
  npc = Npc.new(id, position)

  World.world.register(npc)
  parse_hash(npc, hash) unless hash.empty?
end

# Parses the remaining key-value pairs in the hash.
def parse_hash(npc, hash)
  hash.each do |key, value| 
    if key == :face
      facing_position = direction_to_position(value, npc.position)
      npc.turn_to(facing_position)
    elsif key == :bounds
      # TODO
    elsif key == :delta_bounds
      # TODO
    elsif key == :spawn_animation
      npc.play_animation(Animation.new(value))
    elsif key == :spawn_graphic
      npc.play_graphic(Graphic.new(value))
    else
      raise "Unrecognised key #{key} - value #{value}."
    end
  end
end


# Returns a position that an entity at the specified position should be facing towards if they are looking in the specified direction.
def direction_to_position(direction, position)
  x = position.x
  y = position.y
  z = position.height

  case direction
    when :north      then return Position.new(x,     y + 1, z)
    when :north_east then return Position.new(x + 1, y + 1, z)
    when :east       then return Position.new(x + 1, y,     z)
    when :south_east then return Position.new(x + 1, y - 1, z)
    when :south      then return Position.new(x,     y - 1, z)
    when :south_west then return Position.new(x - 1, y - 1, z)
    when :west       then return Position.new(x - 1, y,     z)
    when :north_west then return Position.new(x - 1, y + 1, z)
    else return position
  end
end

# Locates an entity with the specified type (e.g. npc) and name, returning possible ids as an array.
def locate_entity(type, name, limit=5)
  ids = []
  name.downcase!
  Kernel.const_get("#{type.capitalize}Definition").definitions.each do |definition|
    break if ids.length == limit
    ids << definition.id.to_i if definition.name.to_s.downcase == name
  end
  return ids
end