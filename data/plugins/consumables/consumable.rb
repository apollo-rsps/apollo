require 'java'

# A map of item ids to consumables.
CONSUMABLES = {}

# The id of the food consumption animation.
CONSUME_ANIMATION_ID = 829

# Contains the different types of consumables
module ConsumableType
  FOOD = 1
  POTION = 2
  DRINK = 3
end

# An item that can be consumed to produce a skill effect.
class Consumable
  attr_reader :name, :id, :sound, :delay, :type

  def initialize(name, id, sound, delay, type)
    @name = name.to_s.gsub(/_/, ' ')
    @id = id
    @sound = sound
    @delay = delay
    @type = type
  end

  def consume(_player)
    # Override to provide specific functionality.
  end

end

# Appends a consumable to the map, with its id as the key.
def append_consumable(consumable)
  CONSUMABLES[consumable.id] = consumable
end

# An Action used for food consumption.
class ConsumeAction < Action
  attr_reader :consumable

  def initialize(player, slot, consumable)
    super(2, true, player)
    @consumable = consumable
    @slot = slot
    @executions = 0
  end

  def execute
    if @executions == 0
      mob.inventory.reset(@slot)
      @consumable.consume(mob)

      mob.play_animation(Animation.new(CONSUME_ANIMATION_ID))
    end

    @executions += 1

    if @executions >= @consumable.delay
      stop
    end
  end

  def equals(other)
    get_class == other.get_class && mob == other.mob && @consumable.type == other.consumable.type
  end

end

# Intercepts the first item option message and consumes the consumable, if necessary.
on :message, :first_item_option do |player, message|
  consumable = CONSUMABLES[message.id]

  unless consumable.nil?
    player.start_action(ConsumeAction.new(player, message.slot, consumable))
    message.terminate
  end
end
