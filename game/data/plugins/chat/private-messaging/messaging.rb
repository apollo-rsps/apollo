require 'java'

java_import 'org.apollo.game.message.impl.ForwardPrivateChatMessage'
java_import 'org.apollo.game.model.World'
java_import 'org.apollo.game.model.entity.setting.PrivacyState'

on :message, :private_chat do |player, message|
  friend = $world.get_player(message.username)

  if interaction_permitted(player, friend)
    chat = message.compressed_chat
    friend.send(ForwardPrivateChatMessage.new(player.username, player.privilege_level, chat))
  end
end

# Checks if the sender is permitted to interact with the friend they have added:
def interaction_permitted(sender, friend)
  username = sender.username
  return false if friend.nil? || friend.has_ignored(username)

  privacy = friend.friend_privacy
  friend.friends_with(username) ? (privacy != PrivacyState::OFF) : (privacy == PrivacyState::ON)
end
