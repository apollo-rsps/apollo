require 'java'

java_import 'org.apollo.game.event.impl.ForwardPrivateMessageEvent'
java_import 'org.apollo.game.model.World'
java_import 'org.apollo.game.model.settings.PrivacyState'

on :command, :filter do |player, command|
  player.send_message('Your message filter is now ' + (player.toggle_message_filter ? 'enabled.' : 'disabled.'))
end