on :event, :add_ignore do |ctx, player, event|
  username = event.username
  player.add_ignore(username)
end

on :event, :remove_ignore do |ctx, player, event|
  username = event.username
  player.remove_ignore(username)
end