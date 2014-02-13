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

  if (friend == nil) # the friend the player added is offline
    player.send(SendFriendEvent.new(friend_username, 0))
  elsif (friend.friends_with(player_username)) # new friend already has the player added
    unless (player.friend_privacy == PrivacyState::OFF) # player's private chat state is not off
      friend.send(SendFriendEvent.new(player_username, player.world_id)) # ... so we can tell the friend what world they're on
    end

    unless (friend.friend_privacy == PrivacyState::OFF) # new friend's private chat state is not off 
      player.send(SendFriendEvent.new(friend_username, friend.world_id)) # ... so we can let the player know what world they're on
    end
  elsif (friend.friend_privacy == PrivacyState::ON) # new friend doesn't have the player added but their private chat state is online
	  player.send(SendFriendEvent.new(friend_username, friend.world_id)) # ... so we can let the player know what world they're on
  end
end

# Processes a remove friend event, updating the logged-in status of the player if necessary.
on :event, :remove_friend do |ctx, player, event|
  friend_username = event.username
  player_username = player.username

  player.remove_friend(friend_username)
  friend = World.world.get_player(friend_username)
  next if friend == nil

  friend.send(SendFriendEvent.new(player_username, 0)) if (friend.friends_with(player_username) && player.friend_privacy != PrivacyState::ON)
end

# Update the friend server status and send the friend/ignore lists of the player logging in.
on :login do |player|
  player.send(FriendServerStatusEvent.new(ServerStatus::CONNECTING))

  player.send(IgnoreListEvent.new(player.ignored_usernames)) if player.ignored_usernames.size > 0

  world = World.world
  iterator = player.friend_usernames.iterator
  while iterator.has_next
    username = iterator.next
    world_id = world.is_player_online(username) ? world.get_player(username).world_id : 0;
    player.send(SendFriendEvent.new(username, world_id))
  end

  player.send(FriendServerStatusEvent.new(ServerStatus::ONLINE))
  update_friends(player, player.world_id)
end

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
    next if other == player
    other.send(SendFriendEvent.new(username, world)) if (player.friends_with(other.username) || player.friend_privacy == PrivacyState::ON)
  end
end