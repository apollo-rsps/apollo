require 'java'

java_import 'org.apollo.game.model.Player'
java_import 'org.apollo.game.sync.block.SynchronizationBlock'

on :command, :headicon, RIGHTS_ADMIN do |player, command|
  args = command.arguments
  unless (args.length == 1)
    player.send_message("Usage - ::headicon [id]")
    break
  end

  player.head_icon = args[0].to_i
  player.send_message("Adding headicon")
  player.block_set.add SynchronizationBlock.create_appearance_block(player)
end

on :command, :prayicon, RIGHTS_ADMIN do |player, command|
  args = command.arguments
  unless (args.length == 1)
    player.send_message("Usage - ::prayicon [id]")
    break
  end

  player.prayer_icon = args[0].to_i
  player.send_message("Adding prayicon")
  player.block_set.add SynchronizationBlock.create_appearance_block(player)
end