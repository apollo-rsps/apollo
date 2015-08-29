require 'java'

java_import 'org.apollo.game.action.DistancedAction'
java_import 'org.apollo.game.model.inter.bank.BankUtils'

BANK_BOOTH_ID = 2213
BANK_BOOTH_SIZE = 1

# The npcs with a 'bank' menu action.
BANKER_NPCS = [166, 494, 495, 496, 497, 498, 499, 1036, 1360, 1702, 2163, 2164, 2354, 2355, 2568,
               2569, 2570]

# A distanced action to open a new bank.
class BankAction < DistancedAction
  attr_reader :position

  def initialize(mob, position)
    super(0, true, mob, position, BANK_BOOTH_SIZE)
    @position = position
  end

  def executeAction
    mob.turn_to(@position)
    BankUtils.open_bank(mob)
    stop
  end

  def equals(other)
    get_class == other.get_class && @position == other.position
  end
end

# Intercepts the object action message
on :message, :second_object_action do |player, message|
  if message.id == BANK_BOOTH_ID
    player.start_action(BankAction.new(player, message.position))
    message.terminate
  end
end

on :message, :second_npc_action do |player, message|
  npc = $world.npc_repository.get(message.index)
  if BANKER_NPCS.include?(npc.id)
    player.start_action(BankAction.new(player, npc.position))
    message.terminate
  end
end
