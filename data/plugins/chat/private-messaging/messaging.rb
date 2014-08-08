require 'java'

java_import 'org.apollo.game.message.impl.ForwardPrivateChatMessage'
java_import 'org.apollo.game.model.World'
java_import 'org.apollo.game.model.setting.PrivacyState'

on :message, :private_message do |ctx, player, message|
  friend = $world.get_player(message.username)
  friend.send(ForwardPrivateChatMessage.new(player.username, player.privilege_level, message.compressed_message)) if interaction_permitted(player, friend)
end

# Checks if the sender is permitted to interact with the friend they have added:
def interaction_permitted(sender, friend)
  if friend == nil || friend.has_ignored(sender.username)
    return false
  end
  
  return friend.friends_with(sender.username) ? friend.friend_privacy != PrivacyState::OFF : friend.friend_privacy == PrivacyState::ON
end