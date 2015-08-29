require 'java'

java_import 'org.apollo.cache.def.NpcDefinition'
java_import 'org.apollo.game.action.Action'
java_import 'org.apollo.game.model.Animation'
java_import 'org.apollo.game.model.Graphic'
java_import 'org.apollo.game.model.Position'
java_import 'org.apollo.game.model.World'
java_import 'org.apollo.game.model.entity.Npc'

# Information about npc spawning
#
# Npcs are passed to spawn npc as a hash. Every key and every non-integer value must be a Symbol.
# Every hash must implement the following:
#   :name - the name of the npc. If this npc shares its name with another, append the specific id
#           after the name (e.g. :woman_4)
#   :x - the x coordinate where the npc will spawn.
#   :y - the y coordinate where the npc will spawn.
# Optional arguments are as follows:
#   :face - the direction the npc should face when it spawns. Supported options are :north,
#           :north_east, :east, :south_east, :south, :south_west, :west, and :north_west
#   :bounds - the rectangular bound that the npc can wander about in. Order is
#             [bottom-left x-coordinate, bottom-left y-coordinate, top-right x-coordinate,
#             top-right y-coordinate]
#   :delta_bounds - the rectangular bound that the npc can wander about in, as a difference from
#                   the spawn point. Order is [x-delta, y-delta]. Should not be used with :bounds.
#   :spawn_animation - the animation that will be played when the npc spawns.
#   :spawn_graphic - the graphic that will be played when the npc spawns.

# Spawns an npc with the properties specified in the hash.
def spawn_npc(hash)
  unless (hash.key?(:name) || hash.key?(:id)) && hash.has_keys?(:x, :y)
    fail 'A name (or id), x coordinate, and y coordinate must be specified to spawn an npc.'
  end

  npc = get_npc(hash)
  spawn(npc, hash)
  npc
end

# Spawns the specified npc and applies the properties in the hash.
def spawn(npc, hash)
  unless hash.empty?
    hash = decode_hash(npc.position, hash)
    apply_decoded_hash(npc, hash)
  end

  $world.register(npc)
end

# Returns an npc with the id and position specified by the hash.
def get_npc(hash)
  id = lookup_npc(hash.delete(:name))

  z = hash.delete(:z)
  position = Position.new(hash.delete(:x), hash.delete(:y), z.nil? ? 0 : z)
  Npc.new($world, id, position)
end

# Applies a decoded hash (one aquired using parse_hash) to the specified npc.
def apply_decoded_hash(npc, hash)
  hash.each do |key, value|
    case key
      when :face            then npc.turn_to(value)
      when :boundary        then npc.boundaries = value
      when :spawn_animation then npc.play_animation(Animation.new(value))
      when :spawn_graphic   then npc.play_graphic(Graphic.new(value))
      else fail "Unrecognised key #{key} - value #{value}."
    end
  end
end

# Parses the remaining key-value pairs in the hash.
def decode_hash(position, hash)
  decoded = {}

  hash.each do |key, value|
    case key
      when :face
        decoded[:face] = direction_to_position(value, position)
      when :delta_bounds
        fail ':delta_bounds must have two values.' unless value.length == 2
        dx, dy, x, y, z = value[0], value[1], position.x, position.y, position.height
        fail 'Delta values cannot be less than 0.' if dx < 0 || dy < 0

        decoded[:boundary] = [Position.new(x - dx, y - dy, z), Position.new(x + dx, y + dy, z)]
      when :bounds
        fail ':bounds must have four values.' unless value.length == 4
        min_x, min_y, max_x, max_y = value[0], value[1], value[2], value[3]

        decoded[:boundary] = [Position.new(min_x, min_y), Position.new(max_x, max_y)]
      when :spawn_animation then decoded[:spawn_animation] = Animation.new(value)
      when :spawn_graphic then decoded[:spawn_graphic] = Graphic.new(value)
      else fail "Unrecognised key #{key} - value #{value}."
    end
  end

  decoded
end

# Returns a position that an entity at the specified position should be facing towards if they are
# looking in the specified direction.
def direction_to_position(direction, position)
  x, y, z = position.x, position.y, position.height

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

# An action that spawns an npc temporarily, before executing an action.
class TemporaryNpcAction < Action
  attr_reader :executions, :combative

  def initialize(delay, immediate, hash)
    super(delay, immediate, get_npc(hash))

    @executions = 0
    @hash = hash
  end

  def execute
    if @executions == 0
      spawn(mob, @hash)
      execute_spawn_action
    else
      execute_action
    end

    @executions += 1
  end

  def execute_action
    # Override to provide functionality.
  end

  def execute_spawn_action
    # Overridden to provide functionality for when the npc spawns.
  end

end

# A random event that spawns and executes some sort of action.
class RandomEvent < TemporaryNpcAction

  def initialize(delay, immediate, hash, combative, target)
    super(delay, immediate, hash)

    @combative = combative
    @target = target
  end

  def execute_spawn_action
    mob.turn_to(target.position)
    mob.update_interacting_mob(target.index)
  end

end

# Adds a random event to the array.
def register_random_event(event)
  RANDOM_EVENTS << event
end

# Contains random event npcs
RANDOM_EVENTS = []

# Spawns a random event for the specified player.
def send_random_event(player)
  position = player.position
  npc_position = Position.new(position.x + 1, position.y, position.height)
  # TODO: Find an unoccupied tile instead of the assumption that (x + 1) is traversable!!

  spawn_random_event(npc_position, false)
end

# Spawns a random event in the specified position.
# If 'combat' is false, only non-combat events will be spawned.
def spawn_random_event(_position, _combat)
  event = RANDOM_EVENTS[rand(RANDOM_EVENTS.size)]
  attempts = 0

  while event.combative && attempts < 5
    event = RANDOM_EVENTS[rand(RANDOM_EVENTS.size)]
    attempts += 1
  end

  event.execute unless attempts == 5 # 5 iterations is plenty, just give up at this point
end
