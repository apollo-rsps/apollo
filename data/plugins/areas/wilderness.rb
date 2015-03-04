require 'java'

java_import 'org.apollo.game.model.entity.Player'
java_import 'org.apollo.game.message.impl.SetWidgetTextMessage'
java_import 'org.apollo.game.message.impl.OpenOverlayMessage'


# Constants constants related to the wilderness
module WildernessConstants

  # The wilderness level overlay interface id
  OVERLAY_INTERFACE_ID = 197

  # The wilderness level string id
  LEVEL_STRING_ID = 199

end

declare_attribute(:wilderness_level, 0, :transient)

# Determines the wilderness level for the specified player's position
def wilderness_level(player)
  return ((player.position.y - 3520) / 8).ceil
end

area_action :wilderness_level do

  on_entry do |player| 
    player.wilderness_level = wilderness_level(player)
    player.interface_set.open_overlay(WildernessConstants::OVERLAY_INTERFACE_ID)
    player.send(SetWidgetTextMessage.new(WildernessConstants::LEVEL_STRING_ID, "Level: #{player.wilderness_level}"))
    show_action(player, ATTACK_ACTION)
  end

  while_in do |player|
    current = player.wilderness_level
    updated = wilderness_level(player)
    if (current != updated)
        player.wilderness_level = updated
        player.send(SetWidgetTextMessage.new(WildernessConstants::LEVEL_STRING_ID, "Level: #{player.wilderness_level}"))
    end
  end

  on_exit do |player|
    player.wilderness_level = 0
    player.interface_set.close()
    player.send(OpenOverlayMessage.new(-1))
    hide_action(player, ATTACK_ACTION)
  end

end

area :name => :wilderness, :coordinates => [ 2945, 3522, 3390, 3972, 0 ], :actions => :wilderness_level