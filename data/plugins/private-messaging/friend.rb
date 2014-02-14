require 'java'

java_import 'org.apollo.game.event.impl.FriendServerStatusEvent'
java_import 'org.apollo.game.event.impl.SendFriendEvent'
java_import 'org.apollo.game.model.Player'
java_import 'org.apollo.game.model.World'
java_import 'org.apollo.game.model.settings.ServerStatus'
java_import 'org.apollo.game.model.settings.PrivacyState'


# Processes an add friend event, updating the logged-in status of the player (and the person they added) if necessary.
on :event, :add_friend do |ctx, player, event|
  friend_username = event.username
  player_username = player.username

  player.add_friend(friend_username)
  friend = World.world.get_player(friend_username)

  if friend == nil # the friend the player added is offline
    player.send(SendFriendEvent.new(friend_username, 0))
  elsif friend.friends_with(player_username) # new friend already has the player added
    friend.send(SendFriendEvent.new(player_username, player.world_id)) unless player.friend_privacy == PrivacyState::OFF # player's private chat state is not off, so notify the friend

    player.send(SendFriendEvent.new(friend_username, friend.world_id)) unless friend.friend_privacy == PrivacyState::OFF # new friend's private chat state is not off, so notify the player
  elsif friend.friend_privacy == PrivacyState::ON # new friend doesn't have the player added but their private chat state is on
   player.send(SendFriendEvent.new(friend_username, friend.world_id)) # so we can let the player know what world they're on
  end
end

# Processes a remove friend event, updating the logged-in status of the player if necessary.
on :event, :remove_friend do |ctx, player, event|
  friend_username = event.username
  player_username = player.username

  player.remove_friend(friend_username)
  if (World.world.is_player_online(friend_username))
    friend = World.world.get_player(friend_username)
    friend.send(SendFriendEvent.new(player_username, 0)) if (friend.friends_with(player_username) && player.friend_privacy != PrivacyState::ON)
  end
end

# Update the friend server status and send the friend/ignore lists of the player logging in.
on :login do |player|
  player.send(FriendServerStatusEvent.new(ServerStatus::CONNECTING))
  player.send(IgnoreListEvent.new(player.ignored_usernames)) if player.ignored_usernames.size > 0

  username = player.username
  world = World.world
  iterator = player.friend_usernames.iterator # Iterate the player's friend list and notify the player that they are online if they are
  while iterator.has_next
    friend_username = iterator.next
    friend = world.get_player(friend_username)
    friend_world_id = (friend == nil || !viewable?(friend, username)) ? 0 : friend.world_id

    player.send(SendFriendEvent.new(friend_username, friend_world_id))
  end

  player.send(FriendServerStatusEvent.new(ServerStatus::ONLINE))
  update_friends(player, player.world_id)
end

# Notifies the player's friends that the player has logged out.
on :logout do |player|
  update_friends(player, 0)
end


# Notifies the currently logged in players that the specified player has logged into the specified world, unless the 
# newly logged-in player has their friend privacy state set to 'off'.
def update_friends(player, world=0)
  privacy = player.friend_privacy
  
  iterator = World.world.player_repository.iterator
  username = player.username

  while iterator.has_next
    other = iterator.next
    next if (!other.friends_with(username) || other == player)

    world = viewable?(player, other.username) ? world : 0
    other.send(SendFriendEvent.new(username, world))
  end
end

# Checks if the specified player can be viewed by the player with the specified other username
def viewable?(player, other_username)
  privacy = player.friend_privacy
  return privacy != PrivacyState::OFF && player.friends_with(other_username) || privacy == PrivacyState::ON
end