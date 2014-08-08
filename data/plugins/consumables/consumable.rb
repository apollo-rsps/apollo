require 'java'

# A map of item ids to consumables.
CONSUMABLES = {}

CONSUME_ANIMATION_ID = 829

# An item that can be consumed to produce a skill effect.
class Consumable    
  attr_reader :name, :id

  def initialize(name, id, sound_id)
    @name = name.to_s.gsub(/_/, ' ')
    @id = id
    @sound_id = sound_id
  end

  def consume(player)
  # Override to provide specific functionality.
  end

end

# Appends a consumable to the map, with its id as the key.
def append_consumable(consumable)
  CONSUMABLES[consumable.id] = consumable
end

class ConsumeAction < Action
  attr_reader :consumable

  def initialize(player, slot, consumable)
    super(0, true, player)
    @consumable = consumable
    @slot = slot
    @executions = 0
  end

  def execute()
    if @executions == 0
      mob.inventory.reset(@slot)
      consumable.consume(mob)
      mob.play_animation(Animation.new(CONSUME_ANIMATION_ID))
      @executions += 1
    else
      stop
    end
  end

  def equals(other)
    return (mob == other.mob && @consumable.id == other.consumable.id)
  end
  
end

# Intercepts the first item option message and consumes the consumable, if necessary.
on :message, :first_item_option do |ctx, player, message|
  consumable = CONSUMABLES[message.id]
  unless consumable == nil
    player.start_action(ConsumeAction.new(player, message.slot, consumable))
    ctx.break_handler_chain
  end
end