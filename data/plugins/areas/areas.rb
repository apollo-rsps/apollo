require 'java'

java_import 'org.apollo.game.model.entity.Player'

# Todo make 0 the default height

# A map of coordinates (as an array) to areas.
AREAS = []

# An area of the game world.
class Area

  def initialize(name, coordinates, actions)
    @name = name
    @coordinates = coordinates
    @actions = actions
  end

  # Called when the player has entered the area.
  def entered(player)
    actions.each { |action| action.entered(player) }
  end

  # Called whilst the player is inside the area.
  def inside(player)
    acttions.each { |action| action.inside(player) }
  end

  # Called when the player has exited the area.
  def exited(player)
    actions.each { |action| action.exited(player) }
  end

end

# Creates a new area and registers it with the supplied coordinates.
def area(hash)
  raise 'Hash must contain a name, coordinates, and actions pair.' unless hash.has_keys?(:name, :coordinates, :actions)
  name = hash[:name]; coordinates = hash[:coordinates]; actions = hash[:actions]

  AREAS << Area.new(name, coordinates, actions.is_a?(Symbol) ? [actions] : actions)
end

# Coordinates refer to the bottom-left position (min_x, min_y) and the top-right position (max_x, max_y), followed by the height (optional).
area :name => :wilderness, :coordinates => [ 2944, 3520, 3391, 3967, 0 ], :actions => [ :pvp, :multicombat, :wilderness ]
area :name => :duel_arena, :coordinates => [ 3327, 3200, 3392, 3286    ], :actions => :pvp