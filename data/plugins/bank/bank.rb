require 'java'
java_import 'org.apollo.game.action.DistancedAction'
java_import 'org.apollo.game.model.inter.bank.BankUtils'

BANK_BOOTH_ID = 2213
BANK_BOOTH_SIZE = 1

class BankAction < DistancedAction
  attr_reader :position

  def initialize(character, position)
    super 0, true, character, position, BANK_BOOTH_SIZE
    @position = position
  end

  def executeAction
    character.turn_to @position
    BankUtils.open_bank character
    stop
  end

  def equals(other)
    return (get_class == other.get_class and @position == other.position)
  end
end

on :event, :object_action do |ctx, player, event|
  if event.option == 2 and event.id == BANK_BOOTH_ID
    player.startAction BankAction.new(player, event.position)
  end
end

# TODO: when we have NPCs/dialogue, also respond to clicking on them
