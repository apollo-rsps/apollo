require 'java'

on :command, :filter do |player, command|
  player.send_message('Your message filter is now ' + (player.toggle_message_filter ? 'enabled.' : 'disabled.'))
end