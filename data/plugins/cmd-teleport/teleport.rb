require 'java'
java_import 'org.apollo.game.model.Position'

on :command, :pos, RIGHTS_ADMIN do |player, command|
  player.send_message "You are at: " + player.position.to_s
end

on :command, :tele, RIGHTS_ADMIN do |player, command|
  args = command.arguments
  if (2..3).include? args.length
    x = args[0].to_i
    y = args[1].to_i
    z = args.length == 3 ? args[2].to_i : 0

    player.teleport Position.new(x, y, z)
  else
    player.send_message "Syntax: ::tele [x] [y] [z=0]"
  end
end
