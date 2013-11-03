WALK_BUTTON_ID = 152
RUN_BUTTON_ID = 153

on :button, WALK_BUTTON_ID do |player|
  player.running = false
end

on :button, RUN_BUTTON_ID do |player|
  player.running = true
end

on :command, :run do |player, command|
  running = player.is_running	
  player.running = !running

  player.send_message(running ? "You are now running." : "You are no longer running.")
end