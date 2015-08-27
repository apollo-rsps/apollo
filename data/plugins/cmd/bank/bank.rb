require 'java'

# Opens the player's bank.
on :command, :bank, RIGHTS_ADMIN do |player, _command|
  player.open_bank
end
