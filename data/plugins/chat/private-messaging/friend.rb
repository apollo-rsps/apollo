require 'java'

java_import 'org.apollo.game.message.impl.FriendServerStatusMessage'
java_import 'org.apollo.game.message.impl.IgnoreListMessage'
java_import 'org.apollo.game.message.impl.SendFriendMessage'
java_import 'org.apollo.game.model.World'
java_import 'org.apollo.game.model.entity.setting.ServerStatus'
java_import 'org.apollo.game.model.entity.setting.PrivacyState'
java_import 'org.apollo.game.model.entity.Player'

# Processes an add friend message, updating the logged-in status of the player (and the person they
# added) if necessary.
on :message, :add_friend do |player, message|
  friend_username = message.username
  player_username = player.username

  player.add_friend(friend_username)
  friend = $world.get_player(friend_username)

  if friend.nil? # the friend the player added is offline
    player.send(SendFriendMessage.new(friend_username, 0))
  elsif friend.friends_with(player_username)

    # player's private chat state is not off, so notify the friend
    unless player.friend_privacy == PrivacyState::OFF
      friend.send(SendFriendMessage.new(player_username, player.world_id))
    end

    # new friend's private chat state is not off, so notify the player
    unless friend.friend_privacy == PrivacyState::OFF
      player.send(SendFriendMessage.new(friend_username, friend.world_id))
    end
  elsif friend.friend_privacy == PrivacyState::ON
    # new friend doesn't have the player added but their private chat state is on, so inform the
    # player of the world they are on.
    player.send(SendFriendMessage.new(friend_username, friend.world_id))
  end
end

# Processes a remove friend message, updating the logged-in status of the player if necessary.
on :message, :remove_friend do |player, message|
  friend_username = message.username
  player_username = player.username

  player.remove_friend(friend_username)
  if $world.is_player_online(friend_username)
    friend = $world.get_player(friend_username)

    remove = friend.friends_with(player_username) && player.friend_privacy != PrivacyState::ON
    friend.send(SendFriendMessage.new(player_username, 0)) if remove
  end
end

# Update the friend server status and send the friend/ignore lists of the player logging in.
on :login do |_event, player|
  player.send(FriendServerStatusMessage.new(ServerStatus::CONNECTING))
  player.send(IgnoreListMessage.new(player.ignored_usernames)) if player.ignored_usernames.size > 0

  username = player.username
  world = $world
  iterator = player.friend_usernames.iterator

  # Iterate the player's friend list and notify the player that they are online if they are
  while iterator.has_next
    friend_username = iterator.next
    friend = world.get_player(friend_username)
    friend_world_id = (friend.nil? || !viewable?(friend, username)) ? 0 : friend.world_id

    player.send(SendFriendMessage.new(friend_username, friend_world_id))
  end

  player.send(FriendServerStatusMessage.new(ServerStatus::ONLINE))
  update_friends(player, player.world_id)
end

# Notifies the player's friends that the player has logged out.
on :logout do |_event, player|
  update_friends(player, 0)
end

# Notifies the currently logged in players that the specified player has logged into the specified
#  world, unless the newly logged-in player has their friend privacy state set to 'off'.
def update_friends(player, world = 0)
  privacy = player.friend_privacy

  iterator = $world.player_repository.iterator
  username = player.username

  while iterator.has_next
    other = iterator.next
    next if !other.friends_with(username) || other == player

    world = viewable?(player, other.username) ? world : 0
    other.send(SendFriendMessage.new(username, world))
  end
end

# Checks if the specified player can be viewed by the player with the specified other username
def viewable?(player, other_username)
  privacy = player.friend_privacy
  privacy != PrivacyState::OFF && player.friends_with(other_username) || privacy == PrivacyState::ON
end
