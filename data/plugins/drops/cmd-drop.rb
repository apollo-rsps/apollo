require 'java'

java_import 'org.apollo.game.model.Player'
java_import 'org.apollo.game.event.impl.PositionEvent'
java_import 'org.apollo.game.event.impl.SetTileItemEvent'

on :command, :drop, RIGHTS_ADMIN do |player, command|
  player.send(PositionEvent.new(player.position, player.position))
  player.send(SetTileItemEvent.new(70, 1))
end