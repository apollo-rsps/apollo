require 'java'

java_import 'org.apollo.game.model.Position'
java_import 'org.apollo.game.model.entity.EntityType'
java_import 'org.apollo.game.model.entity.Player'



# Creates a new area and registers it with the supplied coordinates.
def area(hash)
  raise 'Hash must contain a name, coordinates, and actions pair.' unless hash.has_keys?(:name, :coordinates, :actions)
  name = hash[:name]; coordinates = hash[:coordinates]; actions = hash[:actions]

  actions = [ actions ] if actions.is_a?(Symbol)
  actions.map! { |action| AREA_ACTIONS[action]}
  @areas << Area.new(name, coordinates, actions)
end


private

# A map of coordinates (as an array) to areas.
@areas = []

# An area of the game world.
class Area

  def initialize(name, coordinates, actions)
    @name = name
    @coordinates = coordinates
    @actions = actions
  end

  def min_x() # TODO better data structure and methods than this
    @coordinates[0]
  end

  def min_y()
    @coordinates[1]
  end

  def max_x()
    @coordinates[2]
  end

  def max_y()
    @coordinates[3]
  end

  def height()
    @coordinates[4]
  end

  # Called when the player has entered the area.
  def entered(player)
    @actions.each { |action| action.entered(player) }
  end

  # Called when the player has moved, but is still inside the area (and was in the area before).
  def inside(player)
    @actions.each { |action| action.inside(player) }
  end

  # Called when the player has exited the area.
  def exited(player)
    @actions.each { |action| action.exited(player) }
  end

end

# Listen for the MobPositionUpdateEvent and update the area listeners if appropriate.
on :mob_position_update do |event|
  mob = event.mob
  next unless mob.entity_type == EntityType::PLAYER

  old = mob.position
  @areas.each do |area|
    was_inside = old.inside(area)
    next_inside = event.next.inside(area)

    if was_inside
      if next_inside then area.inside(mob) else area.exited(mob) end
    else
      area.entered(mob) if next_inside
    end
  end
end

# The existing Position class.
class Position

  # Returns whether or not this Position is inside the specified Area.
  def inside(area)
    return false if (x < area.min_x() || x > area.max_x() || y < area.min_y() || y > area.max_y())
    z = area.height()

    return true if (z.nil? || z == height)
  end

end