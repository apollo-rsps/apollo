require 'java'

java_import 'org.apollo.game.model.World'
java_import 'org.apollo.game.model.entity.Player'

# Adds a command to mute a player. Admins cannot be muted.
on :command, :mute, RIGHTS_MOD do |player, command|
  name = command.arguments.to_a.join(' ')
  on_player = $world.get_player(name)

  if validate(player, on_player)
    on_player.muted = true
    on_player.send_message('You have just been muted.')
    player.send_message("You have just muted #{on_player.get_username}.")
  end
end

# Adds a command to unmute a player.
on :command, :unmute, RIGHTS_MOD do |player, command|
  name = command.arguments.to_a.join(' ')
  on_player = $world.get_player(name)

  if validate(player, on_player)
    on_player.muted = false
    on_player.send_message('You are no longer muted.')
    player.send_message("You have just unmuted #{on_player.get_username}.")
  end
end

# Adds a command to ban a player. Admins cannot be banned.
on :command, :ban, RIGHTS_ADMIN do |player, command|
  name = command.arguments.to_a.join(' ')
  on_player = $world.get_player(name)

  if validate(player, on_player)
    on_player.banned = true
    on_player.logout # TODO force logout
    player.send_message("You have just banned #{on_player.get_username}.")
  end
end

# Ensures the player isn't nil, and that they aren't an Administrator.
def validate(player, on_player)
  if on_player.nil?
    player.send_message('That player does not exist.')
    return false
  elsif on_player.get_privilege_level == RIGHTS_ADMIN
    player.send_message('You cannot perform this action on Administrators.')
    return false
  end
  
  true
end
