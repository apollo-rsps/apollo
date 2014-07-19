require 'java'

java_import 'org.apollo.game.model.Position'
java_import 'org.apollo.game.model.World'
java_import 'org.apollo.game.model.area.Sector'
java_import 'org.apollo.game.model.area.SectorCoordinates'
java_import 'org.apollo.game.model.area.SectorRepository'
java_import 'org.apollo.game.model.def.ItemDefinition'

# :transient :recurrent

def spawn_npc(hash)
  raise 'A name (or id), x coordinate, and y coordinate must be specified to spawn an item' unless (hash.has_key?(:name) || hash.has_key?(:id)) && hash.has_key?(:x) && hash.has_key?(:y)
  
  z = hash.delete(:z)
  position = Position.new(hash.delete(:x), hash.delete(:y), z == nil ? 0 : z)

end

def get_item(hash)
  id = hash.delete(:id)

  if id == nil
    name = hash.delete(:name).to_s.gsub('_', ' ')
    if name.include?(' ')
      id = name[name.rindex(' ') + 1, name.length - 1].to_i
    end
    id = locate_entity('item', name, 1).first if id == nil || id == 0
  end

  raise "The item called #{name} could not be identified." if id == nil

  amount = hash.delete(:amount)
  return Item.new(id, amount)
end

class GroundItem
  
  def initialise(id, amount, x, y, z)
    @item = Item.new(id, amount)
    @position = Position.new(x, y, z)
  end

end