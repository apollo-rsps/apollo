require 'java'

java_import 'org.apollo.game.message.impl.DisplayCrossbonesMessage'
java_import 'org.apollo.game.model.entity.Player'

# Registers an area action.
def area_action(name, &block)
  AREA_ACTIONS[name] = action = AreaAction.new
  action.instance_eval(&block)
end

AREA_ACTIONS = {}

private

# An action that is called when a player enters or exits an area.
class AreaAction

  # Sets the block to be called when the player enters the area.
  def on_entry(&block)
    @on_enter = block
  end

  # Sets the block to be called while the player is in the area.
  def while_in(&block)
    @while_in = block
  end

  # Sets the block to be called when the player exits the area.
  def on_exit(&block)
    @on_exit = block
  end

  # Called when the player has entered an area this action is registered to.
  def entered(player, position)
    @on_enter.call(player, position) unless @on_enter.nil?
  end

  # Called while the player is in area this action is registered to.
  def inside(player, position)
    @while_in.call(player, position) unless @while_in.nil?
  end

  # Called when the player has exited an area this action is registered to.
  def exited(player, position)
    @on_exit.call(player, position) unless @on_exit.nil?
  end

end
