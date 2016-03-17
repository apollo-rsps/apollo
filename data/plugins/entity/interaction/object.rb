# Catch all object actions and turn the player to
# the position of the object

on :message, :object_action do |player, message|
  player.turn_to message.position
end