require 'java'

java_import 'org.apollo.game.message.impl.SetPlayerActionMessage'
java_import 'org.apollo.game.model.entity.Player'

# A right-click action for a Player.
class PlayerAction
  attr_reader :slot, :primary, :name

  def initialize(slot, primary, name)
    index = [:first, :second, :third, :fourth, :fifth].find_index(slot)
    fail "Unsupported action slot #{slot}." if index.nil?

    @slot = index
    @primary = primary
    @name = name
  end

end

ATTACK_ACTION = PlayerAction.new(:second, true, 'Attack')
CHALLENGE_ACTION = PlayerAction.new(:second, true, 'Challenge')
FOLLOW_ACTION = PlayerAction.new(:fourth, true, 'Follow')
TRADE_ACTION = PlayerAction.new(:fifth, true, 'Trade with')

# Shows multiple context menu action for the specified player
def show_actions(player, *actions)
  fail 'Must specify at least one action.' if actions.nil?

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
  player.send(SetPlayerActionMessage.new('null', action.slot, action.primary))
end

# Monkey-patch Player to provide action utility methods.
class Player

  def actions
    @actions ||= {}
  end

  def add_action(action)
    actions[action.slot] = action.name
  end

  def action?(action)
    actions[action.slot] == action.name
  end

end
