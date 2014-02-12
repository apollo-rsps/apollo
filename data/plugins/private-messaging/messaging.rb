require 'java'

java_import 'org.apollo.game.event.impl.ForwardPrivateMessageEvent'
java_import 'org.apollo.game.model.World'
java_import 'org.apollo.game.model.settings.PrivacyState'

on :event, :send_private_message do |ctx, player, event|
  friend = World.world.get_player(event.username)
  return unless interaction_permitted(player, friend)

  friend.send(ForwardPrivateMessageEvent.new(player.username, player.privilege_level, event.compressed_message))
end

# Checks if the sender is permitted to interact with the friend they have added:
def interaction_permitted(sender, friend)
  if friend == nil || friend.has_ignored(sender.username)
  	return false
  end
  
  return friend.friends_with(sender.username) ? friend.friend_privacy != PrivacyState::OFF : friend.friend_privacy == PrivacyState::ON
end