require 'java'

java_import 'org.apollo.game.event.impl.ForwardPrivateMessageEvent'
java_import 'org.apollo.game.model.World'
java_import 'org.apollo.game.model.setting.PrivacyState'

on :event, :private_message do |ctx, player, event|
  friend = $world.get_player(event.username)
  friend.send(ForwardPrivateMessageEvent.new(player.username, player.privilege_level, event.compressed_message)) if interaction_permitted(player, friend)
end

# Checks if the sender is permitted to interact with the friend they have added:
def interaction_permitted(sender, friend)
  if friend == nil || friend.has_ignored(sender.username)
    return false
  end
  
  return friend.friends_with(sender.username) ? friend.friend_privacy != PrivacyState::OFF : friend.friend_privacy == PrivacyState::ON
end