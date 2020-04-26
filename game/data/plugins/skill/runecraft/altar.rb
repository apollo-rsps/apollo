require 'java'

java_import 'org.apollo.game.action.DistancedAction'
java_import 'org.apollo.game.model.Position'

PORTALS = {}
ENTRANCE_ALTARS = {}
CRAFTING_ALTARS = {}

# Represents a runecrafting altar.
class Altar
  attr_reader :entrance_altar, :crafting, :portal_id, :entrance, :exit, :crafting_centre

  def initialize(entrance_altar, crafting, portal_id, entrance_position, exit_position,
                 crafting_centre)
    @entrance_altar = entrance_altar
    @altar = crafting
    @portal_id = portal_id
    @entrance_position = entrance_position
    @exit_position = exit_position
    @crafting_centre = crafting_centre
  end

end

# Intercepts the item on object message.
on :message, :item_on_object do |player, message|
  talisman = TALISMANS[message.id]
  altar = ENTRANCE_ALTARS[message.object_id]

  unless talisman.nil? || altar.nil?
    player.start_action(TeleportAction.new(player, message.position, 2, altar.entrance_position))
    message.terminate
  end
end

# Intercepts the first object action message.
on :message, :object_action do |player, message|
  if message.option == 1
    object_id = message.id

    if PORTALS.key?(object_id)
      altar = PORTALS[object_id]
      entrance = altar.entrance_position

      player.start_action(TeleportAction.new(player, entrance, 1, altar.exit_position))
      message.terminate
    elsif RUNES.key?(object_id)
      rune = RUNES[object_id]
      altar = CRAFTING_ALTARS[object_id]

      player.start_action(RunecraftingAction.new(player, rune, altar.crafting_centre))
      message.terminate
    end
  end
end

# An action that causes a mob to teleport when it comes within the specified distance of a
# specified position.
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
    get_class == other.get_class && mob == other.mob &&
      @teleport_position == other.teleport_position
  end

end

# Appends an altar to the list.
def altar(name, hash)
  unless hash.has_keys?(:entrance_altar, :crafting, :portal, :entrance, :exit, :altar_centre)
    fail "#{name} is missing one of: entrance altar id, crafting altar id, entrance portal position, "\
         "and altar centre position."
  end

  entrance_altar, crafting = hash[:entrance_altar], hash[:crafting]
  portal_id = hash[:portal]

  entrance = Position.new(*hash[:entrance])
  exit_position = Position.new(*hash[:exit])
  centre = Position.new(*hash[:altar_centre])

  altar = Altar.new(entrance_altar, crafting, portal_id, entrance, exit_position, centre)
  PORTALS[portal_id] = ENTRANCE_ALTARS[entrance_altar] = CRAFTING_ALTARS[crafting] = altar
end

altar :air, entrance_altar: 2452, crafting: 2478, portal: 2465,
            entrance: [2841, 4829], exit: [2983, 3292], altar_centre: [2844, 4834]

altar :mind, entrance_altar: 2453, crafting: 2479, portal: 2466,
             entrance: [2793, 4828], exit: [2980, 3514], altar_centre: [2786, 4841]

altar :water, entrance_altar: 2454, crafting: 2480, portal: 2467,
              entrance: [2726, 4832], exit: [3187, 3166], altar_centre: [2716, 4836]

altar :earth, entrance_altar: 2455, crafting: 2481, portal: 2468,
              entrance: [2655, 4830], exit: [3304, 3474], altar_centre: [2658, 4841]

altar :fire, entrance_altar: 2456, crafting: 2482, portal: 2469,
             entrance: [2574, 4849], exit: [3311, 3256], altar_centre: [2585, 4838]

altar :body, entrance_altar: 2457, crafting: 2483, portal: 2470,
             entrance: [2524, 4825], exit: [3051, 3445], altar_centre: [2525, 4832]

altar :cosmic, entrance_altar: 2458, crafting: 2484, portal: 2471,
               entrance: [2142, 4813], exit: [2408, 4379], altar_centre: [2142, 4833]

altar :law, entrance_altar: 2459, crafting: 2485, portal: 2472,
            entrance: [2464, 4818], exit: [2858, 3379], altar_centre: [2464, 4832]

altar :nature, entrance_altar: 2460, crafting: 2486, portal: 2473,
               entrance: [2400, 4835], exit: [2867, 3019], altar_centre: [2400, 4841]

altar :chaos, entrance_altar: 2461, crafting: 2487, portal: 2474,
              entrance: [2268, 4842], exit: [3058, 3591], altar_centre: [2271, 4842]

altar :death, entrance_altar: 2462, crafting: 2488, portal: 2475,
              entrance: [2208, 4830], exit: [3222, 3222], altar_centre: [2205, 4836]
