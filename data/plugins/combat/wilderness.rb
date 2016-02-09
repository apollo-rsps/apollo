require 'java'

java_import 'org.apollo.game.model.entity.Player'
java_import 'org.apollo.game.message.impl.SetWidgetTextMessage'
java_import 'org.apollo.game.message.impl.OpenOverlayMessage'

declare_attribute(:wilderness_level, 0, :transient)

# Constants constants related to the wilderness
module WildernessConstants

  # The wilderness level overlay interface id
  OVERLAY_INTERFACE_ID = 197

  # The wilderness level string id
  LEVEL_STRING_ID = 199

end

# Determines the wilderness level for the specified position
def wilderness_level(position)
  ((position.y - 3520) / 8).ceil + 1
end

area_action :wilderness_level do
  on_entry do |player, position|
    player.wilderness_level = wilderness_level(position)
    player.interface_set.open_overlay(WildernessConstants::OVERLAY_INTERFACE_ID)

    id = WildernessConstants::LEVEL_STRING_ID
    player.send(SetWidgetTextMessage.new(id, "Level: #{player.wilderness_level}"))
    show_action(player, ATTACK_ACTION)
  end

  while_in do |player, position|
    current = player.wilderness_level
    updated = wilderness_level(position)

    if current != updated
      player.wilderness_level = updated

      id = WildernessConstants::LEVEL_STRING_ID
      player.send(SetWidgetTextMessage.new(id, "Level: #{player.wilderness_level}"))
    end
  end

  on_exit do |player, position|
    player.wilderness_level = 0
    player.interface_set.close

    player.send(OpenOverlayMessage.new(-1))
    hide_action(player, ATTACK_ACTION)
  end
end

# Monkey patch the existing player class to add method of checking whether or not a player is
# within the wilderness
class Player

  def in_wilderness
    wilderness_level > 0
  end

end

area name: :wilderness, coordinates: [2945, 3522, 3390, 3972, 0], actions: :wilderness_level
