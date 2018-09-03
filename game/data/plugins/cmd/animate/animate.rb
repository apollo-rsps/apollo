require 'java'

java_import 'org.apollo.game.model.Animation'
java_import 'org.apollo.game.model.Graphic'

# Makes the player perform the animation with the specified id.
on :command, :animate, RIGHTS_MOD do |player, command|
  args = command.arguments
  next unless valid_arg_length(args, 1, player, 'Invalid syntax - ::animate [animation-id]')

  player.play_animation(Animation.new(args[0].to_i))
end

# Makes the player perform the graphic with the specified id.
on :command, :graphic, RIGHTS_MOD do |player, command|
  args = command.arguments
  next unless valid_arg_length(args, 1, player, 'Invalid syntax - ::graphic [graphic-id]')

  player.play_graphic(Graphic.new(args[0].to_i))
end
