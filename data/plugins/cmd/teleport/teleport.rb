require 'java'

java_import 'org.apollo.game.model.Position'

# Sends the player's position.
on :command, :pos, RIGHTS_MOD do |player, command|
  player.send_message('You are at: #{player.position}.')
end

# Teleports the player to the specified position.
on :command, :tele, RIGHTS_ADMIN do |player, command|
  args = command.arguments
  unless (2..3).include?(args.length)
    player.send_message('Invalid syntax - ::tele [x] [y] [optional-z]')
    return
  end

  x = args[0].to_i
  y = args[1].to_i
  z = args.length == 3 ? args[2].to_i : player.position.height

  player.teleport(Position.new(x, y, z)) if (0..4).include?(z)
end