java_import 'org.apollo.game.model.entity.Player'

on :login do |_event, player|
  show_action(player, TRADE_ACTION)
  show_action(player, FOLLOW_ACTION)
end
