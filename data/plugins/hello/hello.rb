require 'java'
java_import 'org.apollo.game.model.Animation'
java_import 'org.apollo.game.model.World'

SERVER_RELEASE = World.world.release_number

on :command, :hello do |player, command|
  player.play_animation Animation::WAVE
  player.send_message("Hello, World!")
end

on :command, :release do |player, command|
  player.send_message("This server is currently running the #{SERVER_RELEASE} release.")
end