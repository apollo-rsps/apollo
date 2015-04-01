on :message, :add_ignore do |player, message|
  username = message.username
  player.add_ignore(username)
end

on :message, :remove_ignore do |player, message|
  username = message.username
  player.remove_ignore(username)
end
