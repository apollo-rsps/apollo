require 'java'

java_import 'org.apollo.game.action.DistancedAction'
java_import 'org.apollo.game.model.Position'

PORTALS = {}
ENTRANCE_ALTARS = {}
CRAFTING_ALTARS = {}

# Represents a runecrafting altar.
class Altar
  attr_reader :entrance_altar, :crafting_altar, :portal_id, :entrance_position, :exit_position, :crafting_centre

  def initialize(entrance_altar, crafting_altar, portal_id, entrance_position, exit_position,crafting_centre)
    @entrance_altar = entrance_altar
    @altar = crafting_altar
    @portal_id = portal_id
    @entrance_position = entrance_position
    @exit_position = exit_position
    @crafting_centre = crafting_centre
  end

end


# Intercepts the item on object message.
on :message, :item_on_object do |ctx, player, message|
  talisman = TALISMANS[message.id]; altar = ENTRANCE_ALTARS[message.object_id]
  if (talisman != nil && altar != nil)
    player.start_action(TeleportAction.new(player, message.position, 2, altar.entrance_position))
    ctx.break_handler_chain
  end
end

# Intercepts the first object action message.
on :message, :object_action do |ctx, player, message|
  if (message.option == 1)
    object_id = message.id
    if (altar = PORTALS[object_id]) != nil # Get the altar associated with this exit portal.
      player.start_action(TeleportAction.new(player, altar.entrance_position, 1, altar.exit_position))
      ctx.break_handler_chain
    elsif (rune = RUNES[object_id]) != nil # Get the rune associated with this altar.
      altar = CRAFTING_ALTARS[object_id]
      player.start_action(RunecraftingAction.new(player, rune, altar.crafting_centre))
      ctx.break_handler_chain
    end
  end
end


# An action that causes a mob to teleport when it comes within the specified distance of a specified position.
class TeleportAction < DistancedAction
  attr_reader :teleport_position

  def initialize(mob, position, distance, teleport_position)
    super(0, true, mob, position, distance)
    @teleport_position = teleport_position
  end

  def executeAction
    mob.teleport(@teleport_position)
    stop
  end

  def equals(other)
    return (get_class == other.get_class && mob == other.mob && @teleport_position == other.teleport_position)
  end

end

# Appends an altar to the list.
def append_altar(hash)
  #raise 'Hash must contain an entrance altar id, crafting altar id, entrance portal position, and altar centre position.'
  entrance_altar = hash[:entrance_altar]; crafting_altar = hash[:crafting_altar]; portal_id = hash[:exit_portal]; entrance_position = hash[:entrance_position]; exit_position = hash[:exit_position]; altar_centre = hash[:altar_centre]

  PORTALS[portal_id] = ENTRANCE_ALTARS[entrance_altar] = CRAFTING_ALTARS[crafting_altar] = Altar.new(entrance_altar, crafting_altar, portal_id, Position.new(*entrance_position), Position.new(*exit_position), Position.new(*altar_centre))
end

# Appends an altar to the list.
append_altar :name => :air_altar,    :entrance_altar => 2452, :crafting_altar => 2478, :exit_portal => 2465, :entrance_position => [ 2841, 4829 ], :exit_position => [ 2983, 3292 ], :altar_centre => [ 2844, 4834 ]
append_altar :name => :mind_altar,   :entrance_altar => 2453, :crafting_altar => 2479, :exit_portal => 2466, :entrance_position => [ 2793, 4828 ], :exit_position => [ 2980, 3514 ], :altar_centre => [ 2786, 4841 ]
append_altar :name => :water_altar,  :entrance_altar => 2454, :crafting_altar => 2480, :exit_portal => 2467, :entrance_position => [ 2726, 4832 ], :exit_position => [ 3187, 3166 ], :altar_centre => [ 2716, 4836 ]
append_altar :name => :earth_altar,  :entrance_altar => 2455, :crafting_altar => 2481, :exit_portal => 2468, :entrance_position => [ 2655, 4830 ], :exit_position => [ 3304, 3474 ], :altar_centre => [ 2658, 4841 ]
append_altar :name => :fire_altar,   :entrance_altar => 2456, :crafting_altar => 2482, :exit_portal => 2469, :entrance_position => [ 2574, 4849 ], :exit_position => [ 3311, 3256 ], :altar_centre => [ 2585, 4838 ]
append_altar :name => :body_altar,   :entrance_altar => 2457, :crafting_altar => 2483, :exit_portal => 2470, :entrance_position => [ 2524, 4825 ], :exit_position => [ 3051, 3445 ], :altar_centre => [ 2525, 4832 ]
append_altar :name => :cosmic_altar, :entrance_altar => 2458, :crafting_altar => 2484, :exit_portal => 2471, :entrance_position => [ 2142, 4813 ], :exit_position => [ 2408, 4379 ], :altar_centre => [ 2142, 4833 ]
append_altar :name => :law_altar,    :entrance_altar => 2459, :crafting_altar => 2485, :exit_portal => 2472, :entrance_position => [ 2464, 4818 ], :exit_position => [ 2858, 3379 ], :altar_centre => [ 2464, 4832 ]
append_altar :name => :nature_altar, :entrance_altar => 2460, :crafting_altar => 2486, :exit_portal => 2473, :entrance_position => [ 2400, 4835 ], :exit_position => [ 2867, 3019 ], :altar_centre => [ 2400, 4841 ]
append_altar :name => :chaos_altar,  :entrance_altar => 2461, :crafting_altar => 2487, :exit_portal => 2474, :entrance_position => [ 2268, 4842 ], :exit_position => [ 3058, 3591 ], :altar_centre => [ 2271, 4842 ]
append_altar :name => :death_altar,  :entrance_altar => 2462, :crafting_altar => 2488, :exit_portal => 2475, :entrance_position => [ 2208, 4830 ], :exit_position => [ 3222, 3222 ], :altar_centre => [ 2205, 4836 ]