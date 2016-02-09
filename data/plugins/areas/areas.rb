require 'java'

java_import 'org.apollo.game.model.Position'
java_import 'org.apollo.game.model.entity.EntityType'
java_import 'org.apollo.game.model.entity.Player'

# Creates a new area and registers it with the supplied coordinates.
def area(hash)
  failure_message = 'Hash must contain a name, coordinates, and actions pair.'
  fail failure_message unless hash.has_keys?(:name, :coordinates, :actions)

  name, coordinates, actions = hash[:name], hash[:coordinates], hash[:actions]

  actions = [actions] if actions.is_a?(Symbol)
  actions.map! { |action| AREA_ACTIONS[action] }
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

  def min_x # TODO: better data structure and methods than this
    @coordinates[0]
  end

  def min_y
    @coordinates[1]
  end

  def max_x
    @coordinates[2]
  end

  def max_y
    @coordinates[3]
  end

  def height
    @coordinates[4]
  end

  # Called when the player has entered the area.
  def entered(player, position)
    @actions.each { |action| action.entered(player, position) }
  end

  # Called when the player has moved, but is still inside the area (and was in the area before).
  def inside(player, position)
    @actions.each { |action| action.inside(player, position) }
  end

  # Called when the player has exited the area.
  def exited(player, position)
    @actions.each { |action| action.exited(player, position) }
  end

end

on :login do |event|
  player = event.player

  @areas.each do |area|
    area.entered(player) if player.position.inside(area)
  end
end

# Listen for the MobPositionUpdateEvent and update the area listeners if appropriate.
on :mob_position_update do |event|
  mob = event.mob
  next unless mob.entity_type == EntityType::PLAYER

  old = mob.position
  updated = event.next
  @areas.each do |area|
    was_inside = old.inside(area)
    next_inside = updated.inside(area)

    if was_inside
      next_inside ? area.inside(mob, updated) : area.exited(mob, updated)
    else
      area.entered(mob, updated) if next_inside
    end
  end
end

# The existing Position class.
class Position

  # Returns whether or not this Position is inside the specified Area.
  def inside(area)
    return false if x < area.min_x || x > area.max_x || y < area.min_y || y > area.max_y
    z = area.height

    z.nil? || z == height
  end

end
