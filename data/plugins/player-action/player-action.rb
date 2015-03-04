require 'java'

java_import 'org.apollo.game.message.impl.SetPlayerActionMessage'
java_import 'org.apollo.game.model.entity.Player'



class PlayerAction
  attr_reader :slot, :primary, :name

  def initialize(slot, primary, name)
    index = [ :first, :second, :third, :fourth, :fifth ].find_index(slot)
    raise "Unsupport action slot #{slot}." if index.nil?

    @slot = index
    @primary = primary
    @name = name
  end

end

ATTACK_ACTION = PlayerAction.new(:third, true, 'Attack')
CHALLENGE_ACTION = PlayerAction.new(:third, true, 'Challenge')
TRADE_ACTION = PlayerAction.new(:fourth, true, 'Trade with')
FOLLOW_ACTION = PlayerAction.new(:fifth, true, 'Follow')

# Shows multiple context menu action for the specified player
def show_actions(player, *actions)
  raise 'Must specify at least one action' if actions.nil?

  actions.each do |action|
    player.add_action(action)
    player.send(SetPlayerActionMessage.new(action.name, action.slot, action.primary))
  end
end

# Shows a single context menu action for the specified player
def show_action(player, action)
  show_actions(player, action)
end

# Hides a context menu action for the specified player
def hide_action(player, action)
  show_action(player, PlayerAction.new(action.slot, action.primary, 'null'))
end

class Player

  def actions
    @actions ||= {}
  end

  def add_action(action)
    actions[action.slot] = action.name
  end

  def has_action(action)
    return actions[action.slot] == action.name
  end

end