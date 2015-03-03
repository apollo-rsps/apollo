require 'java'

java_import 'org.apollo.game.model.entity.Player'
java_import 'org.apollo.game.message.impl.OpenOverlayMessage'
java_import 'org.apollo.game.message.impl.SetWidgetTextMessage'



private

MIN_X = 2945
MIN_Y = 3522
MAX_X = 3390
MAX_Y = 3972

OVERLAY_INTERFACE_ID = 197
LEVEL_STRING_ID = 199

declare_attribute(:wilderness_level, 0, :transient)

# Determines the wilderness level for the specified player's position
def wilderness_level(player)
  return (player.position.y - (MIN_Y - 1) / 8).ceil
end

area_action :wilderness_level do

  on_entry do |player| 
    player.wilderness_level = wilderness_level(player)
    player.interface_set.open_overlay(OVERLAY_INTERFACE_ID)
    player.send(SetWidgetTextMessage.new(LEVEL_STRING_ID, "Level: #{player.wilderness_level}"))
    show_action(ATTACK_ACTION)
  end

  while_in do |player|
    current = player.wilderness_level
    updated = wilderness_level(player)
    if (current != updated)
        player.wilderness_level = updated
        player.send(SetWidgetTextMessage.new(LEVEL_STRING_ID, "Level: #{player.wilderness_level}"))
    end
  end

  on_exit do |player|
    player.wilderness_level = 0
    player.interface_set.close() # TODO: Will this cause issues with other potentially open interfaces?
    hide_action(ATTACK_ACTION)
  end

end

area :name => :wilderness, :coordinates => MIN_X, MIN_Y, MAX_X, MAX_Y, 0, => :actions => :wilderness_level