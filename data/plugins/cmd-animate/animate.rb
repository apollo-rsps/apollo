require 'java'

java_import 'org.apollo.game.model.Animation'
java_import 'org.apollo.game.model.Graphic'

on :command, :animate, RIGHTS_MOD do |player, command|
  args = command.arguments
  unless args.length == 1
    player.send_message('Invalid syntax - ::animate [animation-id]')
    return
  end

  player.play_animation(Animation.new(args[0].to_i))
end

on :command, :graphic, RIGHTS_MOD do |player, command|
  args = command.arguments
  unless args.length == 1
    player.send_message('Invalid syntax - ::graphic [graphic-id]')
    return
  end

  player.play_graphic(Graphic.new(args[0].to_i))
end