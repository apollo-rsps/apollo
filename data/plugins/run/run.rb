WALK_BUTTON_ID = 152
RUN_BUTTON_ID = 153

on :button, WALK_BUTTON_ID do |player|
  player.set_running(false)
end

on :button, RUN_BUTTON_ID do |player|
	player.set_running(true)
end

on :command, :run do |player, command|
	running = player.is_running	
	player.set_running(!running)
	
	player.send_message(running ? "You are now running." : "You are no longer running.")
end