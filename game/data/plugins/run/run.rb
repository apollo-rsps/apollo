WALK_BUTTON_ID = 152
RUN_BUTTON_ID = 153

on :button, WALK_BUTTON_ID do |player|
  player.toggle_running
end

on :button, RUN_BUTTON_ID do |player|
  player.toggle_running
end
