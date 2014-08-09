require 'java'

java_import 'org.apollo.game.model.inter.bank.BankUtils'

# Opens the player's bank.
on :command, :bank, RIGHTS_ADMIN do |player, command|
  BankUtils.open_bank(player)
end