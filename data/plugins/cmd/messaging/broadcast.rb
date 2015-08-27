require 'java'

java_import 'org.apollo.game.model.World'
java_import 'org.apollo.game.model.entity.Player'

# Adds a command to broadcast a message to every player on the server.
on :command, :broadcast, RIGHTS_ADMIN do |player, command|
  message = command.arguments.to_a.join(' ')
  broadcast = "[Broadcast] #{player.get_username.capitalize}: #{message}"

  $world.player_repository.each { |other| other.send_message(broadcast) }
end
