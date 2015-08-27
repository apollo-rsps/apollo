require 'java'

on :command, :filter do |player, command|
  status = player.toggle_message_filter ? 'enabled' : 'disabled'
  player.send_message('Your message filter is now ' + status + '.')
end
